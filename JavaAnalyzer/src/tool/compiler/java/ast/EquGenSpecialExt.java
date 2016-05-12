package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ast.Special;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.TypedSetVariable;

/**
 * Special <: Expr <: Term <: Node					<br>
 * Special <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenSpecialExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
//		Special spc = (Special) this.node();
//		Report.report(0, "[Enter] Special: " + spc);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Special spc = (Special) this.node();
		Report.report(0, "[Leave] Special: " + spc);
		
		setTypedSetVar(new TypedSetVariable(spc.type()));
		
		return super.equGenLeave(v);
	}
}