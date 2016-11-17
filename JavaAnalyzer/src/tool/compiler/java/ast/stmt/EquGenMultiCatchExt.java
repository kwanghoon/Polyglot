package tool.compiler.java.ast.stmt;

import java.util.ArrayList;

import polyglot.ast.Node;
import polyglot.ast.TypeNode;
import polyglot.ext.jl5.types.JL5ClassType;
import polyglot.ext.jl7.ast.MultiCatch;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.effect.EffectSet;
import tool.compiler.java.effect.ExnEffectElem;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarGoal;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * MultiCatch <: Catch <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenMultiCatchExt extends EquGenAbstractCatchExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Multi-Catch";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		MultiCatch multiCatch = (MultiCatch) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		MultiCatch multiCatch = (MultiCatch) this.node();
		
		// catch (C1 | ... | Cn e) { stmt }
		//   1. e의 타입 C1, ... , Cn을 가져와 EffectSet을 만들고 이를 리턴하고,
		ArrayList<ExnEffectElem> cs = new ArrayList<>();
		for (TypeNode ci : multiCatch.alternatives()) {
			cs.add(new ExnEffectElem((JL5ClassType) ci.type()));
		}
		EffectSet<ExnEffectElem> exnEffect = new EffectSet<>(cs);
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