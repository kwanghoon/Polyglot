package tool.compiler.java.effect;

import tool.compiler.java.aos.MetaSetVariable;

public class EffectVariable extends EffectSetVariable {
	
	private MetaSetVariable chi_effect;
	
	/**
	 * @param type
	 */
	protected EffectVariable(EffectName type) {
		super(type);
	}
	
	/**
	 * @param type
	 * @param chi_effect
	 */
	protected EffectVariable(EffectName type, MetaSetVariable chi_effect) {
		super(type);
		this.chi_effect = chi_effect;
	}
	
	public static EffectVariable create(EffectName type) {
		return create(type, null);
	}
	
	public static EffectVariable create(EffectName type, MetaSetVariable chi_effect) {
		switch (type) {
		case ExnEff:
			return new ExnEffectVariable(chi_effect);
		case ActivityEff:
			return new ActivityEffectVariable(chi_effect);
		}
		return new EffectVariable(type, chi_effect);
	}
	
	/**
	 * @return the chi_effect
	 */
	public MetaSetVariable getMetaSetVar() {
		return chi_effect;
	}
}