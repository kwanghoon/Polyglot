package tool.compiler.java.ast.stmt;

import java.util.LinkedHashMap;
import java.util.Map;

import polyglot.ast.ConstructorCall;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5ProcedureInstance;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.ast.expr.EquGenExprExt;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.info.MethodCallInfo;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * ConstructorCall <: Stmt <: Term <: Node	<br>
 * ConstructorCall <: ProcedureCall <: Term <: Node
 * @author LHJ
 */
public class EquGenConstructorCallExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Constructor Call";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
		ConstructorCall ctorCall = (ConstructorCall) this.node();
		JL5ProcedureInstance porcIns = (JL5ProcedureInstance) ctorCall.procedureInstance();
		
		// (호출) 메서드 인포 생성
		MethodCallInfo mtdInfo = new MethodCallInfo(porcIns);
		v.addToSet(mtdInfo);
		ReportUtil.report(mtdInfo);
		
//		ArrayList<TypedSetVariable> argSetVars = new ArrayList<>();
//		for(Expr arg: cc.arguments()) {
//			argSetVars.add(typedSetVar(arg));
//		}
//		
//		JL5MethodInstance ins = (JL5MethodInstance) cc.methodInstance();
//		v.addToSet(new InvokeMth(typedSetVar(), ins, argSetVars, new TypedSetVariable(call.type())));
		
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		ConstructorCall ctorCall = (ConstructorCall) this.node();
//		JL5ConstructorInstance ctorIns = (JL5ConstructorInstance) ctorCall.constructorInstance();
		
		// this(e1, ..., en); / super(e1, ..., en);
		final LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> x_effs = new LinkedHashMap<>();
		
		//   1. qualifier를 분석하면 나오는 effects(exn, activity)인 X_eff0를 가져오고, 
		final Expr qualifier = ctorCall.qualifier();
		if (qualifier != null) {
			EquGenExprExt.effects(qualifier, x_effs, EffectSetVarSource.SubExpression);
		}
		
		//   2. e1, ... , en를 분석해서 나오는 effects(exn, activity)인 X_eff1, ... , X_effn를 찾은 다음,
		for (Expr arg : ctorCall.arguments()) {
			EquGenExprExt.effects(arg, x_effs, EffectSetVarSource.SubExpression);
		}
		
		//   3. X_eff0 ∪ X_eff1 ∪ ... ∪ X_effn를 구하고, 이를 리턴할 effects(exn, activity)로 지정.
		setEffects(x_effs);
		
		setLocalEnv(v.peekTypeEnv().getCurrEnv());
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}