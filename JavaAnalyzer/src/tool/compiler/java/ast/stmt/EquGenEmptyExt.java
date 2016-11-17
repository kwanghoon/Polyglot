package tool.compiler.java.ast.stmt;

import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.visit.EquGenerator;

/**
 * Empty <: Stmt <: Term <: Node
 * @author LHJ
 */
@Deprecated
public class EquGenEmptyExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Empty";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		Empty empty = (Empty) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
//		Empty empty = (Empty) this.node();
		
		setLocalEnv(v.peekTypeEnv().getCurrEnv());
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}