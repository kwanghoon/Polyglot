package tool.compiler.java.constraint;

import tool.compiler.java.aos.AbsObjSet;
import tool.compiler.java.aos.AbstractObject;
import tool.compiler.java.aos.DataFlowSetVariable;
import tool.compiler.java.aos.TypedSetVariable;
import tool.compiler.java.util.CollUtil;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * { context1, ..., contextk } <: X
 */
public class ObjsSubseteqX implements Constraint {
	
	// fields
	
	private LinkedHashSet<AbstractObject> objs;	// { context1, ..., contextk } (NOT null)
	private DataFlowSetVariable x;						// X (NOT null)
	
	
	// constructors
	
	/**
	 * { context1, ..., contextk } <: X
	 * @param objs	set objects	{ context1, ..., contextk }
	 * @param x		set X
	 */
	public ObjsSubseteqX(Collection<AbstractObject> objs, DataFlowSetVariable x) {
		this(new LinkedHashSet<>(objs), x);
	}
	
	/**
	 * { context1, ..., contextk } <: X
	 * @param objs	set objects	{ context1, ..., contextk }
	 * @param x		set X
	 */
	public ObjsSubseteqX(AbstractObject[] objs, DataFlowSetVariable x) {
		this(Arrays.asList(objs), x);
	}
	
	/**
	 * { context } <: X
	 * @param obj	set object	{ context }
	 * @param x		set X
	 */
	public ObjsSubseteqX(AbstractObject obj, DataFlowSetVariable x) {
		this(new LinkedHashSet<AbstractObject>(), x);
		if(obj != null) {
			this.objs.add(obj);
		} else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * { context1, ..., contextk } <: X
	 * @param objs	set objects	{ context1, ..., contextk }
	 * @param x		set X
	 */
	private ObjsSubseteqX(LinkedHashSet<AbstractObject> objs, DataFlowSetVariable x) {
		this.objs = objs;
		this.x = x;
	}
	
	
	// substitution methods
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * { context1, ..., contextk } <: X
	 * @param x		set X
	 * @return		Substituted New Constraint
	 */
	public ObjsSubseteqX substitute(TypedSetVariable x) {
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
	public Constraint substitute(List<TypedSetVariable> x) {
		if(x.size() != substitutableSize()) {
			throw new IllegalArgumentException("The Size of tsvs must be " + substitutableSize() + ". "
					+ "(Current size is " + x.size() + ".)");
		}
		return substitute(x.iterator().next());
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
	public DataFlowSetVariable getX() {
		return x;
	}
	
	
	@Override
	public int absObjSetSize() {
		return objs.size() + 1;
	}
	
	@Override
	public int substitutableSize() {
		return 1;
	}
	
	@Override
	public List<? extends AbsObjSet> getAllAbsObjSets() {
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
	
	@Override
	public String getKind() {
		return this.getClass().getSimpleName();
	}
	
	
	/**
	 * Form:	{ context1, ..., contextk } <: C{X}
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