package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * TryWithResources <: Try <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenTryWithResourcesExt extends EquGenTryExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Try with Resources";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
//		ReportUtil.enterReport(this);
//		TryWithResources tryRes = (TryWithResources)this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
//		ReportUtil.leaveReport(this);
//		TryWithResources tryRes = (TryWithResources)this.node();
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}