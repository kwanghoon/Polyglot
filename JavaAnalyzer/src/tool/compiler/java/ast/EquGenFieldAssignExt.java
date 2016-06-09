package tool.compiler.java.ast;

import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.FieldAssign;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5FieldInstance;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.AssignField;
import tool.compiler.java.visit.AssignStaticField;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.MetaSetVariable;

/**
 * FieldAssign <: Assign <: Expr <: Term <: Node				<br>
 * FieldAssign <: Assign <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenFieldAssignExt extends EquGenAssignExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		FieldAssign fasgn = (FieldAssign) this.node();
		Report.report(0, "[Enter] Field Assign: " + fasgn);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		FieldAssign fasgn = (FieldAssign) this.node();
		Field lVal = fasgn.left();
		Expr rVal = fasgn.right();
		JL5FieldInstance fldIns = (JL5FieldInstance) fasgn.left().fieldInstance();
//		Report.report(0, "[Leave] Field Assign: " + fasgn);
		
		// e1.f = e2 / C.f = e2
		//   1. e2의 타입 D{Chi2}를 가져오고
		MetaSetVariable dchi2 = EquGenExt.MetaSetVar(rVal);
		
		//   2-1. e1의 타입 C{Chi1}을 가져오고, D{Chi2} <: C{Chi1}.f 제약식을 추가 (non-static)
		if(!fldIns.flags().isStatic()) {
			MetaSetVariable cchi1 = EquGenExt.MetaSetVar(lVal.target());
			AssignField af = new AssignField(dchi2, cchi1, fldIns);
			v.getCurrMC().addMetaConstraint(af);
			Report.report(1, "[Leave] Field Assign: " + fasgn + "\n\t[AssignField] " + af);
		}
		
		//   2-2. D{Chi2} <: C.f 제약식을 추가 (static)
		else {
			AssignStaticField asf = new AssignStaticField(dchi2, fldIns);
			v.getCurrMC().addMetaConstraint(asf);
			Report.report(1, "[Leave] Field Assign: " + fasgn + "\n\t[AssignStaticField] " + asf);
		}
		
		//   4. e1.f의 타입을 리턴할 타입으로 지정
		setMetaSetVar(EquGenExt.MetaSetVar(lVal));
		
		return super.equGenLeave(v);
	}
}