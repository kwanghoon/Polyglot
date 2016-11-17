package tool.compiler.java.env;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import polyglot.ext.jl5.types.JL5LocalInstance;
import tool.compiler.java.aos.MetaSetVariable;

public class LocalEnvironment {
	private HashMap<JL5LocalInstance, MetaSetVariable> env;
	
	public LocalEnvironment() {
		env = new HashMap<>();
	}
	
	protected LocalEnvironment put(JL5LocalInstance local, MetaSetVariable msv) {
		env.put(local, msv);
		return this;
	}
	
	protected LocalEnvironment putAll(LocalEnvironment typeEnv) {
		env.putAll(typeEnv.env);
		return this;
	}
	
	public MetaSetVariable get(JL5LocalInstance x) {
		return env.get(x);
	}
	
	public Collection<MetaSetVariable> getAll() {
		return env.values();
	}
	
	public boolean contains(MetaSetVariable msv) {
		return env.containsValue(msv);
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		Iterator<Entry<JL5LocalInstance, MetaSetVariable>> i = env.entrySet().iterator();
		if (! i.hasNext()) {
			return "{}";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		for (;;) {
			Entry<JL5LocalInstance, MetaSetVariable> e = i.next();
			JL5LocalInstance key = e.getKey();
			MetaSetVariable value = e.getValue();
			sb.append(key);
			sb.append(':').append(' ');
			sb.append(value);
			if (! i.hasNext()) {
				return sb.append('}').toString();
			}
			sb.append('\n');
		}
	}
}
