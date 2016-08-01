package tool.compiler.java.info;

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