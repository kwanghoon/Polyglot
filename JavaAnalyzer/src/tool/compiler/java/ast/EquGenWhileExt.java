package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ast.While;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * While <: Loop <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenWhileExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		While whileLoop = (While)this.node();
		Report.report(2, "[Enter] While: " + whileLoop);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		While whileLoop = (While)this.node();
		
		Report.report(2, "[Leave] While: " + whileLoop);
		return super.equGenLeave(v);
	}
}