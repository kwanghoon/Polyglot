package tool.compiler.java.aos;

import java.util.Collection;

import polyglot.types.Type;

public class TypedSetVariable extends DataFlowSetVariable {
	
	public static final String KIND = "X";
	private static long idFactor = 1;
	
	/**
	 * @param type
	 */
	public TypedSetVariable(Type type) {
		super(type);
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
}