package tool.compiler.java.ast;

import polyglot.ast.Assign;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * Assign <: Expr <: Term <: Node				<br>
 * Assign <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenAssignExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
//		Assign asgn = (Assign) this.node();
//		Report.report(0, "[Enter] Assign: " + asgn);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
//		Assign asgn = (Assign) this.node();
//		Report.report(0, "[Leave] Assign: " + asgn);
		
		return super.equGenLeave(v);
	}
}