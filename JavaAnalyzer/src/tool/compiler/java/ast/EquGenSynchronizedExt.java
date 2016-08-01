package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.visit.EquGenerator;

/**
 * Synchronized <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenSynchronizedExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Synchronized";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		Synchronized synchronizedStmt = (Synchronized)this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
//		Synchronized synchronizedStmt = (Synchronized)this.node();
		
		setLocalEnv(v.getTypeEnv().getCurrEnv());
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}