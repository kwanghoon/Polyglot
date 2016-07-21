package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ast.Try;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * Try <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenTryExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Try tryStmt = (Try)this.node();
		Report.report(2, "[Enter] Try: " + tryStmt);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Try tryStmt = (Try)this.node();
		Report.report(2, "[Leave] Try: " + tryStmt);
		
		return super.equGenLeave(v);
	}
}