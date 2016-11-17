package tool.compiler.java.ast.expr;

import java.util.LinkedHashMap;
import java.util.Map;

import polyglot.ast.Assign;
import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * Assign <: Expr <: Term <: Node				<br>
 * Assign <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenAssignExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Assign";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
//		Assign asgn = (Assign) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Assign asgn = (Assign) this.node();
		
		// e1 = e2 / e1 op= e2
		final LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> x_effs = new LinkedHashMap<>();
		
		//   4. e1을 분석하면 나오는 effects(exn, activity)인 X_eff1를 가져오고, 
		EquGenExprExt.effects(asgn.left(), x_effs, EffectSetVarSource.SubExpression);
		
		//   5. e2를 분석하면 나오는 effects(exn, activity)인 X_eff2를 가져와, 
		EquGenExprExt.effects(asgn.right(), x_effs, EffectSetVarSource.SubExpression);
		
		//   6. X_eff1 ∪ X_eff2를 구하고, 이를 리턴할 effects(exn, activity)로 지정
		setEffects(x_effs);
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}