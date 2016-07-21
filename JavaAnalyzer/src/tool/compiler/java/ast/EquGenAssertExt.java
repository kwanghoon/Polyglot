package tool.compiler.java.ast;

import polyglot.ast.Assert;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * Assert <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenAssertExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Assert assertStmt = (Assert)this.node();
		Report.report(2, "[Enter] Assert: " + assertStmt);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Assert assertStmt = (Assert)this.node();
		Report.report(2, "[Leave] Assert: " + assertStmt);
		
		return super.equGenLeave(v);
	}
}