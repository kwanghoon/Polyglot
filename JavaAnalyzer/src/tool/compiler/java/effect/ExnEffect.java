package tool.compiler.java.effect;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import polyglot.ext.jl5.types.JL5ClassType;
import tool.compiler.java.util.CollUtil;

/**
 * Exception Effect
 */
public class ExnEffect implements Effect, Iterable<JL5ClassType> {
	
	private LinkedHashSet<JL5ClassType> exceptions;
	
	public ExnEffect() {
		exceptions = new LinkedHashSet<>();
	}

	public void add(JL5ClassType exceptionClass) {
		exceptions.add(exceptionClass);
	}
	
	public void addAll(Collection<JL5ClassType> exceptionClasses) {
		exceptions.addAll(exceptionClasses);
	}
	
	public JL5ClassType get(int index) {
		return (JL5ClassType) exceptions.toArray()[index];
	}
	
	public Collection<JL5ClassType> getAll() {
		return new LinkedHashSet<>(exceptions);
	}
	
	public int size() {
		return exceptions.size();
	}
	
	@Override
	public EffectName getType() {
		return EffectName.ExnEff;
	}

	@Override
	public Iterator<JL5ClassType> iterator() {
		return exceptions.iterator();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		try {
			return CollUtil.getStringOf(exceptions);
		} catch(NullPointerException e) {
			return null;
		}
	}
}