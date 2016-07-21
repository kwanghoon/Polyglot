package tool.compiler.java.ast;

import polyglot.ast.Expr;
import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5ArrayType;
import polyglot.ext.jl5.types.JL5LocalInstance;
import polyglot.main.Report;
import polyglot.types.Type;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.util.EquGenUtil;
import tool.compiler.java.visit.ArrayMetaSetVariable;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.MetaSetVariable;
import tool.compiler.java.visit.XSubseteqY;

import java.util.Collection;

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
		
		// C x = e / C[] x = e;
		//   1. x : C{Chi1} / C[]{Chi1(base, elem)}을 현재 env에 추가 (Chi1은 새로운 변수)
		MetaSetVariable cchi1 = !EquGenUtil.isArray(localDeclType) ?	// C 또는 C[]에 대한 MetaSetVariable
				new MetaSetVariable(localDeclType) : 					// C x: C{Chi1}
				new ArrayMetaSetVariable((JL5ArrayType) localDeclType);	// C[] x: C[]{Chi1(base, elem)}
		v.getLocalEnv().add(localIns, cchi1);
		Report.report(3, "\t[MetaSetVariable] " + cchi1 + " (For Env.: New)");
		
		//   2. e가 있는지 확인 후
		Expr e = localDecl.init();
		if(e != null) {
			//   2a. e의 타입 D{Chi2}를 가져오고
			MetaSetVariable dchi2 = EquGenExt.metaSetVar(e);
			
			//   2b. D{Chi2} <: C{Chi1}을 제약식 집합에 추가
			XSubseteqY xy = new XSubseteqY(dchi2, cchi1);
			v.getCurrMC().addMetaConstraint(xy);
			Report.report(3, "\t[XSubseteqY] " + xy);
			
			//   2c. 배열 변수인 경우, Top Level 아래의 MetaSetVariable의 데이터 플로우
			if(EquGenUtil.isArray(localDeclType)) {
				Collection<XSubseteqY> xys = EquGenUtil.constrain(
						(ArrayMetaSetVariable) dchi2, 
						(ArrayMetaSetVariable) cchi1);
				v.getCurrMC().addMetaConstraints(xys);
			}
		}
		
		//   3. 리턴할 MataSetVar는 없음
		
		return super.equGenLeave(v);
	}
}