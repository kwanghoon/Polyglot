package tool.compiler.java.aos;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import polyglot.types.Type;

public class TypedSetVariable extends SetVariable {
	
	@Deprecated
	private LinkedHashSet<TypedSetVariable> subSetVars = null;
	
	public static final String KIND = "X";
	private static long idFactor = 1;
	
	/**
	 * @param type
	 */
	public TypedSetVariable(Type type) {
		super(type);
	}
	
	/**
	 * @param subSetVars
	 */
	@Deprecated
	public TypedSetVariable(Collection<TypedSetVariable> subSetVars) {
		this(inferTypeFromSubSetVars(subSetVars));
		setSubSetVars(subSetVars);
	}
	
	@Deprecated
	protected static final Type inferTypeFromSubSetVars(Collection<TypedSetVariable> subSetVars) {
		
		
		return null;
	}
	
	/**
	 * @see tool.compiler.java.aos.AbsObjSet#kind()
	 */
	@Override
	public String kind() {
		return KIND;
	}
	
	/**
	 * generate ID Number
	 */
	@Override
	protected long idFoctor() {
		return idFactor++;
	}
	
	/**
	 * @return the subSetVars
	 */
	@Deprecated
	public Set<TypedSetVariable> getSubSetVars() {
		return subSetVars;
	}
	
	/**
	 * @param subSetVars the subSetVars to set
	 */
	@Deprecated
	protected final void setSubSetVars(Collection<TypedSetVariable> subSetVars) {
		this.subSetVars = new LinkedHashSet<>(subSetVars);
	}
}