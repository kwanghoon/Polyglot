package tool.compiler.java.ast;

import polyglot.ast.Catch;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * Catch <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenCatchExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Catch catchStmt = (Catch)this.node();
		Report.report(2, "[Enter] Catch: " + catchStmt);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Catch catchStmt = (Catch)this.node();
		
		Report.report(2, "[Leave] Catch: " + catchStmt);
		return super.equGenLeave(v);
	}
}