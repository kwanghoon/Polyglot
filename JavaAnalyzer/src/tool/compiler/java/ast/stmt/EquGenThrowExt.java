package tool.compiler.java.ast.stmt;

import polyglot.ast.Node;
import polyglot.ast.Throw;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.ast.expr.EquGenExprExt;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.effect.EffectVariable;
import tool.compiler.java.effect.ExnEffectVariable;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarGoal;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * Throw <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenThrowExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Throw";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		Throw throwStmt = (Throw) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		Throw throwStmt = (Throw) this.node();
		
		// throw expr;
		//   1. expr의 타입 C{Chi}을 가져와,
		MetaSetVariable cchi = EquGenExprExt.metaSetVar(throwStmt.expr());
		ReportUtil.report(cchi, MetaSetVarSource.SubExpression, MetaSetVarGoal.Effect);
		
		//   2. EffectVariable을 만들고
		EffectVariable exnEffect = new ExnEffectVariable(cchi);
		
		//   3. 이를 리턴할 exn effect로 지정.
		setExceptionEffect(exnEffect);
		ReportUtil.report(exnEffect, EffectSetVarSource.New, EffectSetVarGoal.Return);
		
		//   4. expr을 분석하면 나오는 activity effect를 가져오고, 
		final EffectSetVariable activityEffect = EquGenExprExt.exceptionEffect(throwStmt.expr());
		
		//   5. 이를 리턴할 activity effect로 지정.
		if (activityEffect != null) {
			ReportUtil.report(activityEffect, EffectSetVarSource.SubExpression, EffectSetVarGoal.Return);
			addEffect(EffectName.ActivityEff, activityEffect);
		}
		
		// TODO: 다른 effects가 추가된다면 그에 맞는 반환 작업이 필요
		
		setLocalEnv(v.peekTypeEnv().getCurrEnv());
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}