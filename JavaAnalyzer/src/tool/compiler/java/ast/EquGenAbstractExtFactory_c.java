package tool.compiler.java.ast;

import polyglot.ast.ExtFactory;
import polyglot.ext.jl7.ast.JL7AbstractExtFactory_c;

public abstract class EquGenAbstractExtFactory_c extends
	JL7AbstractExtFactory_c implements EquGenExtFactory {
	
	public EquGenAbstractExtFactory_c() {
		super();
	}
	
	public EquGenAbstractExtFactory_c(ExtFactory nextExtFactory) {
		super(nextExtFactory);
	}
	
	// TODO: Implement factory methods for new extension nodes in future
	// extensions. This entails calling the factory method for extension's
	// AST superclass.
}