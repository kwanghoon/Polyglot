package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.env.LocalEnvironment;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.visit.EquGenerator;

/**
 * Block <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenBlockExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Block";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		Block block = (Block)this.node();
		
		// 로컬 환경 구성
		v.getTypeEnv().push();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
//		Block block = (Block)this.node();
		
		// 로컬 환경 해제
		LocalEnvironment typeEnv = v.getTypeEnv().pop();
		setLocalEnv(typeEnv);
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}