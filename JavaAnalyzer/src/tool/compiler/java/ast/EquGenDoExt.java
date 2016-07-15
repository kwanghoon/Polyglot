package tool.compiler.java.ast;

import polyglot.ast.Do;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * Do <: Loop <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenDoExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Do doLoop = (Do)this.node();
		Report.report(2, "[Enter] Do-While: " + doLoop);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Do doLoop = (Do)this.node();
		
		Report.report(2, "[Leave] Do-While: " + doLoop);
		return super.equGenLeave(v);
	}
}