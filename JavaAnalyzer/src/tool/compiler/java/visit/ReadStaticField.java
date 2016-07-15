package tool.compiler.java.visit;

import java.util.ArrayList;
import java.util.Collection;

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
	private AbsObjSet dx;			// D, X
	
	
	// constructor
	
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
	
	
	// substitution methods
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * C{X}.f <: D{Y}
	 * @param dx	set D, X	( D{X} )
	 * @return		Substituted New Constraint
	 */
	public ReadStaticField subst(TypedSetVariable dx) {
		if(!this.dx.equalsForType(dx)) {
			throw new IllegalArgumentException("The Type Mismatch for dx. "
					+ "(orig: " + this.dx.getType() + ", subst: " + dx.getType() + ")");
		}
		
		return new ReadStaticField(this.cf, dx);
	}
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * C{X}.f <: D{Y}
	 * @param dx	set D, X	( D{X} )	(The size is 1.)
	 * @return		Substituted New Constraint
	 */
	@Override
	public Constraint subst(Collection<TypedSetVariable> dx) {
		if(dx.size() != 1) {
			throw new IllegalArgumentException("The Size of tsvs must be 1.");
		}
		return subst(dx.iterator().next());
	}
	
	
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
	
	
	@Override
	public ArrayList<AbsObjSet> getAllAbsObjSet() {
		ArrayList<AbsObjSet> abss = new ArrayList<>();
		abss.add(dx);
		return abss;
	}
	
	@Override
	public boolean contains(AbsObjSet aos) {
		if (dx.equals(aos)) {
			return true;
		}
		return false;
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