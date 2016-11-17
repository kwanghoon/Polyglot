package tool.compiler.java.ast.stmt;

import java.util.LinkedHashMap;
import java.util.Map;

import polyglot.ast.Node;
import polyglot.ext.jl5.ast.ExtendedFor;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.ast.expr.EquGenExprExt;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * ExtendedFor <: Loop <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenExtendedForExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
//	public static final String KIND = "Enhanced For";
	public static final String KIND = "Extended For";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		ExtendedFor forLoop = (ExtendedFor) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		ExtendedFor forLoop = (ExtendedFor) this.node();
		
		// for ( stmt0 : expr ) { stmt1 }
		final LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> x_effs = new LinkedHashMap<>();
		
		//   1. stmt0을 분석하면 나오는 effects(exn, activity)인 X_eff0를 가져오고, 
		EquGenStmtExt.effects(forLoop.decl(), x_effs, EffectSetVarSource.SubStatement);
		
		//   2. expr을 분석하면 나오는 effects(exn, activity)인 X_eff1를 가져온 다음, 
		EquGenExprExt.effects(forLoop.expr(), x_effs, EffectSetVarSource.SubExpression);
		
		//   3. stmt1을 분석하면 나오는 effects(exn, activity)인 X_eff2를 가져와, 
		EquGenStmtExt.effects(forLoop.body(), x_effs, EffectSetVarSource.SubStatement);
		
		//   4. X_eff0 ∪ X_eff1 ∪ X_eff2를 구하고, 이를 리턴할 effects(exn, activity)로 지정.
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