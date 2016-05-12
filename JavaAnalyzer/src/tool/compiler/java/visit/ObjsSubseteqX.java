package tool.compiler.java.visit;

import tool.compiler.java.util.CollUtil;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * { context1, ..., contextk } \subseteq X
 */
public class ObjsSubseteqX extends Constraint {
	
	// fields
	
	private LinkedHashSet<AbstractObjectInfo> objs;	// { context1, ..., contextk }
	private TypedSetVariable x;						// X
	
	
	// constructor
	
	/**
	 * { context1, ..., contextk } \subseteq X
	 * @param objs	set objects	{ context1, ..., contextk }
	 * @param x		set X
	 */
	public ObjsSubseteqX(Collection<AbstractObjectInfo> objs, TypedSetVariable x) {
		super();
		try{
			this.objs = new LinkedHashSet<>(objs);
		} catch (NullPointerException e) {
			this.objs = null;
		}
		this.x = x;
	}
	
	public ObjsSubseteqX(AbstractObjectInfo obj, TypedSetVariable x) {
		this(new LinkedList<AbstractObjectInfo>(), x);
		this.objs.add(obj);
	}
	
	/**
	 * { context1, ..., contextk } \subseteq X
	 * @param objs	set objects	{ context1, ..., contextk }
	 * @param x		set X
	 */
	public ObjsSubseteqX(AbstractObjectInfo[] objs, TypedSetVariable x) {
		this(Arrays.asList(objs), x);
	}
	
	
	// getter methods
	
	/**
	 * @return Objs	{ context1, ..., contextk }
	 */
	public List<AbstractObjectInfo> getObjs() {
		return new ArrayList<AbstractObjectInfo>(objs);
	}
	
	/**
	 * @return the X
	 */
	public TypedSetVariable getX() {
		return x;
	}
	
	
	/**
	 * Form:	{ context1, ..., contextk } \subseteq X
	 */
	@Override
	public String toString() {
		return CollUtil.getStringOf(objs, '{', '}') + " <: " + getX();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		try {
			for(AbstractObjectInfo obj : objs) {
				result = prime * result + ((obj == null) ? 0 : obj.hashCode());
			}
		} catch (NullPointerException ignored) {}
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		return result;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ObjsSubseteqX other = (ObjsSubseteqX) obj;
		if (objs == null) {
			if (other.objs != null) {
				return false;
			}
		} else if (!objs.equals(other.objs)) {
			return false;
		}
		if (x == null) {
			if (other.x != null) {
				return false;
			}
		} else if (!x.equals(other.x)) {
			return false;
		}
		return true;
	}
}