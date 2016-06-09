package tool.compiler.java.visit;

import polyglot.types.Type;

/**
 * C{X} <: D{Y}<br>
 * ... variable-to-variable assign  (Not mandatory)
 */
public class VarToVar implements Constraint {
	
	// fields
	
	/* ### Abstract Fields ###
	 * Type c;			// C
	 * SetVariable x;	// X
	 * Type d;			// D
	 * SetVariable y;	// Y
	 */
	
	/* ### Actual Fields ### */
	private AbsObjSet cx;	// C, X
	private AbsObjSet dy;	// D, Y
	
	
	// constructor
	
	/**
	 * C{X} <: D{Y}
	 * @param cx	set C, X	( C{X} )
	 * @param dy	set D, Y	( D{Y} )
	 */
	public VarToVar(AbsObjSet cx, AbsObjSet dy) {
		super();
		this.cx = cx;
		this.dy = dy;
	}
	
	
	// getter methods
	
	/**
	 * @return the C{X}
	 */
	public AbsObjSet getCX() {
		return cx;
	}
	
	/**
	 * @return the C
	 */
	public Type getC() {
		return cx.getType();
	}
	
	/**
	 * @return the X
	 */
	public String getX() {
		return cx.getID();
	}
	
	/**
	 * @return the D{Y}
	 */
	public AbsObjSet getDY() {
		return dy;
	}
	
	/**
	 * @return the D
	 */
	public Type getD() {
		return dy.getType();
	}
	
	/**
	 * @return the Y
	 */
	public String getY() {
		return dy.getID();
	}
	
	
	/**
	 * Form:	C{X} <: D{Y}
	 */
	@Override
	public String toString() {
		return getCX() + " <: " + getDY();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cx == null) ? 0 : cx.hashCode());
		result = prime * result + ((dy == null) ? 0 : dy.hashCode());
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
		VarToVar other = (VarToVar) obj;
		if (cx == null) {
			if (other.cx != null) {
				return false;
			}
		} else if (!cx.equals(other.cx)) {
			return false;
		}
		if (dy == null) {
			if (other.dy != null) {
				return false;
			}
		} else if (!dy.equals(other.dy)) {
			return false;
		}
		return true;
	}
}