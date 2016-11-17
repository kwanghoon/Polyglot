package tool.compiler.java.ast.stmt;

import java.util.LinkedHashMap;
import java.util.Map;

import polyglot.ast.Node;
import polyglot.ast.Switch;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.ast.expr.EquGenExprExt;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * Switch <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenSwitchExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Switch";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		Switch switchStmt = (Switch) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		Switch switchStmt = (Switch) this.node();
		
		// switch ( expr ) { stmt1 ... stmtn }
		final LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> x_effs = new LinkedHashMap<>();
		
		//   1. expr을 분석하면 나오는 effects(exn, activity)인 X_eff0를 가져오고, 
		EquGenExprExt.effects(switchStmt.expr(), x_effs, EffectSetVarSource.SubExpression);
		
		//   2. stmt1, ... , stmtn를 분석해서 나오는 effects(exn, activity)의 집합 X_eff1을 만들고,
		EquGenStmtExt.effects(switchStmt.elements(), x_effs, EffectSetVarSource.SubStatement);
		
		//   3. X_eff0 ∪ X_eff1를 구하고, 이를 리턴할 effects(exn, activity)로 지정.
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