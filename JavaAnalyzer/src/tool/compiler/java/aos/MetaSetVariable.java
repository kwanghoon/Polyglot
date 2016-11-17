package tool.compiler.java.aos;

import polyglot.ext.jl5.types.JL5ArrayType;
import polyglot.types.Type;

public class MetaSetVariable extends DataFlowSetVariable {
	
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
	protected MetaSetVariable(Type type, boolean isLocal) {
		super(type);
		this.isLocal = isLocal;
	}
	
	/**
	 * @param type
	 */
	protected MetaSetVariable(Type type) {
		super(type);
//		this(type, false);
	}
	
	public static MetaSetVariable create(Type type) {
		return type instanceof JL5ArrayType ? 	// C 또는 C[]에 대한 MetaSetVariable
				new ArrayMetaSetVariable((JL5ArrayType) type) :	// C[] x: C[]{Chi(base, elem)} (Array Type)
				new MetaSetVariable(type); 						// C x: C{Chi} (Scalar Type)
	}
	
	public boolean isNull() {
		return getType().isNull();
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
