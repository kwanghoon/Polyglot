package tool.compiler.java.visit;

import polyglot.ext.jl5.types.JL5FieldInstance;
import polyglot.types.Type;

/**
 * C.f <: D{X}<br>
 * ... read a static field
 */
public class ReadStaticField implements Constraint {
	
	// fields
	
	/* ### Abstract Fields ###
	 * Type c;				// C
	 * FieldInstance f;		// f
	 * Type d;				// D
	 * SetVariable x;		// X
	 */
	
	/* ### Actual Fields ### */
	private JL5FieldInstance cf;	// C, f
	private AbsObjSet dx;	// D, X
	
	
	// constructors
	
	/**
	 * C.f <: D{X}
	 * @param cf	set C, f	( C.f )
	 * @param dx	set D, X	( D{X} )
	 */
	public ReadStaticField(JL5FieldInstance cf, AbsObjSet dx) {
		super();
		this.cf = cf;
		this.dx = dx;
	}
	
//	/**
//	 * C.f <: D{X}<br>
//	 * auto-construct AbsObjSet object ( C{X} )
//	 * @param left	for C, f	( C.f )
//	 * @param right	for D, X	( D{X} )
//	 */
//	@Deprecated
//	public ReadStaticField(JL5FieldInstance left, Type right) {
//		this(left, new AbsObjSet(right));
//	}
	
	
	// getter methods
	
	/**
	 * @return the C
	 */
	public Type getC() {
		return cf.container();
	}
	
	/**
	 * @return the f
	 */
	public JL5FieldInstance getF() {
		return cf;
	}
	
	/**
	 * @return the D{X}
	 */
	public AbsObjSet getDX() {
		return dx;
	}
	
	/**
	 * @return the D
	 */
	public Type getD() {
		return dx.getType();
	}
	
	/**
	 * @return the X
	 */
	public String getX() {
		return dx.getID();
	}
	
	
	/**
	 * Form:	C.f <: D{X}
	 */
	@Override
	public String toString() {
		return getC() + "." + getF().name() + " <: " + getDX();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cf == null) ? 0 : cf.hashCode());
		result = prime * result + ((dx == null) ? 0 : dx.hashCode());
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
		ReadStaticField other = (ReadStaticField) obj;
		if (cf == null) {
			if (other.cf != null) { 
				return false;
			}
		} else if (!cf.equals(other.cf)) {
			return false;
		}
		if (dx == null) {
			if (other.dx != null) {
				return false;
			}
		} else if (!dx.equals(other.dx)) {
			return false;
		}
		return true;
	}
}