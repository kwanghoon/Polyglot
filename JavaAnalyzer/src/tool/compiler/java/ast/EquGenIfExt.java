package tool.compiler.java.ast;

import polyglot.ast.If;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * If <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenIfExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		If ifStmt = (If)this.node();
		Report.report(2, "[Enter] If: " + ifStmt);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		If ifStmt = (If)this.node();
		
		Report.report(2, "[Leave] If: " + ifStmt);
		return super.equGenLeave(v);
	}
}