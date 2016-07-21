package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ext.jl7.ast.TryWithResources;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * TryWithResources <: Try <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenTryWithResourcesExt extends EquGenTryExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		TryWithResources tryRes = (TryWithResources)this.node();
		Report.report(2, "[Enter] Try with Resources: " + tryRes);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		TryWithResources tryRes = (TryWithResources)this.node();
		Report.report(2, "[Leave] Try with Resources: " + tryRes);
		
		return super.equGenLeave(v);
	}
}