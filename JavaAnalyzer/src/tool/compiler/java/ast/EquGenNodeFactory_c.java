package tool.compiler.java.ast;

import polyglot.ext.jl7.ast.JL7NodeFactory_c;

public class EquGenNodeFactory_c extends JL7NodeFactory_c implements
		EquGenNodeFactory {
	
	public EquGenNodeFactory_c(EquGenLang lang, EquGenExtFactory extFactory) {
		super(lang, extFactory);
	}
	
	@Override 
	public EquGenExtFactory extFactory() {
		return (EquGenExtFactory) super.extFactory();
	}
	
	
	// TODO: Implement factory methods for new AST nodes.
	// TODO: Override factory methods for overridden AST nodes.
	// TODO: Override factory methods for AST nodes with new extension nodes.
}