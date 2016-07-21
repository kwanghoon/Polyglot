package tool.compiler.java.visit;

import polyglot.types.Type;

public abstract class SetVariable extends AbsObjSet {
	
	protected static final long NOID = -1;
	
	/**
	 * @param type
	 */
	protected SetVariable(Type type) {
		setType(type);
		generateID();
	}
	
	protected abstract long idFoctor();
	
	/**
	 * @return the ID
	 */
	public final String getID() {
		if(idNum() != NOID) {
			return super.getID();
		} else {
			return null;
		}
	}
	
	/**
	 * generate ID Number
	 */
	@Override
	protected final long generateIDNum() {
		if(getType() != null && !getType().isVoid() && !getType().isNull()) {
			return idFoctor();
		} else {
			return NOID;
		}
	}
		
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		try {
			if(getType().isNull()) {
				return "null";
			}
			
			String result = getType() + "{";
			if(!getType().isVoid()) {
				result += super.toString();
			} 
			return result + "}";
		} catch (NullPointerException e) {
			return "The type field of TypedSetVariable is null.";
		}
	}
}