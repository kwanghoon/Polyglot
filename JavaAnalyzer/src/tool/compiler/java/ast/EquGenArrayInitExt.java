package tool.compiler.java.ast;

import polyglot.ast.ArrayInit;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * ArrayInit <: Expr <: Term <: Node				<br>
 * ArrayInit <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenArrayInitExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ArrayInit arrInit = (ArrayInit) this.node();
		Report.report(2, "[Enter] Array Initialization: " + arrInit + " (type: " + arrInit.type() + ")");
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ArrayInit arrInit = (ArrayInit) this.node();
		Report.report(2, "[Leave] Array Initialization: " + arrInit);
		
		return super.equGenLeave(v);
	}
}