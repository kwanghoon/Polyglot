package tool.compiler.java.visit;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import polyglot.types.Type;

public class TypedSetVariable extends TypedVariable implements ProgramPointSet {
	
	private LinkedHashSet<TypedSetVariable> subSetVars = null;
	
	private static long idGen = 1;
	private static final long NOID = -1;
	
	/**
	 * @param type
	 */
	public TypedSetVariable(Type type) {
		setType(type);
		generateID();
	}
	
	/**
	 * @param subSetVars
	 */
	@Deprecated
	public TypedSetVariable(Collection<TypedSetVariable> subSetVars) {
		this(inferTypeFromSubSetVars(subSetVars));
		setSubSetVars(subSetVars);
	}
	
	protected static final Type inferTypeFromSubSetVars(Collection<TypedSetVariable> subSetVars) {
		
		
		return null;
	}
	
	/**
	 * @see tool.compiler.java.visit.TypedVariable#kind()
	 */
	@Override
	protected String kind() {
		return "X";
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
	 * @return the subSetVars
	 */
	public Set<TypedSetVariable> getSubSetVars() {
		return subSetVars;
	}
	
	/**
	 * @param subSetVars the subSetVars to set
	 */
	protected final void setSubSetVars(Collection<TypedSetVariable> subSetVars) {
		this.subSetVars = new LinkedHashSet<>(subSetVars);
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
			return "The type of TypedSetVariable is null.";
		}
	}
}