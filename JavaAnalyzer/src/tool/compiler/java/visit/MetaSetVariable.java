package tool.compiler.java.visit;

import polyglot.types.Type;

public class MetaSetVariable extends AbsObjSet {
	
//	public static final String KIND = "Χ";	// Capital Chi
//	public static final String KIND = "χ";	// Small Letter Chi
	public static final String KIND = "Chi";
	private boolean isLocal;
	private static long idGen = 1;
	private static final long NOID = -1;
	
	/**
	 * @param type
	 * @param isLocal
	 */
	public MetaSetVariable(Type type, boolean isLocal) {
		setType(type);
		generateID();
		this.isLocal = isLocal;
	}
	
	/**
	 * @param type
	 */
	public MetaSetVariable(Type type) {
		this(type, false);
	}
	
	/**
	 * @see tool.compiler.java.visit.AbsObjSet#kind()
	 */
	@Override
	public String kind() {
		return KIND;
	}
	
	/**
	 * @return the ID
	 */
	public String getID() {
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
	protected long generateIDNum() {
		if(getType() != null && !getType().isVoid() && !getType().isNull()) {
			return idGen++;
		} else {
			return NOID;
		}
	}
	
	/**
	 * @return the isLocal
	 */
	public boolean isLocal() {
		return isLocal;
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
			return "The type field of MetaSetVariable is null.";
		}
	}
}
