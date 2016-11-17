package tool.compiler.java.ast.expr;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.util.Pair;
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
	
	/**
	 * @see tool.compiler.java.ast.Effectable#exceptionEffect()
	 * @see tool.compiler.java.ast.Effectable.Effectable_c#exceptionEffect()
	 */
	@Override
	public final EffectSetVariable exceptionEffect() {
		return effectable.exceptionEffect();
	}
	
	/**
	 * @param n node
	 * @return the Exception Effect of node n
	 */
	public static final EffectSetVariable exceptionEffect(Expr n) {
		return ((EquGenExprExt) EquGenExt.ext(n)).exceptionEffect();
	}
	
	/**
	 * @param exceptionEffect the Exception Effect to set
	 * @see tool.compiler.java.ast.Effectable.Effectable_c#setExceptionEffect(EffectSetVariable)
	 */
	protected final void setExceptionEffect(EffectSetVariable exceptionEffect) {
		effectable.setExceptionEffect(exceptionEffect);
	}
	
	/**
	 * @param exceptionEffects Exception Effects to set
	 * @see tool.compiler.java.ast.Effectable.Effectable_c#setExceptionEffect(Map)
	 */
	protected final void setExceptionEffect(Map<EffectSetVariable, EffectSetVarSource> exceptionEffects) {
		effectable.setExceptionEffect(exceptionEffects);
	}
	
	/**
	 * @see tool.compiler.java.ast.Effectable#effect(EffectName)
	 * @see tool.compiler.java.ast.Effectable.Effectable_c#effect(EffectName)
	 */
	@Override
	public final EffectSetVariable effect(EffectName type) {
		return effectable.effect(type);
	}
	
	/**
	 * @see tool.compiler.java.ast.Effectable#effects()
	 * @see tool.compiler.java.ast.Effectable.Effectable_c#effects()
	 */
	@Override
	public final HashMap<EffectName, EffectSetVariable> effects() {
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
	
	/**
	 * @param n node
	 * @param effectMap map for putting effects
	 * @param effectSrc source of effects
	 * @see tool.compiler.java.ast.Effectable.Effectable_c#effects(HashMap, EffectSetVarSource, LinkedHashMap)
	 */
	public static final HashMap<EffectName, EffectSetVariable> effects(Expr n, 
			LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> effectMap, 
			EffectSetVarSource effectSrc) {
		HashMap<EffectName, EffectSetVariable> effects = effects(n);
		Effectable_c.effects(effects, effectSrc, effectMap);
		return effects;
	}
	
	
	/**
	 * @param ns nodes
	 * @param effectMap map for putting effects
	 * @param effectSrc source of effects
	 * @see tool.compiler.java.ast.Effectable.Effectable_c#effects(HashMap, EffectSetVarSource, LinkedHashMap)
	 */
	public static final LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> effects(
			Collection<? extends Expr> ns, 
			LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> effectMap, 
			EffectSetVarSource effectSrc) {
		final LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> subMap = new LinkedHashMap<>();
		for (Expr n : ns) {
			EquGenExprExt.effects(n, subMap, effectSrc);
		}
		return Effectable_c.effects(subMap, effectMap);
	}
	
	/**
	 * @param effect the Effect to add
	 * @see tool.compiler.java.ast.Effectable.Effectable_c#addEffect(EffectSetVariable)
	 */
	protected final void addEffect(EffectSetVariable effect) {
		effectable.addEffect(effect);
	}
	
	/**
	 * @param type the type of the effect
	 * @param effect the Effect to add
	 * @see tool.compiler.java.ast.Effectable.Effectable_c#addEffect(EffectName, EffectSetVariable)
	 */
	protected final void addEffect(EffectName type, EffectSetVariable effect) {
		effectable.addEffect(type, effect);
	}
	
	/**
	 * @param type the type of the effect
	 * @param effects Effects to add
	 * @see tool.compiler.java.ast.Effectable.Effectable_c#addEffect(EffectName, Map)
	 */
	protected final void addEffect(EffectName type, final Map<EffectSetVariable, EffectSetVarSource> effects) {
		effectable.addEffect(type, effects);
		
	}
	
	/**
	 * @param effects Effects to set
	 * @see tool.compiler.java.ast.Effectable.Effectable_c#setEffects(Map)
	 */
	protected final void setEffects(final Map<EffectName, Map<EffectSetVariable, EffectSetVarSource>> effects) {
		effectable.setEffects(effects);
	}
	
	/**
	 * @see tool.compiler.java.ast.Effectable.Effectable_c#unionize(Map)
	 */
	protected static final Pair<EffectSetVariable, EffectSetVarSource> unionize(final Map<EffectSetVariable, EffectSetVarSource> effects) {
		return Effectable_c.unionize(effects);
	}
}