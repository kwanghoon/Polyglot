package tool.compiler.java.ast;

import polyglot.ast.ArrayAccessAssign;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * ArrayAccessAssign <: Assign <: Expr <: Term <: Node				<br>
 * ArrayAccessAssign <: Assign <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenArrayAccessAssignExt extends EquGenAssignExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ArrayAccessAssign aasgn = (ArrayAccessAssign) this.node();
		Report.report(2, "[Enter] Array Access Assign: " + aasgn);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ArrayAccessAssign aasgn = (ArrayAccessAssign) this.node();
		
		Report.report(2, "[Leave] Array Access Assign: " + aasgn);
		return super.equGenLeave(v);
	}
}