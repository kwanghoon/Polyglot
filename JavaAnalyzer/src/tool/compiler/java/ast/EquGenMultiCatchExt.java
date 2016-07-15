package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ext.jl7.ast.MultiCatch;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * MultiCatch <: Catch <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenMultiCatchExt extends EquGenCatchExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		MultiCatch multiCatch = (MultiCatch)this.node();
		Report.report(2, "[Enter] Multi-Catch: " + multiCatch);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		MultiCatch multiCatch = (MultiCatch)this.node();
		
		Report.report(2, "[Leave] Multi-Catch: " + multiCatch);
		return super.equGenLeave(v);
	}
}