package tool.compiler.java.ast.expr;

import java.util.LinkedHashMap;
import java.util.Map;

import polyglot.ast.ArrayAccess;
import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.ArrayMetaSetVariable;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * ArrayAccess <: Variable <: Expr <: Term <: Node				<br>
 * ArrayAccess <: Variable <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenArrayAccessExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Array Access";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		ArrayAccess arrAccess = (ArrayAccess) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		ArrayAccess arrAccess = (ArrayAccess) this.node();
		
		// a[i]	(ArrayAccess의 index는 하나임)
		//   1. a[i]의 타입 T{Chi}를 가져와
		ArrayMetaSetVariable arrMSV = (ArrayMetaSetVariable) metaSetVar(arrAccess.array());
		MetaSetVariable tchi = arrMSV.base();
		ReportUtil.report(tchi, MetaSetVarSource.ArrayBase, MetaSetVarGoal.Return);
		
		//   2. T{Chi}를 리턴할 타입으로 지정
		setMetaSetVar(tchi);
		
		//   3. a을 분석하면 나오는 exn effect인 X_eff0를 가져오고, 
		final LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> x_effs = new LinkedHashMap<>();
		EquGenExprExt.effects(arrAccess.array(), x_effs, EffectSetVarSource.SubExpression);
		
		//   4. i를 분석하면 나오는 exn effect인 X_eff1를 가져와, 
		EquGenExprExt.effects(arrAccess.index(), x_effs, EffectSetVarSource.SubExpression);
		
		//   5. X_eff0 ∪ X_eff1를 구하고, 이를 리턴할 exn effect로 지정
		setEffects(x_effs);
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}