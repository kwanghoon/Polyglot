package tool.compiler.java.visit;

import java.util.HashMap;

import polyglot.ext.jl5.types.JL5LocalInstance;

public class LocalEnv {
	
	private HashMap<JL5LocalInstance, MetaSetVariable> env;
	
	/**
	 */
	public LocalEnv() {
		env = new HashMap<>();
	}
	
	public void add(JL5LocalInstance x, MetaSetVariable tsv) {
		env.put(x, tsv);
	}
	
	public MetaSetVariable lookUp(JL5LocalInstance x) {
		return env.get(x);
	}
	
	/**
	 * @return the env
	 */
	public HashMap<JL5LocalInstance, MetaSetVariable> getEnv() {
		return env;
	}
}