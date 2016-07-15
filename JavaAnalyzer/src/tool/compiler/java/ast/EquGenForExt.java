package tool.compiler.java.ast;

import polyglot.ast.For;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * For <: Loop <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenForExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		For forLoop = (For)this.node();
		Report.report(2, "[Enter] For: " + forLoop);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		For forLoop = (For)this.node();
		
		Report.report(2, "[Leave] For: " + forLoop);
		return super.equGenLeave(v);
	}
}