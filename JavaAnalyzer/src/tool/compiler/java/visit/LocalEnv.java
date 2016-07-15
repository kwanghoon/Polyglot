package tool.compiler.java.visit;

import java.util.HashMap;
import java.util.LinkedList;

import polyglot.ext.jl5.types.JL5LocalInstance;

public class LocalEnv {
	
	private HashMap<JL5LocalInstance, MetaSetVariable> currEnv;		// 현재 환경
	private LinkedList<HashMap<JL5LocalInstance, MetaSetVariable>> envStack;
	
	/**
	 */
	public LocalEnv() {
		envStack = new LinkedList<>();
	}
	
	public void add(JL5LocalInstance x, MetaSetVariable tsv) {
		currEnv.put(x, tsv);
	}
	
	public MetaSetVariable lookUp(JL5LocalInstance x) {
		MetaSetVariable msv = null;
		for (HashMap<JL5LocalInstance, MetaSetVariable> tmpenv : envStack) {
			msv = tmpenv.get(x);
			if (msv != null) {
				break;
			}
		}
		return msv;
	}
	
//	/**
//	 * @return the current env
//	 */
//	public HashMap<JL5LocalInstance, MetaSetVariable> getCurrEnv() {
//		return currEnv;
//	}
	
	public void push() {
		currEnv = new HashMap<>();
		envStack.push(currEnv);
	}
	
	public void pop() {
		envStack.pop();
		currEnv = envStack.peek();
	}
}