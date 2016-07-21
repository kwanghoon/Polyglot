package tool.compiler.java.visit;

import polyglot.ext.jl5.types.JL5ArrayType;
import polyglot.types.Type;

public class MetaSetVariable extends SetVariable {
	
//	public static final String KIND = "Χ";	// Capital Chi
//	public static final String KIND = "χ";	// Small Letter Chi
	public static final String KIND = "Chi";
	private static long idFactor = 1;
	@Deprecated
	private boolean isLocal;
	
	/**
	 * @param type
	 * @param isLocal
	 */
	@Deprecated
	public MetaSetVariable(Type type, boolean isLocal) {
		super(type);
		this.isLocal = isLocal;
	}
	
	/**
	 * @param type
	 */
	public MetaSetVariable(Type type) {
		super(type);
//		this(type, false);
	}
	
	public static MetaSetVariable create(Type type) {
		return type instanceof JL5ArrayType ? new ArrayMetaSetVariable((JL5ArrayType) type) : new MetaSetVariable(type);
	}
	
	/**
	 * @see tool.compiler.java.visit.AbsObjSet#kind()
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
	 * @return the isLocal
	 */
	@Deprecated
	public boolean isLocal() {
		return isLocal;
	}
}
