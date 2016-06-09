package tool.compiler.java.visit;

import polyglot.ext.jl5.types.JL5FieldInstance;
import polyglot.types.Type;

/**
 * C{X} <: D{Y}.f<br>
 * ... assign to an instance field
 */
public class AssignField implements Constraint {
	
	// fields
	
	/* ### Abstract Fields ###
	 * Type c;				// C
	 * SetVariable x;		// X
	 * Type d;				// D
	 * SetVariable y;		// Y
	 * FieldInstance f;		// f
	 */
	
	/* ### Actual Fields ### */
	private AbsObjSet cx;	// C, X
	private AbsObjSet dy;	// D, Y
	private JL5FieldInstance f;		// f
	
	
	// constructors
	
	/**
	 * C{X} <: D{Y}.f
	 * @param cx	set C, X	( C{X} )
	 * @param dy	set D, Y	( D{Y} )
	 * @param f		set f
	 */
	public AssignField(AbsObjSet cx, AbsObjSet dy, JL5FieldInstance f) {
		super();
		this.cx = cx;
		this.dy = dy;
		this.f = f;
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
	 * @return the f
	 */
	public JL5FieldInstance getF() {
		return f;
	}
	
	
	/**
	 * Form:	C{X} <: D{Y}
	 */
	@Override
	public String toString() {
		return getCX() + " <: " + getDY() + "." + getF().name();
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
		result = prime * result + ((f == null) ? 0 : f.hashCode());
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
		AssignField other = (AssignField) obj;
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
		if (f == null) {
			if (other.f != null) {
				return false;
			}
		} else if (!f.equals(other.f)) {
			return false;
		}
		return true;
	}
}