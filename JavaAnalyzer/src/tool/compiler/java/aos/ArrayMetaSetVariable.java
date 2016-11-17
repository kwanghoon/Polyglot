package tool.compiler.java.aos;

import polyglot.ext.jl5.types.JL5ArrayType;
import polyglot.types.Type;

public class ArrayMetaSetVariable extends MetaSetVariable {
	
	private MetaSetVariable base;
	private MetaSetVariable length;
	
	protected ArrayMetaSetVariable(JL5ArrayType type) {
		super(type);
		this.base = MetaSetVariable.create(type.base());
		this.length = new MetaSetVariable(type.lengthField().type());
	}
	
	public static ArrayMetaSetVariable create(Type type) {
		try {
			return create((JL5ArrayType) type);
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("The type must be JL5ArrayType.");
		}
	}
	
	public static ArrayMetaSetVariable create(JL5ArrayType type) {
		return new ArrayMetaSetVariable(type);	// C[] x: C[]{Chi(base, elem)} (Array Type)
	}
	
	public MetaSetVariable base() {
		return base;
	}
	
	public MetaSetVariable length() {
		return length;
	}
	
	public MetaSetVariable ultimateBase() {
		if (base() instanceof ArrayMetaSetVariable) {
			return ((ArrayMetaSetVariable)base()).ultimateBase();
		}
		
		return base();
	}
}