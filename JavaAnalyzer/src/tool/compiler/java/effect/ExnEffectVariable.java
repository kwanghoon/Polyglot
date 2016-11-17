package tool.compiler.java.effect;

import tool.compiler.java.aos.MetaSetVariable;

public class ExnEffectVariable extends EffectVariable {
	
	/**
	 * 
	 */
	public ExnEffectVariable() {
		this(null);
	}
	
	/**
	 * @param chi_effect
	 */
	public ExnEffectVariable(MetaSetVariable chi_effect) {
		super(EffectName.ExnEff, chi_effect);
	}
}