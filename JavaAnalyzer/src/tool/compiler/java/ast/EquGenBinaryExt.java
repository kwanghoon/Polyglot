package tool.compiler.java.ast;

import polyglot.ast.Binary;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * Binary <: Expr <: Term <: Node				<br>
 * Binary <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenBinaryExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();

	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Binary binary = (Binary) this.node();
		Report.report(0, "Binary: " + binary);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		return super.equGenLeave(v);
	}
}