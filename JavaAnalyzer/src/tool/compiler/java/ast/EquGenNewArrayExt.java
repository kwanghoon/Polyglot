package tool.compiler.java.ast;

import polyglot.ast.NewArray;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * NewArray <: Expr <: Term <: Node					<br>
 * NewArray <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenNewArrayExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		NewArray nwArr = (NewArray) this.node();
		Report.report(0, "[Enter] New Array: " + nwArr);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		NewArray nwArr = (NewArray) this.node();
		Report.report(0, "[Leave] New Array: " + nwArr);
		
		return super.equGenLeave(v);
	}
}