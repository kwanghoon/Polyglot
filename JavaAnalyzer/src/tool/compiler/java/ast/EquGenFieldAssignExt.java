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
		Report.report(2, "[Enter] Field Assign: " + fasgn);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		FieldAssign fasgn = (FieldAssign) this.node();
		Field lVal = fasgn.left();
		Expr rVal = fasgn.right();
		JL5FieldInstance fldIns = (JL5FieldInstance) lVal.fieldInstance();
		Report.report(2, "[Leave] Field Assign: " + fasgn);
		
		// e1.f = e2 / C.f = e2
		//   1. e2의 타입 E{Chi3}를 가져오고
		MetaSetVariable echi3 = EquGenExt.metaSetVar(rVal);
		
		//   2A. e1.f = e2	(non-static)
		//       e1의 타입 C{Chi1}을 가져오고, E{Chi3} <: C{Chi1}.f 제약식을 추가
		if(!fldIns.flags().isStatic()) {
			MetaSetVariable cchi1 = EquGenExt.metaSetVar(lVal.target());
			AssignField af = new AssignField(echi3, cchi1, fldIns);
			v.getCurrMC().addMetaConstraint(af);
			Report.report(3, "\t[AssignField] " + af);
		}
		
		//   2B. C.f = e2	(static)
		//       E{Chi3} <: C.f 제약식을 추가
		else {
			AssignStaticField asf = new AssignStaticField(echi3, fldIns);
			v.getCurrMC().addMetaConstraint(asf);
			Report.report(3, "\t[AssignStaticField] " + asf);
		}
		
		//   3. e1.f의 타입 D{Chi2}를 리턴할 타입으로 지정
		MetaSetVariable dchi2 = EquGenExt.metaSetVar(lVal);
		setMetaSetVar(dchi2);
		Report.report(3, "\t[MetaSetVariable] " + dchi2 + " (For return: From L-Value)");
		
		return super.equGenLeave(v);
	}
}