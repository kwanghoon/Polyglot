package tool.compiler.java.ast.stmt;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import polyglot.ast.Stmt;
import polyglot.ast.Node;
import polyglot.util.Pair;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.ast.Effectable;
import tool.compiler.java.ast.EquGenExt;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.env.LocalEnvironment;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenStmtExt extends EquGenExt implements Effectable {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Statement";
	
	private LocalEnvironment localEnv = null;
	private Effectable_c effectable = new Effectable_c();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
//		ReportUtil.enterReport(this);
//		Stmt stmt = (Stmt) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
//		ReportUtil.leaveReport(this);
//		Stmt stmt = (Stmt) this.node();
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
	
	
	/**
	 * @return the LocalEnvironment
	 */
	public final LocalEnvironment localEnv() {
		return localEnv;
	}
	
	/**
	 * @param n node
	 * @return the LocalEnvironment of node n
	 */
	public static final LocalEnvironment localEnv(Stmt n) {
		return ((EquGenStmtExt) EquGenExt.ext(n)).localEnv();
	}
	
	/**
	 * @param localEnv the LocalEnvironment to set
	 */
	protected final void setLocalEnv(LocalEnvironment localEnv) {
		this.localEnv = localEnv;
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
	public static final EffectSetVariable exceptionEffect(Stmt n) {
		return ((EquGenStmtExt) EquGenExt.ext(n)).exceptionEffect();
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
	public static final EffectSetVariable effect(Stmt n, EffectName type) {
		return ((EquGenStmtExt) EquGenExt.ext(n)).effect(type);
	}
	
	/**
	 * @param n node
	 * @return Effects of node n
	 */
	public static final HashMap<EffectName, EffectSetVariable> effects(Stmt n) {
		return ((EquGenStmtExt) EquGenExt.ext(n)).effects();
	}
	
	/**
	 * @param n node
	 * @param effectMap map for putting effects
	 * @param effectSrc source of effects
	 * @see tool.compiler.java.ast.Effectable.Effectable_c#effects(HashMap, EffectSetVarSource, LinkedHashMap)
	 */
	public static final HashMap<EffectName, EffectSetVariable> effects(Stmt n, 
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
			Collection<? extends Stmt> ns, 
			LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> effectMap, 
			EffectSetVarSource effectSrc) {
		final LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> subMap = new LinkedHashMap<>();
		for (Stmt n : ns) {
			EquGenStmtExt.effects(n, subMap, effectSrc);
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