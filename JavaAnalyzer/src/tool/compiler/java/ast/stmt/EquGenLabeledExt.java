package tool.compiler.java.ast.stmt;

import java.util.HashMap;
import java.util.Map.Entry;

import polyglot.ast.Labeled;
import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarGoal;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * Labeled <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenLabeledExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Labeled";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		Labeled labeled = (Labeled) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		Labeled labeled = (Labeled) this.node();
		
		// label: stmt
		//   1. expr를 분석하면 나오는 effects(exn, activity)를 가져와
		final HashMap<EffectName, EffectSetVariable> effects = EquGenStmtExt.effects(labeled.statement());
		
		//   2. 이를 리턴할 effects(exn, activity)로 지정.
		if (effects != null) {
			for (Entry<EffectName, EffectSetVariable> entry : effects.entrySet()) {
				EffectSetVariable effect = entry.getValue();
				addEffect(entry.getKey(), effect);
				ReportUtil.report(effect, EffectSetVarSource.SubStatement, EffectSetVarGoal.Return);
			}
		}
		
		setLocalEnv(v.peekTypeEnv().getCurrEnv());
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}