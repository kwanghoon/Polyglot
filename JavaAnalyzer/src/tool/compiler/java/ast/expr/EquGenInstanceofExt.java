package tool.compiler.java.ast.expr;

import java.util.HashMap;
import java.util.Map.Entry;

import polyglot.ast.Instanceof;
import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.AbstractObject;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.constraint.ObjsSubseteqX;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarGoal;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * Instanceof <: Expr <: Term <: Node				<br>
 * Instanceof <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenInstanceofExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Instanceof";
	
	private AbstractObject absObj;
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
		Instanceof insof = (Instanceof) this.node();
		
		absObj = new AbstractObject(insof);
		v.addToSet(absObj);
		ReportUtil.report(absObj);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		Instanceof insof = (Instanceof) this.node();
		
		// e instanceof C
		//   1. boolean{Chi} 변수 생성
		MetaSetVariable tchi = MetaSetVariable.create(insof.type());
		ReportUtil.report(tchi, MetaSetVarSource.New, MetaSetVarGoal.Return);
		
		//   2. boolean{o} <: boolean{Chi} 제약식을 추가
		ObjsSubseteqX ox = new ObjsSubseteqX(absObj, tchi);
		v.getCurrCF().addMetaConstraint(ox);
		ReportUtil.report(ox);
		
		//   3. return boolean{Chi}
		setMetaSetVar(tchi);
		
		//   4. e를 분석하면 나오는 effects(exn, activity)를 가져와
		final HashMap<EffectName, EffectSetVariable> effects = EquGenExprExt.effects(insof.expr());
		
		//   5. 이를 리턴할 effects(exn, activity)로 지정
		if (effects != null) {
			for (Entry<EffectName, EffectSetVariable> entry : effects.entrySet()) {
				EffectSetVariable effect = entry.getValue();
				addEffect(entry.getKey(), effect);
				ReportUtil.report(effect, EffectSetVarSource.SubExpression, EffectSetVarGoal.Return);
			}
		}
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}