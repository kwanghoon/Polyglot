package tool.compiler.java.ast;

import polyglot.ast.Conditional;
import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.constraint.XSubseteqY;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * Conditional <: Expr <: Term <: Node					<br>
 * Conditional <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenConditionalExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Conditional";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		Conditional cond = (Conditional) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		Conditional cond = (Conditional) this.node();
		
		// e ? e1 : e2
		//   1. 리턴할 타입 T{Chi}를 만든 다음, (Chi는 새로 만들고 T는 이 노드 자신의 타입)
		MetaSetVariable tchi = new MetaSetVariable(cond.type());
		ReportUtil.report(tchi, MetaSetVarSource.New, MetaSetVarGoal.Return);
		
		//   2. e1의 타입 T1{Chi1}와 e2의 타입 T2{Chi2}를 가져온다.
		MetaSetVariable t1chi1 = metaSetVar(cond.consequent());
		MetaSetVariable t2chi2 = metaSetVar(cond.alternative());
		ReportUtil.report(t1chi1, MetaSetVarSource.SubExpression, MetaSetVarGoal.Flow);
		ReportUtil.report(t2chi2, MetaSetVarSource.SubExpression, MetaSetVarGoal.Flow);
		
		//   3. T1{Chi1} <: T{Chi}와 T2{Chi2} <: T{Chi} 제약식을 추가
		XSubseteqY xy1 = new XSubseteqY(t1chi1, tchi);
		XSubseteqY xy2 = new XSubseteqY(t2chi2, tchi);
		v.getCurrMC().addMetaConstraint(xy1);
		v.getCurrMC().addMetaConstraint(xy2);
		ReportUtil.report(xy1);
		ReportUtil.report(xy2);
		
		//   4. T{Chi}를 리턴 타입으로 지정
		setMetaSetVar(tchi);
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}