package tool.compiler.java.ast.stmt;

import java.util.LinkedHashMap;
import java.util.Map;

import polyglot.ast.Do;
import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.ast.expr.EquGenExprExt;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * Do <: Loop <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenDoExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Do-While";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		Do doLoop = (Do) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		Do doLoop = (Do) this.node();
		
		// do { stmt } while ( expr );
		final LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> x_effs = new LinkedHashMap<>();
		
		//   1. stmt을 분석하면 나오는 effects(exn, activity)인 X_eff1를 가져오고, 
		EquGenStmtExt.effects(doLoop.body(), x_effs, EffectSetVarSource.SubStatement);
		
		//   2. expr을 분석하면 나오는 effects(exn, activity)인 X_eff0를 가져와, 
		EquGenExprExt.effects(doLoop.cond(), x_effs, EffectSetVarSource.SubExpression);
		
		//   3. X_eff1 ∪ X_eff0를 구하고, 이를 리턴할 effects(exn, activity)로 지정.
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