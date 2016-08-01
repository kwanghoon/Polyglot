package tool.compiler.java.aos;

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
		return type instanceof JL5ArrayType ? 	// C 또는 C[]에 대한 MetaSetVariable
				new ArrayMetaSetVariable((JL5ArrayType) type) : 	// C[] x: C[]{Chi1(base, elem)}
				new MetaSetVariable(type); 					// C x: C{Chi1}
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
	 * @return the isLocal
	 */
	@Deprecated
	public boolean isLocal() {
		return isLocal;
	}
}
