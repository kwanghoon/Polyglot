package tool.compiler.java.ast.stmt;

import java.util.LinkedHashMap;
import java.util.Map;

import polyglot.ast.For;
import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.ast.expr.EquGenExprExt;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * For <: Loop <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenForExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "For";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		For forLoop = (For) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		For forLoop = (For) this.node();
		
		// for ( stmt_i1, ... , stmt_in ; expr ; stmt_u1, ... , stmt_uk ) { stmt }
		final LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> x_effs = new LinkedHashMap<>();
		
		//   1. stmt_i1, ... , stmt_in를 분석해서 나오는 effects(exn, activity)의 집합 X_eff0을 만들고,
		EquGenStmtExt.effects(forLoop.inits(), x_effs, EffectSetVarSource.SubStatement);
		
		//   2. expr을 분석하면 나오는 exn effect인 X_eff1를 가져오고, 
		EquGenExprExt.effects(forLoop.cond(), x_effs, EffectSetVarSource.SubExpression);
		
		//   3. stmt_u1, ... , stmt_uk를 분석해서 나오는 effects(exn, activity)의 집합 X_eff2을 만들고,
		EquGenStmtExt.effects(forLoop.iters(), x_effs, EffectSetVarSource.SubStatement);
		
		//   4. stmt를 분석하면 나오는 exn effect인 X_eff3를 가져와, 
		EquGenStmtExt.effects(forLoop.body(), x_effs, EffectSetVarSource.SubStatement);
		
		//   5. X_eff0 ∪ X_eff1 ∪ X_eff2 ∪ X_eff3를 구하고, 이를 리턴할 effects(exn, activity)로 지정.
		setEffects(x_effs);
		
		// TODO: environment의 합집합을 구하는 동작이 필요 없음?
		setLocalEnv(v.peekTypeEnv().getCurrEnv());
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}