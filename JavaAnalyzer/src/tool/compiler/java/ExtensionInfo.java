package tool.compiler.java;

import polyglot.ast.NodeFactory;
//import polyglot.ext.jl5.Topics;
import polyglot.ext.jl5.ast.JL5ExtFactory_c;
import polyglot.ext.jl7.ast.JL7ExtFactory_c;
import polyglot.frontend.Scheduler;
import polyglot.types.TypeSystem;
import tool.compiler.java.ast.EquGenExtFactory_c;
import tool.compiler.java.ast.EquGenLang_c;
import tool.compiler.java.ast.EquGenNodeFactory_c;
import tool.compiler.java.types.EquGenTypeSystem_c;

public class ExtensionInfo extends polyglot.ext.jl7.JL7ExtensionInfo {
	static {
//		Topics t = new Topics();
	}
	
	@Override
	public String defaultFileExtension() {
		return super.defaultFileExtension();
	}
	
	@Override
	public String compilerName() {
		return super.defaultFileExtension(); // TODO: ???
	}
	
	// public Parser parser(Reader reader, Source source, ErrorQueue eq); 
	
	// public Set<String> keywords();
	
	@Override
	public Scheduler createScheduler() {
		return new EquGenScheduler(this);
	}
	
	@Override
	protected NodeFactory createNodeFactory() {
		return new EquGenNodeFactory_c(EquGenLang_c.instance, 
				new EquGenExtFactory_c(new JL7ExtFactory_c(new JL5ExtFactory_c())));
	}
	
	@Override
	protected TypeSystem createTypeSystem() {
		return new EquGenTypeSystem_c();
	}
}