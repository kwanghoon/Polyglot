package tool.compiler.java.ast;

import polyglot.ast.FieldDecl;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5ArrayType;
import polyglot.ext.jl5.types.JL5FieldInstance;
import polyglot.main.Report;
import polyglot.types.Type;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.ArrayMetaSetVariable;
import tool.compiler.java.visit.AssignField;
import tool.compiler.java.visit.AssignStaticField;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.FieldInfo;
import tool.compiler.java.visit.MetaSetVariable;
import tool.compiler.java.visit.XSubseteqY;

/**
 * FieldDecl <: ClassMember <: Term <: Node	<br>
 * FieldDecl <: CodeNode <: Term <: Node
 * @author LHJ
 */
public class EquGenFieldDeclExt extends EquGenExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		FieldDecl fldDecl = (FieldDecl) this.node();
		Report.report(2, "[Enter] Field Declaration: " + fldDecl/*.name()*/);
		
		FieldInfo fldInfo = new FieldInfo((JL5FieldInstance) fldDecl.fieldInstance());
		v.addToSet(fldInfo);
		
		Report.report(3, "\t[FieldInfo] " + fldInfo);
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		FieldDecl fldDecl = (FieldDecl) this.node();
		Type fldDeclType = fldDecl.type().type();
		JL5FieldInstance fldIns = (JL5FieldInstance) fldDecl.fieldInstance();
		Report.report(2, "[Leave] Field Declaration: " + fldDecl/*.name()*/);
		
		// 필드 선언시 초기화 하는 경우에 한해
		if(fldDecl.init() != null) {
			// D f = e2 / static D f = e2
			//   1. e2의 타입 E{Chi3}를 가져오고
			MetaSetVariable echi3 = EquGenExt.MetaSetVar(fldDecl.init());
			
			//   2. 스칼라 변수 또는 배열 변수의 Top Level의 MetaSetVariable의 데이터 플로우
			//   2-1. e1(this)의 타입 C{Chi1}을 가져오고, E{Chi3} <: C{Chi1}.f 제약식을 추가 (non-static)
			if(!fldIns.flags().isStatic()) {
				MetaSetVariable cchi1 = v.getCurrCC().getThis();
				AssignField af = new AssignField(echi3, cchi1, fldIns);
				v.getCurrCC().addMetaConstraint(af);
				Report.report(3, "\t[AssignField] " + af);
			}
			
			//   2-2. E{Chi3} <: C.f 제약식을 추가 (static)
			else {
				AssignStaticField asf = new AssignStaticField(echi3, fldIns);
				v.getCurrCC().addMetaConstraint(asf);
				Report.report(3, "\t[AssignStaticField] " + asf);
			}
			
			//   3. 배열 변수인 경우, Top Level 아래의 MetaSetVariable의 데이터 플로우
			if(fldDeclType instanceof JL5ArrayType) {
				XSubseteqY xy;
				ArrayMetaSetVariable dichi;			// D{Chi2}의 하위 레벨 base Di{Chi}
				ArrayMetaSetVariable eichi;			// E{Chi3}의 하위 레벨 base Ei{Chi}
				MetaSetVariable dichi_base = v.getCurrCC().getField(fldIns);	// D[]에 대한 MetaSetVariable
				MetaSetVariable eichi_base = echi3;
				do {
					// 현재 레벨 갱신
					dichi = (ArrayMetaSetVariable)dichi_base;	// 현재 Di{Chi} 갱신
					eichi = (ArrayMetaSetVariable)eichi_base;	// 현재 Ei{Chi} 갱신
					
					// (Ei{Chi}의 int{Chi}) <: (Di{Chi}의 int{Chi}) 제약식 추가 (각각의 length)
					xy = new XSubseteqY(eichi.length(), dichi.length());
					v.getCurrCC().addMetaConstraint(xy);
					Report.report(3, "\t[XSubseteqY] " + xy);
					
					// 각각의 base를 가져옴
					dichi_base = dichi.base();	// Di{Chi}의 base
					eichi_base = eichi.base();	// Ei{Chi}의 base
					
					// Ei_base{Chi} <: Di_base{Chi} 제약식 추가
					xy = new XSubseteqY(eichi_base, dichi_base);
					v.getCurrCC().addMetaConstraint(xy);
					Report.report(3, "\t[XSubseteqY] " + xy);
				} while(dichi_base instanceof ArrayMetaSetVariable && 
						eichi_base instanceof ArrayMetaSetVariable);
				// TODO: cchi1_base와 dchi2_base 둘 중 하나만 
				// ArrayMetaSetVariable가 되는 경우가 발생하는가
			}
		}
		
		return super.equGenLeave(v);
	}
}