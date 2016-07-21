package tool.compiler.java.ast;

import polyglot.ast.Labeled;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * Labeled <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenLabeledExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Labeled labeled = (Labeled)this.node();
		Report.report(2, "[Enter] Labeled: " + labeled);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Labeled labeled = (Labeled)this.node();
		Report.report(2, "[Leave] Labeled: " + labeled);
		
		return super.equGenLeave(v);
	}
}