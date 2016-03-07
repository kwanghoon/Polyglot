package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ext.jl7.ast.J7Lang;
import tool.compiler.java.visit.EquGenerator;

public interface EquGenLang extends J7Lang {
	// TODO: Declare any dispatch methods for new AST operations
	
	EquGenerator equGenEnter(Node n, EquGenerator v);
	Node equGen(Node n, EquGenerator v);
	
}