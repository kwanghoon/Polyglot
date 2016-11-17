package tool.compiler.java.ast.stmt;

import java.util.LinkedHashMap;
import java.util.Map;

import polyglot.ast.Assert;
import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.ast.expr.EquGenExprExt;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * Assert <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenAssertExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Assert";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		Assert assertStmt = (Assert) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		Assert assertStmt = (Assert) this.node();
		
		// assert expr0 : expr1;
		final LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> x_effs = new LinkedHashMap<>();
		
		//   1. expr0을 분석하면 나오는 effects(exn, activity)인 X_eff0를 가져오고, 
		EquGenExprExt.effects(assertStmt.cond(), x_effs, EffectSetVarSource.SubExpression);
		
		//   2. expr1를 분석하면 나오는 effects(exn, activity)인 X_eff1를 가져와, 
		EquGenExprExt.effects(assertStmt.errorMessage(), x_effs, EffectSetVarSource.SubExpression);
		
		//   3. X_eff0 ∪ X_eff1를 구하고, 이를 리턴할 effects(exn, activity)로 지정.
		setEffects(x_effs);
		
		setLocalEnv(v.peekTypeEnv().getCurrEnv());
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}