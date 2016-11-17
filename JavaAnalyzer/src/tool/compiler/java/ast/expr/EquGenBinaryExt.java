package tool.compiler.java.ast.expr;

import java.util.LinkedHashMap;
import java.util.Map;

import polyglot.ast.Binary;
import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.AbstractObject;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.constraint.ObjsSubseteqX;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * Binary <: Expr <: Term <: Node				<br>
 * Binary <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenBinaryExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Binary";
	
	private AbstractObject absObj;
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
		Binary binary = (Binary) this.node();
		
		absObj = new AbstractObject(binary);
		v.addToSet(absObj);
		ReportUtil.report(absObj);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		Binary binary = (Binary) this.node();
		
		// e1 > e2 / e1 < e2 / e1 == e2 / e1 <= e2 / e1 >= e2 / e1 != e2 /
		// e1 || e2 / e1 && e2 / e1 + e2 / e1 - e2 / e1 * e2 / e1 / e2 / e1 % e2 /
		// e1 | e2 / e1 & e2 / e1 ^ e2 / e1 << e2 / e1 >> e2 / e1 >>> e2
		//   1. T{Chi} 변수 생성
		MetaSetVariable tchi = MetaSetVariable.create(binary.type());
		ReportUtil.report(tchi, MetaSetVarSource.New, MetaSetVarGoal.Return);
		
		//   2. T{o} <: T{Chi} 제약식을 추가
		ObjsSubseteqX ox = new ObjsSubseteqX(absObj, tchi);
		v.getCurrCF().addMetaConstraint(ox);
		ReportUtil.report(ox);
		
		//   3. return T{Chi}
		setMetaSetVar(tchi);
		
		//   4. e1을 분석하면 나오는 effects(exn, activity)인 X_eff1를 가져오고, 
		final LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> x_effs = new LinkedHashMap<>();
		EquGenExprExt.effects(binary.left(), x_effs, EffectSetVarSource.SubExpression);
		
		//   5. e2를 분석하면 나오는 effects(exn, activity)인 X_eff2를 가져와, 
		EquGenExprExt.effects(binary.right(), x_effs, EffectSetVarSource.SubExpression);
		
		//   6. X_eff1 ∪ X_eff2를 구하고, 이를 리턴할 effects(exn, activity)로 지정
		setEffects(x_effs);
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}