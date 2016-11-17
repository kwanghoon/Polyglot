package tool.compiler.java.ast.stmt;

import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.visit.EquGenerator;

/**
 * Branch <: Stmt <: Term <: Node
 * @author LHJ
 */
@Deprecated
public class EquGenBranchExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Branch";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		Branch branch = (Branch) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
//		Branch branch = (Branch) this.node();
		
		// TODO: environment의 합집합을 구하는 동작이 필요 없음?
		setLocalEnv(v.peekTypeEnv().getCurrEnv());
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}