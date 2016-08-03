package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ast.Return;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.constraint.XSubseteqY;
import tool.compiler.java.env.MethodConstraint;
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
		
		MetaSetVariable tchi = EquGenExprExt.metaSetVar(returnStmt.expr());
		ReportUtil.report(tchi, MetaSetVarSource.SubExpression, MetaSetVarGoal.Flow);
		
		MetaSetVariable ret_chi = ((MethodConstraint) v.getCurrCF()).getReturn();
		ReportUtil.report(tchi, MetaSetVarSource.Environment, MetaSetVarGoal.Flow);
		
		XSubseteqY xy = new XSubseteqY(tchi, ret_chi);
		v.getCurrCF().addMetaConstraint(xy);
		ReportUtil.report(xy);
		
		setLocalEnv(v.peekTypeEnv().getCurrEnv());
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}