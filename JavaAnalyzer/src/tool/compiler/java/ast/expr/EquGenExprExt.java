package tool.compiler.java.ast.expr;

import java.util.HashMap;
import java.util.Map;

import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.ast.Effectable;
import tool.compiler.java.ast.EquGenExt;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * Expr <: Term <: Node					<br>
 * Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenExprExt extends EquGenExt implements Effectable {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Expression";
	
	private MetaSetVariable metaSetVar = null;
	private Effectable_c effectable = new Effectable_c();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
//		ReportUtil.enterReport(this);
//		Expr expr = (Expr)this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
//		ReportUtil.leaveReport(this);
//		Expr expr = (Expr)this.node();
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
	
	
	/**
	 * @return the MetaSetVariable
	 */
	public final MetaSetVariable metaSetVar() {
		return metaSetVar;
	}
	
	/**
	 * @param n node
	 * @return the MetaSetVariable of node n
	 */
	public static final MetaSetVariable metaSetVar(Expr n) {
		return ((EquGenExprExt) EquGenExt.ext(n)).metaSetVar();
	}
	
	/**
	 * @param metaSetVar the MetaSetVariable to set
	 */
	protected final void setMetaSetVar(MetaSetVariable metaSetVar) {
		this.metaSetVar = metaSetVar;
	}
	
	@Override
	public EffectSetVariable exceptionEffect() {
		return effectable.exceptionEffect();
	}
	
	/**
	 * @param n node
	 * @return the Exception Effect of node n
	 */
	public static final EffectSetVariable exceptionEffect(Expr n) {
		return ((EquGenExprExt) EquGenExt.ext(n)).exceptionEffect();
	}
	
	@Override
	public void setExceptionEffect(EffectSetVariable exceptionEffect) {
		effectable.setExceptionEffect(exceptionEffect);
	}
	
	@Override
	public void setExceptionEffect(Map<EffectSetVariable, EffectSetVarSource> exceptionEffects) {
		effectable.setExceptionEffect(exceptionEffects);
	}
	
	@Override
	public EffectSetVariable effect(EffectName type) {
		return effectable.effect(type);
	}
	
	@Override
	public HashMap<EffectName, EffectSetVariable> effects() {
		return effectable.effects();
	}
	
	/**
	 * @param n node
	 * @param type Effect Name
	 * @return the Effect of node n
	 */
	public static final EffectSetVariable effect(Expr n, EffectName type) {
		return ((EquGenExprExt) EquGenExt.ext(n)).effect(type);
	}
	
	/**
	 * @param n node
	 * @return Effects of node n
	 */
	public static final HashMap<EffectName, EffectSetVariable> effects(Expr n) {
		return ((EquGenExprExt) EquGenExt.ext(n)).effects();
	}
	
	@Override
	public void addEffect(EffectSetVariable effect) {
		effectable.addEffect(effect);
	}
	
	@Override
	public void addEffect(EffectName type, EffectSetVariable effect) {
		effectable.addEffect(type, effect);
	}
}