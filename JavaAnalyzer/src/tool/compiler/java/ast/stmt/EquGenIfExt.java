package tool.compiler.java.ast.stmt;

import java.util.LinkedHashMap;
import java.util.Map;

import polyglot.ast.If;
import polyglot.ast.Node;
import polyglot.ast.Stmt;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.ast.expr.EquGenExprExt;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * If <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenIfExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "If";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		If ifStmt = (If) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		If ifStmt = (If) this.node();
		
		// if ( expr ) { stmt1 } else { stmt2 }
		final LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> x_effs = new LinkedHashMap<>();
		
		//   1. expr을 분석하면 나오는 effect인 X_eff0를 가져오고, 
		EquGenExprExt.effects(ifStmt.cond(), x_effs, EffectSetVarSource.SubExpression);
		
		//   2. stmt1을 분석하면 나오는 effect인 X_eff1를 가져온 다음, 
		EquGenStmtExt.effects(ifStmt.consequent(), x_effs, EffectSetVarSource.SubStatement);
		
		//   3. stmt2를 분석하면 나오는 effect인 X_eff2를 가져와, 
		Stmt alternative = ifStmt.alternative();
		if (alternative != null) {
			EquGenStmtExt.effects(alternative, x_effs, EffectSetVarSource.SubStatement);
		}
		
		//   4. X_eff0 ∪ X_eff1 ∪ X_eff2를 구하고, 이를 리턴할 exn effect로 지정.
		setEffects(x_effs);
		
		// if (condition) {consequent} else {alternative}
//		LocalEnvironment cons = EquGenStmtExt.localEnv(ifStmt.consequent());
//		LocalEnvironment altr = EquGenStmtExt.localEnv(ifStmt.alternative());
		
		// TODO: environment의 합집합을 구하는 동작이 필요 없음?
		setLocalEnv(v.peekTypeEnv().getCurrEnv());
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}