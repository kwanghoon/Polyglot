package tool.compiler.java.visit;

/**
 * X <: Y
 */
public class XSubseteqY implements Constraint {
	
	// fields
	
	private AbsObjSet x, y;	// X, Y
	
	
	// constructor
	
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