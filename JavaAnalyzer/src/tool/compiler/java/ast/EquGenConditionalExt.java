package tool.compiler.java.ast;

import polyglot.ast.Conditional;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * Conditional <: Expr <: Term <: Node					<br>
 * Conditional <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenConditionalExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Conditional cond = (Conditional) this.node();
		Report.report(0, "[Enter] Conditional: " + cond);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Conditional cond = (Conditional) this.node();
		Report.report(0, "[Leave] Conditional: " + cond);
		
		return super.equGenLeave(v);
	}
}