package tool.compiler.java.visit;

import polyglot.types.Type;

public class SetVariable extends InfoVariable implements ProgramPointSet {
	
	private static long idGen = 1;
	
	/**
	 * 
	 */
	public SetVariable(Type type) {
		setType(type);
		
		if(type != null && !type.isVoid()) {
			generateID();
		}
	}
	
	/**
	 * @see tool.compiler.java.visit.InfoVariable#kind()
	 */
	@Override
	protected String kind() {
		return "X";
	}
	
	/**
	 * @return the ID
	 */
	public String getID() {
		if(!getType().isVoid()) {
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
		return idGen++;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = getType() + "{";
		if(!getType().isVoid()) {
			result += super.toString();
		} 
		return result + "}";
	}
}