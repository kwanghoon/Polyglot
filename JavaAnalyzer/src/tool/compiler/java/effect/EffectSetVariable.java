package tool.compiler.java.effect;

import tool.compiler.java.aos.SetVariable;

public abstract class EffectSetVariable extends SetVariable implements Effect {
	
	private EffectName type;
	public static final String KIND = "E";
//	public static final String KIND = "Effect";
	private static long idFactor = 1;
	
	/**
	 * @param type
	 */
	protected EffectSetVariable(EffectName type) {
		super();
		this.type = type;
	}
	
	@Override
	public EffectName getEffectType() {
		return this.type;
	}
	
	@Override
	public String kind() {
		return KIND;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return type.getSimpleString() + "{" + getID() + "}";
	}

	@Override
	protected long generateIDNum() {
		return idFactor++;
	}
}