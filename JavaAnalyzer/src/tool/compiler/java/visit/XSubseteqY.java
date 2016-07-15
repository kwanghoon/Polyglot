package tool.compiler.java.visit;

import java.util.ArrayList;
import java.util.Collection;

/**
 * X <: Y
 */
public class XSubseteqY implements Constraint {
	
	// fields
	
	private AbsObjSet x, y;	// X, Y
	
	
	// constructors
	
	/**
	 * X <: Y
	 * @param x	set X
	 * @param y	set Y
	 */
	protected XSubseteqY(AbsObjSet x, AbsObjSet y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	/**
	 * X <: Y
	 * @param x	set X
	 * @param y	set Y
	 */
	public XSubseteqY(AbsObjSet x, MetaSetVariable y) {
		this(x, (AbsObjSet)y);
	}
	
	/**
	 * X <: Y
	 * @param x	set X
	 * @param y	set Y
	 */
	public XSubseteqY(AbsObjSet x, TypedSetVariable y) {
		this(x, (AbsObjSet)y);
	}
	
	
	// substitution methods
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * X <: Y
	 * @param x	set X
	 * @param y	set Y
	 * @return	Substituted New Constraint
	 */
	public XSubseteqY subst(TypedSetVariable x, TypedSetVariable y) {
		if(!this.x.equalsForType(x)) {
			throw new IllegalArgumentException("The Type Mismatch for x. "
					+ "(orig: " + this.x.getType() + ", subst: " + x.getType() + ")");
		}
		
		if(!this.y.equalsForType(y)) {
			throw new IllegalArgumentException("The Type Mismatch for y. "
					+ "(orig: " + this.y.getType() + ", subst: " + y.getType() + ")");
		}
		
		return new XSubseteqY(x, y);
	}
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * X <: Y
	 * @param xy	X and Y	(The size is 2)
	 * @return		Substituted New Constraint
	 */
	@Override
	public Constraint subst(Collection<TypedSetVariable> xy) {
		if(xy.size() != 2) {
			throw new IllegalArgumentException("The Size of tsvs must be 2.");
		}
		Object[] xyArr = xy.toArray();
		return subst((TypedSetVariable)xyArr[0], (TypedSetVariable)xyArr[1]);
	}
	
	
	// getter methods
	
	/**
	 * @return the X
	 */
	public AbsObjSet getX() {
		return x;
	}
	
	/**
	 * @return the Y
	 */
	public AbsObjSet getY() {
		return y;
	}
	
	
	@Override
	public ArrayList<AbsObjSet> getAllAbsObjSet() {
		ArrayList<AbsObjSet> abss = new ArrayList<>();
		abss.add(x);
		abss.add(y);
		return abss;
	}
	
	@Override
	public boolean contains(AbsObjSet aos) {
		if (x.equals(aos)) {
			return true;
		}
		if (y.equals(aos)) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Form:	X <: Y
	 */
	@Override
	public String toString() {
		return getX() + " <: " + getY();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
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
		XSubseteqY other = (XSubseteqY) obj;
		if (x == null) {
			if (other.x != null) {
				return false;
			}
		} else if (!x.equals(other.x)) {
			return false;
		}
		if (y == null) {
			if (other.y != null) {
				return false;
			}
		} else if (!y.equals(other.y)) {
			return false;
		}
		return true;
	}
}