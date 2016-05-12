package tool.compiler.java.ast;

import polyglot.ast.Cast;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * Cast <: Expr <: Term <: Node					<br>
 * Cast <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenCastExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();

	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Cast cast = (Cast) this.node();
		Report.report(0, "Cast: " + cast);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		return super.equGenLeave(v);
	}
}