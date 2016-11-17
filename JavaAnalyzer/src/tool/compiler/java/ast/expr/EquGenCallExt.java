package tool.compiler.java.ast.expr;

import polyglot.ast.Call;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5MethodInstance;
import polyglot.ext.jl5.types.JL5ProcedureInstance;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.constraint.InvokeMth;
import tool.compiler.java.constraint.InvokeStaticMth;
import tool.compiler.java.effect.ActivityEffectVariable;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.info.MethodCallInfo;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;
import tool.compiler.java.visit.EquGenerator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Call <: Expr <: Term <: Node					<br>
 * Call <: Expr <: Receiver <: Prefix <: Node	<br>
 * Call <: ProcedureCall <: Term <: Node
 * @author LHJ
 */
public class EquGenCallExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Call";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
		Call call = (Call) this.node();
		
		// (호출) 메서드 인포 생성
		MethodCallInfo mtdInfo = new MethodCallInfo((JL5ProcedureInstance) call.procedureInstance());
		v.addToSet(mtdInfo);
		ReportUtil.report(mtdInfo);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		Call call = (Call) this.node();
		JL5MethodInstance mthIns = (JL5MethodInstance) call.methodInstance();
		
		// e.m(e1, ..., en) / C.m(e1, ..., en)
		//   1. 리턴할 타입 D{Chi}를 만든 다음, (Chi는 새로 만들고 D는 이 노드 자신의 타입)
		MetaSetVariable dchi = MetaSetVariable.create(call.type());
//		MetaSetVariable dchi = new MetaSetVariable(call.type());
		ReportUtil.report(dchi, MetaSetVarSource.New, MetaSetVarGoal.Return);
		
		//   2. e1~en의 타입 Ci{Chii}를 가져온다.
		ArrayList<MetaSetVariable> cschis = new ArrayList<>();
		MetaSetVariable cichii;
		for(Expr ei: call.arguments()) {
			cichii = EquGenExprExt.metaSetVar(ei);
			cschis.add(cichii);
			ReportUtil.report(cichii, MetaSetVarSource.Argument, MetaSetVarGoal.Flow);
		}
		
		//   3A. e.m(e1, ..., en)	(dynamic)
		//       e의 타입 C{Chi0}를 가져오고, C{Chi0}.m <: (C1{Chi1}, ... , Cn{Chin}) -> D{Chi} 제약식을 추가
		if(!mthIns.flags().isStatic()) {
			MetaSetVariable cchi0 = EquGenExprExt.metaSetVar((Expr) call.target());
			InvokeMth im = new InvokeMth(cchi0, mthIns, cschis, dchi);
			v.getCurrCF().addMetaConstraint(im);
			ReportUtil.report(im);
		}
		
		//   3B. C.m(e1, ..., en)	(static)
		//       C.m <: (C1{Chi1}, ... , Cn{Chin}) -> D{Chi} 제약식을 추가
		else {
			InvokeStaticMth ism = new InvokeStaticMth(mthIns, cschis, dchi);
			v.getCurrCF().addMetaConstraint(ism);
			ReportUtil.report(ism);
		}
		
		//   4. D{Chi}를 리턴 타입으로 지정
		setMetaSetVar(dchi);
		
		//   5. e1, ... , en를 분석해서 나오는 effects(exn, activity)인 X_eff1, ... , X_effn를 찾고,
		final LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> x_effs = new LinkedHashMap<>();
		EquGenExprExt.effects(call.arguments(), x_effs, EffectSetVarSource.SubExpression);
		
		//   6. startActivity()류의 메서드인 경우, 인텐트에 대한 Activity Effect를 만들고, 
		ActivityEffectVariable activityEffect = ActivityEffectVariable.create(call);
		if (activityEffect != null) {
			Map<EffectSetVariable, EffectSetVarSource> argEffect = x_effs.get(EffectName.ActivityEff);
			if (argEffect == null) {
				argEffect = new LinkedHashMap<>();
				x_effs.put(EffectName.ActivityEff, argEffect);
			}
			argEffect.put(activityEffect, EffectSetVarSource.New);
		}
		
		//   7. X_eff1 ∪ ... ∪ X_effn를 구하고, 이를 리턴할 effects(exn, activity)로 지정
		setEffects(x_effs);
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}