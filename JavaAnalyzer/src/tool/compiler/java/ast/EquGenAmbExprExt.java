package tool.compiler.java.ast;

import polyglot.ast.AmbExpr;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * AmbExpr <: Expr <: Term <: Node					<br>
 * AmbExpr <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenAmbExprExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		AmbExpr aexpr = (AmbExpr) this.node();
		Report.report(0, "Ambiguous Expression: " + aexpr);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		return super.equGenLeave(v);
	}
}