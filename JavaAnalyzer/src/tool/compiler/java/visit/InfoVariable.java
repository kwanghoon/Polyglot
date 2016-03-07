package tool.compiler.java.visit;

import polyglot.ext.jl5.types.JL5SubstClassType;
import polyglot.types.Type;

public abstract class InfoVariable implements Info {
	
	private Type type;
	private long idNum;
	
	/**
	 * @return the type
	 */
	@Override
	public Type getType() {
		return type;
	}
	
	/**
	 * 
	 */
	@Override
	public Type getBaseType() {
		if(type instanceof JL5SubstClassType) {
			return ((JL5SubstClassType)type).base();
		}
		return getType();
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
	/**
	 * @return the ID number
	 */
	protected final long idNum() {
		return idNum;
	}
	
	/**
	 * @return the kind of program point
	 */
	protected abstract String kind();
	
	/**
	 * @return the ID number to set
	 */
	protected abstract long generateIDNum();
	
	/**
	 * generate the ID
	 */
	protected final void generateID() {
		idNum = generateIDNum();
	}
	
	/**
	 * @return the ID
	 */
	public String getID() {
		return kind() + idNum();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getID();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}