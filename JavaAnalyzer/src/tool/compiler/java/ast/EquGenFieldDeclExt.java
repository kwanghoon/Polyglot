package tool.compiler.java.ast;

import polyglot.ast.FieldDecl;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5FieldInstance;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.AssignField;
import tool.compiler.java.visit.AssignStaticField;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.FieldInfo;
import tool.compiler.java.visit.MetaSetVariable;

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
//		Report.report(0, "[Enter] Field Declaration: " + fldDecl/*.name()*/);
		
		FieldInfo fldInfo = new FieldInfo((JL5FieldInstance) fldDecl.fieldInstance());
		v.addToSet(fldInfo);
		Report.report(0, "[Enter] Field Declaration: " + fldDecl + "\n\t[FieldInfo] " + fldInfo);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		FieldDecl fldDecl = (FieldDecl) this.node();
		JL5FieldInstance fldIns = (JL5FieldInstance) fldDecl.fieldInstance();
//		Report.report(0, "[Leave] Field Declaration: " + fldDecl/*.name()*/);
		
		// 필드 선언시 초기화 하는 경우
		if(fldDecl.init() != null) {
			// e1.f = e2 / C.f = e2
			//   1. e2의 타입 D{Chi2}를 가져오고
			MetaSetVariable dchi2 = EquGenExt.MetaSetVar(fldDecl.init());
			
			//   2-1. e1의 타입 C{Chi1}을 가져오고, D{Chi2} <: C{Chi1}.f 제약식을 추가 (non-static)
			if(!fldIns.flags().isStatic()) {
				MetaSetVariable cchi1 = v.getCurrCC().getChiThis();
				AssignField af = new AssignField(dchi2, cchi1, fldIns);
				v.getCurrCC().addMetaConstraint(af);
				Report.report(1, "[Leave] Field Declaration: " + fldDecl + "\n\t[AssignField] " + af);
			}
			
			//   2-2. D{Chi2} <: C.f 제약식을 추가 (static)
			else {
				AssignStaticField asf = new AssignStaticField(dchi2, fldIns);
				v.getCurrCC().addMetaConstraint(asf);
				Report.report(1, "[Leave] Field Declaration: " + fldDecl + "\n\t[AssignStaticField] " + asf);
			}
		}
		
		return super.equGenLeave(v);
	}
}