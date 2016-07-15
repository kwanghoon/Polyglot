package tool.compiler.java.visit;

import polyglot.ext.jl5.types.JL5ArrayType;
import polyglot.types.Type;

public class ArrayMetaSetVariable extends MetaSetVariable {
	
	private MetaSetVariable base;
	private MetaSetVariable length;
	
	public ArrayMetaSetVariable(JL5ArrayType type) {
		super(type);
		
		Type baseType = type.base();
		if(baseType instanceof JL5ArrayType) {
			base = new ArrayMetaSetVariable((JL5ArrayType) baseType);
		} else {
			base = new MetaSetVariable(baseType);
		}
		
		length = new MetaSetVariable(type.lengthField().type());
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