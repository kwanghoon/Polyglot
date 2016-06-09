package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ast.Unary;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * Unary <: Expr <: Term <: Node				<br>
 * Unary <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenUnaryExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();

	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Unary unary = (Unary) this.node();
		Report.report(0, "[Enter] Unary: " + unary);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Unary unary = (Unary) this.node();
		Report.report(0, "[Leave] Unary: " + unary);
		
		return super.equGenLeave(v);
	}
}