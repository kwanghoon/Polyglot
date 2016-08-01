package tool.compiler.java.constraint;

import java.util.ArrayList;
import java.util.List;

import tool.compiler.java.aos.AbsObjSet;
import tool.compiler.java.aos.SetVariable;
import tool.compiler.java.aos.TypedSetVariable;

/**
 * X <: Y
 */
public class XSubseteqY implements Constraint {
	
	// fields
	
	private SetVariable x, y;	// X, Y (NOT null)
	
	
	// constructors
	
	/**
	 * X <: Y
	 * @param x	set X
	 * @param y	set Y
	 */
	public XSubseteqY(SetVariable x, SetVariable y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	
	// substitution methods
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * X <: Y
	 * @param x	set X
	 * @param y	set Y
	 * @return	Substituted New Constraint
	 */
	public XSubseteqY substitute(TypedSetVariable x, TypedSetVariable y) {
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
	public Constraint substitute(List<TypedSetVariable> xy) {
		if(xy.size() != substitutableSize()) {
			throw new IllegalArgumentException("The Size of tsvs must be " + substitutableSize() + ". "
					+ "(Current size is " + xy.size() + ".)");
		}
		Object[] xyArr = xy.toArray();
		return substitute((TypedSetVariable)xyArr[0], (TypedSetVariable)xyArr[1]);
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
	public int absObjSetSize() {
		return 2;
	}
	
	@Override
	public int substitutableSize() {
		return 2;
	}
	
	@Override
	public List<? extends AbsObjSet> getAllAbsObjSets() {
		ArrayList<SetVariable> abss = new ArrayList<>();
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
	
	@Override
	public String getKind() {
		return this.getClass().getSimpleName();
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