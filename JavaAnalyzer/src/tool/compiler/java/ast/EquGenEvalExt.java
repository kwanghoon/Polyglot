package tool.compiler.java.ast;

import polyglot.ast.Eval;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * Eval <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenEvalExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Eval eval = (Eval)this.node();
		Report.report(2, "[Enter] Evaluation: " + eval);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Eval eval = (Eval)this.node();
		
		Report.report(2, "[Leave] Evaluation: " + eval);
		return super.equGenLeave(v);
	}
}