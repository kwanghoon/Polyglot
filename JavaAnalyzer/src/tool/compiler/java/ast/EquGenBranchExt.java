package tool.compiler.java.ast;

import polyglot.ast.Branch;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * Branch <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenBranchExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Branch branch = (Branch)this.node();
		Report.report(2, "[Enter] Branch: " + branch);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Branch branch = (Branch)this.node();
		
		Report.report(2, "[Leave] Branch: " + branch);
		return super.equGenLeave(v);
	}
}