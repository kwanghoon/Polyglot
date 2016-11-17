package tool.compiler.java.ast.expr;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import polyglot.ast.Cast;
import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.ArrayMetaSetVariable;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.constraint.XSubseteqY;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.util.EquGenUtil;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarGoal;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * Cast <: Expr <: Term <: Node					<br>
 * Cast <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenCastExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Cast";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		Cast cast = (Cast) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		Cast cast = (Cast) this.node();
		
		// (C) e
		//   1. 리턴할 타입 C{Chi1}를 만든 다음, (Chi는 새로 만들고 C는 이 노드 자신의 타입)
		MetaSetVariable cchi1 = MetaSetVariable.create(cast.type());
//		MetaSetVariable cchi1 = new MetaSetVariable(cast.type());
		ReportUtil.report(cchi1, MetaSetVarSource.New, MetaSetVarGoal.Return);
		
		//   2. e의 타입 D{Chi2}를 가져온다.
		MetaSetVariable dchi2 = metaSetVar(cast.expr());
		ReportUtil.report(dchi2, MetaSetVarSource.SubExpression, MetaSetVarGoal.Flow);
		
		//   3-1. D{Chi2} <: C{Chi1} 제약식을 추가
		XSubseteqY xy = new XSubseteqY(dchi2, cchi1);
		v.getCurrCF().addMetaConstraint(xy);
		ReportUtil.report(xy);
		
		//   3-2. 배열인 경우, D{Chi2} <: C{Chi1}의 하위 레벨 제약식을 집합에 추가
		//         (Top Level 아래의 MetaSetVariable(s)의 데이터 플로우)
		if(EquGenUtil.isArray(dchi2) && EquGenUtil.isArray(cchi1)) {
			Collection<XSubseteqY> xys = XSubseteqY.constrain(
					(ArrayMetaSetVariable) dchi2, 
					(ArrayMetaSetVariable) cchi1);
			v.getCurrCF().addMetaConstraints(xys);
		}
		
		//   4. C{Chi1}를 리턴 타입으로 지정
		setMetaSetVar(cchi1);
		
		//   5. e를 분석하면 나오는 effects(exn, activity)를 가져와
		final HashMap<EffectName, EffectSetVariable> effects = EquGenExprExt.effects(cast.expr());
		
		//   6. 이를 리턴할 effects(exn, activity)로 지정
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