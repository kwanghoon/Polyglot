package tool.compiler.java.visit;

import tool.compiler.java.util.CollUtil;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * { context1, ..., contextk } <: X
 */
public class ObjsSubseteqX implements Constraint {
	
	// fields
	
	private LinkedHashSet<AbstractObject> objs;	// { context1, ..., contextk }
	private AbsObjSet x;						// X
	
	
	// constructors
	
	/**
	 * { context1, ..., contextk } <: X
	 * @param objs	set objects	{ context1, ..., contextk }
	 * @param x		set X
	 */
	protected ObjsSubseteqX(Collection<AbstractObject> objs, AbsObjSet x) {
		super();
		try{
			this.objs = new LinkedHashSet<>(objs);
		} catch (NullPointerException e) {
			this.objs = null;
		}
		this.x = x;
	}
	
	/**
	 * { context1, ..., contextk } <: X
	 * @param objs	set objects	{ context1, ..., contextk }
	 * @param x		set X
	 */
	protected ObjsSubseteqX(Collection<AbstractObject> objs, MetaSetVariable x) {
		this(objs, (AbsObjSet)x);
	}
	
	/**
	 * { context1, ..., contextk } <: X
	 * @param objs	set objects	{ context1, ..., contextk }
	 * @param x		set X
	 */
	protected ObjsSubseteqX(Collection<AbstractObject> objs, TypedSetVariable x) {
		this(objs, (AbsObjSet)x);
	}
	
	/**
	 * { context } <: X
	 * @param obj	set object	{ context }
	 * @param x		set X
	 */
	protected ObjsSubseteqX(AbstractObject obj, AbsObjSet x) {
		this(new LinkedHashSet<AbstractObject>(), x);
		this.objs.add(obj);
	}
	
	/**
	 * { context } <: X
	 * @param obj	set object	{ context }
	 * @param x		set X
	 */
	public ObjsSubseteqX(AbstractObject obj, MetaSetVariable x) {
		this(obj, (AbsObjSet)x);
	}
	
	/**
	 * { context } <: X
	 * @param obj	set object	{ context }
	 * @param x		set X
	 */
	public ObjsSubseteqX(AbstractObject obj, TypedSetVariable x) {
		this(new LinkedList<AbstractObject>(), x);
		this.objs.add(obj);
	}
	
	/**
	 * { context1, ..., contextk } <: X
	 * @param objs	set objects	{ context1, ..., contextk }
	 * @param x		set X
	 */
	protected ObjsSubseteqX(AbstractObject[] objs, AbsObjSet x) {
		this(Arrays.asList(objs), x);
	}
	
	/**
	 * { context1, ..., contextk } <: X
	 * @param objs	set objects	{ context1, ..., contextk }
	 * @param x		set X
	 */
	public ObjsSubseteqX(AbstractObject[] objs, MetaSetVariable x) {
		this(objs, (AbsObjSet)x);
	}
	
	/**
	 * { context1, ..., contextk } <: X
	 * @param objs	set objects	{ context1, ..., contextk }
	 * @param x		set X
	 */
	public ObjsSubseteqX(AbstractObject[] objs, TypedSetVariable x) {
		this(objs, (AbsObjSet)x);
	}
	
	
	// substitution methods
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * { context1, ..., contextk } <: X
	 * @param x		set X
	 * @return		Substituted New Constraint
	 */
	public ObjsSubseteqX subst(TypedSetVariable x) {
		if(!this.x.equalsForType(x)) {
			throw new IllegalArgumentException("The Type Mismatch for x. "
					+ "(orig: " + this.x.getType() + ", subst: " + x.getType() + ")");
		}
		
		return new ObjsSubseteqX(this.objs, x);
	}
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * { context1, ..., contextk } <: X
	 * @param x		set X	(The size is 1.)
	 * @return		Substituted New Constraint
	 */
	@Override
	public Constraint subst(Collection<TypedSetVariable> x) {
		if(x.size() != 1) {
			throw new IllegalArgumentException("The Size of tsvs must be 1.");
		}
		return subst(x.iterator().next());
	}
	
	
	// getter methods
	
	/**
	 * @return Objs	{ context1, ..., contextk }
	 */
	public List<AbstractObject> getObjs() {
		return new ArrayList<AbstractObject>(objs);
	}
	
	/**
	 * @return the X
	 */
	public AbsObjSet getX() {
		return x;
	}
	
	
	@Override
	public ArrayList<AbsObjSet> getAllAbsObjSet() {
		ArrayList<AbsObjSet> abss = new ArrayList<>();
		abss.addAll(objs);
		abss.add(x);
		return abss;
	}
	
	@Override
	public boolean contains(AbsObjSet aos) {
		if (objs.contains(aos)) {
			return true;
		}
		if (x.equals(aos)) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Form:	{ context1, ..., contextk } <: X
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
			for(AbstractObject obj : objs) {
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