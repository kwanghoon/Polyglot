package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ast.Return;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * Return <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenReturnExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Return";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		Return returnStmt = (Return)this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		Return returnStmt = (Return)this.node();
		
		// TODO: 제대로 했는지 확인 필요
		MetaSetVariable tchi = EquGenExprExt.metaSetVar(returnStmt.expr());
		v.getCurrMC().setReturn(tchi);
		ReportUtil.report(tchi, MetaSetVarSource.SubExpression, MetaSetVarGoal.Environment);
		
		setLocalEnv(v.getTypeEnv().getCurrEnv());
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}