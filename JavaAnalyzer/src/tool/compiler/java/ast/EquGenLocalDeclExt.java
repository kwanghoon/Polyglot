package tool.compiler.java.ast;

import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5ArrayType;
import polyglot.ext.jl5.types.JL5LocalInstance;
import polyglot.main.Report;
import polyglot.types.Type;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.ArrayMetaSetVariable;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.MetaSetVariable;
import tool.compiler.java.visit.XSubseteqY;

/**
 * LocalDecl <: ForInit <: Stmt <: Term <: Node	<br>
 * LocalDecl <: VarDecl <: Term <: Node
 * @author LHJ
 */
public class EquGenLocalDeclExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		LocalDecl localDecl = (LocalDecl) this.node();
		Report.report(2, "[Enter] Local Declaration: " + localDecl/*.name()*/);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		LocalDecl localDecl = (LocalDecl) this.node();
		Type localDeclType = localDecl.type().type();
		JL5LocalInstance localIns = (JL5LocalInstance) localDecl.localInstance();
		Report.report(2, "[Leave] Local Declaration: " + localDecl/*.name()*/);
		
		// C x = e || C[] x = e;
		//   1. x : C{Chi1} || C[]{Chi1(base, elem)}을 현재 env에 추가 (X1은 새로운 변수)
		MetaSetVariable cchi1;	// C 또는 C[]에 대한 MetaSetVariable
		if(localDeclType instanceof JL5ArrayType) {
			// C[] x: C[]{Chi1(base, elem)}
			cchi1 = new ArrayMetaSetVariable((JL5ArrayType) localDeclType);
		} else {
			// C x: C{Chi1}
			cchi1 = new MetaSetVariable(localDeclType);
		}
		v.getLocalEnv().add(localIns, cchi1);
		
		//   2. e가 있는지 확인 후 e의 타입 D{Chi2}를 가져오고 D{Chi2} <: C{Chi1}을 제약식 집합에 추가 
		if(localDecl.init() != null) {
			MetaSetVariable dchi2 = EquGenExt.MetaSetVar(localDecl.init());
			XSubseteqY xy = new XSubseteqY(dchi2, cchi1);
			v.getCurrMC().addMetaConstraint(xy);
			Report.report(3, "\t[XSubseteqY] " + xy);
			
		//   3. 배열 변수인 경우, Top Level 아래의 MetaSetVariable의 데이터 플로우
			if(localDeclType instanceof JL5ArrayType) {
				ArrayMetaSetVariable cichi;			// C{Chi1}의 하위 레벨 base Ci{Chi}
				ArrayMetaSetVariable dichi;			// D{Chi2}의 하위 레벨 base Di{Chi}
				MetaSetVariable cichi_base = cchi1;
				MetaSetVariable dichi_base = dchi2;
				do {
					// 현재 레벨 갱신
					cichi = (ArrayMetaSetVariable)cichi_base;	// 현재 Ci{Chi} 갱신
					dichi = (ArrayMetaSetVariable)dichi_base;	// 현재 Di{Chi} 갱신
					
					// (Di{Chi}의 int{Chi}) <: (Ci{Chi}의 int{Chi}) 제약식 추가 (각각의 length)
					xy = new XSubseteqY(dichi.length(), cichi.length());
					v.getCurrMC().addMetaConstraint(xy);
					Report.report(3, "\t[XSubseteqY] " + xy);
					
					// 각각의 base를 가져옴
					cichi_base = cichi.base();	// Ci{Chi}의 base
					dichi_base = dichi.base();	// Di{Chi}의 base
					
					// Di_base{Chi} <: Ci_base{Chi} 제약식 추가
					xy = new XSubseteqY(dichi_base, cichi_base);
					v.getCurrMC().addMetaConstraint(xy);
					Report.report(3, "\t[XSubseteqY] " + xy);
				} while(cichi_base instanceof ArrayMetaSetVariable && 
						dichi_base instanceof ArrayMetaSetVariable);
				// TODO: cchi1_base와 dchi2_base 둘 중 하나만 
				// ArrayMetaSetVariable가 되는 경우가 발생하는가
			}
		}
		
		//   4. 리턴할 MataSetVar는 없음
		
		return super.equGenLeave(v);
	}
}