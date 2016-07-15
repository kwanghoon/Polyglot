package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ast.Case;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * Case <: SwitchElement <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenCaseExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Case caseStmt = (Case)this.node();
		Report.report(2, "[Enter] Case: " + caseStmt);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Case caseStmt = (Case)this.node();
		
		Report.report(2, "[Leave] Case: " + caseStmt);
		return super.equGenLeave(v);
	}
}