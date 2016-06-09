package tool.compiler.java.ast;

import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * Expr <: Term <: Node					<br>
 * Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenExprExt extends EquGenExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
//		Expr expr = (Expr)this.node();
//		Report.report(0, "[Enter] Expression: " + expr);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
//		Expr expr = (Expr)this.node();
//		Report.report(0, "[Leave] Expression: " + expr);
		
		return super.equGenLeave(v);
	}
}