package tool.compiler.java.visit;

import polyglot.types.Type;

public interface Info {
	
	/**
	 * @return	the type
	 */
	public Type getType();
	
	/**
	 * @return	the base type
	 */
	public Type getBaseType();
}