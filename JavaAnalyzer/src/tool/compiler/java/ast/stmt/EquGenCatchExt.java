package tool.compiler.java.ast.stmt;

import polyglot.ast.Catch;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5ClassType;
import polyglot.types.Type;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.effect.EffectSet;
import tool.compiler.java.effect.ExnEffectElem;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarGoal;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * Catch <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenCatchExt extends EquGenAbstractCatchExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Catch";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		Catch singleCatch = (Catch) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		Catch catchStmt = (Catch) this.node();
		
		// catch (C e) { stmt }
		//   1. e의 타입 C를 가져와 EffectSet을 만들고 이를 리턴하고,
		Type c = catchStmt.catchType();
		EffectSet<ExnEffectElem> exnEffect = new EffectSet<>(new ExnEffectElem((JL5ClassType) c));
		setFormalTypes(exnEffect);
		ReportUtil.report(exnEffect, EffectSetVarSource.New, EffectSetVarGoal.Return);
		
		//   2. stmt를 분석하면 나오는 exn effect인 exnEffect를 가져와 이를 리턴할 exn effect로 지정. (EquGenAbstractCatchExt)
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}