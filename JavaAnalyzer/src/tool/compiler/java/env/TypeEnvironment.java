package tool.compiler.java.env;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import polyglot.ext.jl5.types.JL5LocalInstance;
import tool.compiler.java.aos.MetaSetVariable;

public class TypeEnvironment {
	
	private LocalEnvironment currEnv;		// 현재 환경
	private LinkedList<LocalEnvironment> envStack;
	
	/**
	 */
	public TypeEnvironment() {
		envStack = new LinkedList<>();
	}
	
	public void add(JL5LocalInstance x, MetaSetVariable tsv) {
		currEnv.put(x, tsv);
	}
	
	public MetaSetVariable lookUp(JL5LocalInstance x) {
		MetaSetVariable msv = null;
		for (LocalEnvironment tmpenv : envStack) {
			msv = tmpenv.get(x);
			if (msv != null) {
				break;
			}
		}
		return msv;
	}
	
	public Collection<MetaSetVariable> lookUpAll() {
		ArrayList<MetaSetVariable> msvs = new ArrayList<>();
		for (LocalEnvironment tmpenv : envStack) {
			msvs.addAll(tmpenv.getAll());
		}
		return msvs;
	}
	
	/**
	 * @return the current env
	 */
	public LocalEnvironment getCurrEnv() {
		return currEnv;
	}
	
	public void push() {
		currEnv = new LocalEnvironment();
		envStack.push(currEnv);
	}
	
	public LocalEnvironment pop() {
		LocalEnvironment prevPeek = envStack.pop();
		currEnv = envStack.peek();
		return prevPeek;
	}
}