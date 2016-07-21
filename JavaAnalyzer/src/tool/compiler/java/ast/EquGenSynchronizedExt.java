package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ast.Synchronized;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * Synchronized <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenSynchronizedExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Synchronized synchronizedStmt = (Synchronized)this.node();
		Report.report(2, "[Enter] Synchronized: " + synchronizedStmt);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Synchronized synchronizedStmt = (Synchronized)this.node();
		Report.report(2, "[Leave] Synchronized: " + synchronizedStmt);
		
		return super.equGenLeave(v);
	}
}