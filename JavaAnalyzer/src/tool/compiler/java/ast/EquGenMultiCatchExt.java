package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * MultiCatch <: Catch <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenMultiCatchExt extends EquGenCatchExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Multi-Catch";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
//		ReportUtil.enterReport(this);
//		MultiCatch multiCatch = (MultiCatch)this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
//		ReportUtil.leaveReport(this);
//		MultiCatch multiCatch = (MultiCatch)this.node();
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}