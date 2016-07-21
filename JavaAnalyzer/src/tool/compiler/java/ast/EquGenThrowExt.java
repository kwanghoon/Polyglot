package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ast.Throw;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * Throw <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenThrowExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Throw throwStmt = (Throw)this.node();
		Report.report(2, "[Enter] Throw: " + throwStmt);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Throw throwStmt = (Throw)this.node();
		Report.report(2, "[Leave] Throw: " + throwStmt);
		
		return super.equGenLeave(v);
	}
}