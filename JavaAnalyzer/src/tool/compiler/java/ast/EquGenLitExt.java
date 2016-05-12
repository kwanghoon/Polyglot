package tool.compiler.java.ast;

import polyglot.ast.Lit;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.TypedSetVariable;

/**
 * Lit <: Expr <: Term <: Node					<br>
 * Lit <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenLitExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();

	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
//		Lit lit = (Lit) this.node();
//		Report.report(0, "[Enter] Lit: " + lit);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Lit lit = (Lit) this.node();
		Report.report(0, "[Leave] Lit: " + lit);
		
		setTypedSetVar(new TypedSetVariable(lit.type()));
		
		return super.equGenLeave(v);
	}
}