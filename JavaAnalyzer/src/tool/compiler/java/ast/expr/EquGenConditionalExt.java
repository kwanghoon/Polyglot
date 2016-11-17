package tool.compiler.java.ast.expr;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import polyglot.ast.Conditional;
import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.ArrayMetaSetVariable;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.constraint.XSubseteqY;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.util.EquGenUtil;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
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
		
		// e0 ? e1 : e2
		//   1. 리턴할 타입 T{Chi}를 만든 다음, (Chi는 새로 만들고 T는 이 노드 자신의 타입)
		MetaSetVariable tchi = MetaSetVariable.create(cond.type());
//		MetaSetVariable tchi = new MetaSetVariable(cond.type());
		ReportUtil.report(tchi, MetaSetVarSource.New, MetaSetVarGoal.Return);
		
		//   2. e1의 타입 T1{Chi1}와 e2의 타입 T2{Chi2}를 가져온다.
		MetaSetVariable t1chi1 = metaSetVar(cond.consequent());
		MetaSetVariable t2chi2 = metaSetVar(cond.alternative());
		ReportUtil.report(t1chi1, MetaSetVarSource.SubExpression, MetaSetVarGoal.Flow);
		ReportUtil.report(t2chi2, MetaSetVarSource.SubExpression, MetaSetVarGoal.Flow);
		
		//   3-1. T1{Chi1} <: T{Chi}와 T2{Chi2} <: T{Chi} 제약식을 추가
		XSubseteqY xy1 = new XSubseteqY(t1chi1, tchi);
		XSubseteqY xy2 = new XSubseteqY(t2chi2, tchi);
		v.getCurrCF().addMetaConstraint(xy1);
		v.getCurrCF().addMetaConstraint(xy2);
		ReportUtil.report(xy1);
		ReportUtil.report(xy2);
		
		//   3-2. 배열인 경우, T1{Chi1} <: T{Chi}와 T2{Chi2} <: T{Chi}의 하위 레벨 제약식을 추가
		//         (Top Level 아래의 MetaSetVariable(s)의 데이터 플로우)
		if(EquGenUtil.isArray(tchi)) {
			Collection<XSubseteqY> xys1 = XSubseteqY.constrain(
					 t1chi1, (ArrayMetaSetVariable) tchi);
			Collection<XSubseteqY> xys2 = XSubseteqY.constrain(
					t2chi2, (ArrayMetaSetVariable) tchi);
			v.getCurrCF().addMetaConstraints(xys1);
			v.getCurrCF().addMetaConstraints(xys2);
		}
		
		//   4. T{Chi}를 리턴 타입으로 지정
		setMetaSetVar(tchi);
		
		//   5. e0을 분석하면 나오는 exn effect인 X_eff0를 가져오고, 
		final LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> x_effs = new LinkedHashMap<>();
		EquGenExprExt.effects(cond.cond(), x_effs, EffectSetVarSource.SubExpression);
		
		//   6. e1을 분석하면 나오는 exn effect인 X_eff1를 가져온 다음, 
		EquGenExprExt.effects(cond.consequent(), x_effs, EffectSetVarSource.SubExpression);
		
		//   7. e2를 분석하면 나오는 exn effect인 X_eff2를 가져와, 
		EquGenExprExt.effects(cond.alternative(), x_effs, EffectSetVarSource.SubExpression);
		
		//   8. X_eff0 ∪ X_eff1 ∪ X_eff2를 구하고, 이를 리턴할 exn effect로 지정
		setEffects(x_effs);
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}