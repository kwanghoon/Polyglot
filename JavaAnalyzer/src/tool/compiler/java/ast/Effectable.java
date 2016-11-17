package tool.compiler.java.ast;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import polyglot.util.Pair;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.effect.EffectUnion;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarGoal;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;

public interface Effectable {
	
	/**
	 * @return the Exception Effect
	 */
	public EffectSetVariable exceptionEffect();
	
	/**
	 * @param type Effect Name
	 * @return the Effect
	 */
	public EffectSetVariable effect(EffectName type);
	
	/**
	 * @return all effects
	 */
	public HashMap<EffectName, EffectSetVariable> effects();
	
	
	public static class Effectable_c implements Effectable {
		
		private HashMap<EffectName, EffectSetVariable> effects = null;
		
		/**
		 * @see tool.compiler.java.ast.Effectable#exceptionEffect()
		 */
		@Override
		public final EffectSetVariable exceptionEffect() {
			return effect(EffectName.ExnEff);
		}
		
		/**
		 * @param exceptionEffect the Exception Effect to set
		 */
		public final void setExceptionEffect(EffectSetVariable exceptionEffect) {
			try {
				addEffect(EffectName.ExnEff, exceptionEffect);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("The argument exceptionEffect is not Exception Effect.");
			}
		}
		
		/**
		 * @param exceptionEffects Exception Effects to set
		 */
		public final void setExceptionEffect(final Map<EffectSetVariable, EffectSetVarSource> exceptionEffects) {
			addEffect(EffectName.ExnEff, exceptionEffects);
		}
		
		/**
		 * @see tool.compiler.java.ast.Effectable#effect(tool.compiler.java.effect.EffectName)
		 */
		@Override
		public final EffectSetVariable effect(EffectName type) {
			try {
				return effects.get(type);
			} catch(NullPointerException e) {
				return null;
			}
		}
		
		/**
		 * @see tool.compiler.java.ast.Effectable#effects()
		 */
		@Override
		public final HashMap<EffectName, EffectSetVariable> effects() {
			try {
				return effects;
			} catch(NullPointerException e) {
				return null;
			}
		}
		
		/**
		 * Report를 위하여, EffectSetVariable에 EffectSetVarSource를 연관시킴
		 * @param effects	연관시킬 effects
		 * @param effectSrc	연관시킬 EffectSetVarSource
		 * @param effectMap	연관시킨 effects를 담을 Map
		 */
		public static final void effects(
				HashMap<EffectName, EffectSetVariable> effects, 
				EffectSetVarSource effectSrc, 
				LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> effectMap) {
			if (effects != null && effectSrc != null) {
				for (Entry<EffectName, EffectSetVariable> entry : effects.entrySet()) {
					Map<EffectSetVariable, EffectSetVarSource> map = effectMap.get(entry.getKey());
					if (map == null) {
						map = new LinkedHashMap<>();
						effectMap.put(entry.getKey(), map);
					}
					map.put(entry.getValue(), effectSrc);
				}
			}
		}
		
		/**
		 * Report를 위하여, EffectSetVariable에 EffectSetVarSource를 연관시킴
		 * @param effects	연관시킬 effects
		 * @param effectSrc	연관시킬 EffectSetVarSource
		 * @param effectMap	연관시킨 effects를 담을 Map
		 */
		public static final LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> effects(
				HashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> subMap, 
				LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> effectMap) {
			for (Entry<EffectName, Map<EffectSetVariable, EffectSetVarSource>> entry : subMap.entrySet()) {
				Map<EffectSetVariable, EffectSetVarSource> effect = effectMap.get(entry.getKey());
				Pair<EffectSetVariable, EffectSetVarSource> union = unionize(entry.getValue());
				if (effect == null) {
					effect = new LinkedHashMap<>();
					effectMap.put(entry.getKey(), effect);
				}
				effect.put(union.part1(), union.part2());
			}
			return effectMap;
		}
		
		/**
		 * @param effect the Effect to add
		 */
		public final void addEffect(EffectSetVariable effect) {
			if (effect != null) {
				if(effects == null) {
					effects = new HashMap<>();
				}
				effects.put(effect.getEffectType(), effect);
			}
		}
		
		/**
		 * @param type	the type of the effect
		 * @param effect	the Effect to add
		 */
		public final void addEffect(EffectName type, EffectSetVariable effect) {
			if (effect != null) {
				if (effect.getEffectType().equals(type)) {
					addEffect(effect);
				} else {
					throw new IllegalArgumentException("Argumented type and effect's type are NOT matched.");
				}
			} else if (effects != null && effects.containsKey(type)) {
				effects.remove(type);
			}
		}
		
		/**
		 * @param type	the type of the effect
		 * @param effects Effects to add
		 */
		public final void addEffect(EffectName type, final Map<EffectSetVariable, EffectSetVarSource> effects) {
			try {
				if (!effects.isEmpty()) {	// 아래의 effect가 null이 아님이 보장됨.
					Pair<EffectSetVariable, EffectSetVarSource> effect = unionize(effects);
					addEffect(type, effect.part1());
					ReportUtil.report(effect.part1(), effect.part2(), EffectSetVarGoal.Return);
				}
			} catch (NullPointerException ignored) {
				// x_effs가 null인 경우는 무시
			}
		}
		
		/**
		 * @param effects Effects to set
		 */
		public final void setEffects(final Map<EffectName, Map<EffectSetVariable, EffectSetVarSource>> effects) {
			if (this.effects == null) {
				this.effects = new HashMap<>();
			} else {
				this.effects.clear();
			}
			for (Entry<EffectName, Map<EffectSetVariable, EffectSetVarSource>> entry : effects.entrySet()) {
				addEffect(entry.getKey(), entry.getValue());
			}
		}
		
		/**
		 * @param effects	target effects
		 * @return unionized EffectSetVariable and its source
		 */
		public static final Pair<EffectSetVariable, EffectSetVarSource> unionize(final Map<EffectSetVariable, EffectSetVarSource> effects) {
			EffectSetVarSource src_Effect;
			if (effects.size() > 1) {		// 새로운 EffectUnion이 생성되는 것이 보장됨.
				src_Effect = EffectSetVarSource.New;
				ReportUtil.report(effects, EffectSetVarGoal.Flow);
			} else if (!effects.isEmpty()) {	// effects의 size가 1임이 보장됨.
				src_Effect = (EffectSetVarSource) effects.values().toArray()[0];
			} else {
				return null;
			}
			return new Pair<EffectSetVariable, EffectSetVarSource> 
					(EffectUnion.unionize(effects.keySet()), src_Effect);
		}
	}
}