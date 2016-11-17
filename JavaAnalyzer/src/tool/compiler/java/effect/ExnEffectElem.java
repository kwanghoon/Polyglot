package tool.compiler.java.effect;

import polyglot.ext.jl5.types.JL5ClassType;

/**
 * Exception Effect
 */
public class ExnEffectElem implements EffectElem {
	
	private JL5ClassType exception;
	
	public ExnEffectElem(JL5ClassType throwableClassType) {
		this.exception = throwableClassType;
	}
	
	public JL5ClassType get() {
		return exception;
	}
	
	@Override
	public EffectName getEffectType() {
		return EffectName.ExnEff;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		try {
			return exception.name();
		} catch(NullPointerException e) {
			return null;
		}
	}
}