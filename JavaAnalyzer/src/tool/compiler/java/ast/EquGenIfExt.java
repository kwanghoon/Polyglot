package tool.compiler.java.ast;

import polyglot.ast.If;
import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.env.LocalEnvironment;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.visit.EquGenerator;

/**
 * If <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenIfExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "If";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		If ifStmt = (If)this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		If ifStmt = (If)this.node();
		
		// if (condition) {consequent} else {alternative}
		LocalEnvironment cons = EquGenStmtExt.localEnv(ifStmt.consequent());
		LocalEnvironment altr = EquGenStmtExt.localEnv(ifStmt.alternative());
		
		// TODO: environment의 합집합을 구하는 동작이 필요 없음?
		setLocalEnv(v.getTypeEnv().getCurrEnv());
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}