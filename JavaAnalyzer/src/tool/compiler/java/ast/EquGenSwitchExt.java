package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ast.Switch;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * Switch <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenSwitchExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Switch switchStmt = (Switch)this.node();
		Report.report(2, "[Enter] Switch: " + switchStmt);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Switch switchStmt = (Switch)this.node();
		Report.report(2, "[Leave] Switch: " + switchStmt);
		
		return super.equGenLeave(v);
	}
}