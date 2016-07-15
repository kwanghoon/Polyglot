package tool.compiler.java.visit;

import java.util.ArrayList;
import java.util.Collection;

import polyglot.ext.jl5.types.JL5FieldInstance;
import polyglot.types.Type;

/**
 * C{X}.f <: D{Y}<br>
 * ... read an instance field
 */
public class ReadField implements Constraint {
	
	// fields
	
	/* ### Abstract Fields ###
	 * Type c;				// C
	 * SetVariable x;		// X
	 * FieldInstance f;		// f
	 * Type d;				// D
	 * SetVariable y;		// Y
	 */
	
	/* ### Actual Fields ### */
	private AbsObjSet cx;		// C, X
	private JL5FieldInstance f;	// f
	private AbsObjSet dy;		// D, Y
	
	
	// constructor
	
	/**
	 * C{X}.f <: D{Y}
	 * @param cx	set C, X	( C{X} )
	 * @param f		set f
	 * @param dy	set D, Y	( D{Y} )
	 */
	public ReadField(AbsObjSet cx, JL5FieldInstance f, AbsObjSet dy) {
		super();
		this.cx = cx;
		this.f = f;
		this.dy = dy;
	}
	
	
	// substitution methods
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * C{X}.f <: D{Y}
	 * @param cx	set C, X	( C{X} )
	 * @param dy	set D, Y	( D{Y} )
	 * @return		Substituted New Constraint
	 */
	public ReadField subst(TypedSetVariable cx, TypedSetVariable dy) {
		if(!this.cx.equalsForType(cx)) {
			throw new IllegalArgumentException("The Type Mismatch for cx. "
					+ "(orig: " + this.cx.getType() + ", subst: " + cx.getType() + ")");
		}
		
		if(!this.dy.equalsForType(dy)) {
			throw new IllegalArgumentException("The Type Mismatch for dy. "
					+ "(orig: " + this.dy.getType() + ", subst: " + dy.getType() + ")");
		}
		
		return new ReadField(cx, this.f, dy);
	}
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * C{X}.f <: D{Y}
	 * @param cxdy	C{X} and D{Y}	(The size is 2)
	 * @return		Substituted New Constraint
	 */
	@Override
	public Constraint subst(Collection<TypedSetVariable> cxdy) {
		if(cxdy.size() != 2) {
			throw new IllegalArgumentException("The Size of tsvs must be 2.");
		}
		Object[] cxdyArr = cxdy.toArray();
		return subst((TypedSetVariable)cxdyArr[0], (TypedSetVariable)cxdyArr[1]);
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
	 * @return the f
	 */
	public JL5FieldInstance getF() {
		return f;
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
	
	
	@Override
	public ArrayList<AbsObjSet> getAllAbsObjSet() {
		ArrayList<AbsObjSet> abss = new ArrayList<>();
		abss.add(cx);
		abss.add(dy);
		return abss;
	}
	
	@Override
	public boolean contains(AbsObjSet aos) {
		if (cx.equals(aos)) {
			return true;
		}
		if (dy.equals(aos)) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Form:	C{X}.f <: D{Y}
	 */
	@Override
	public String toString() {
		return getCX() + "." + getF().name() + " <: " + getDY();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cx == null) ? 0 : cx.hashCode());
		result = prime * result + ((f == null) ? 0 : f.hashCode());
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
		ReadField other = (ReadField) obj;
		if (cx == null) {
			if (other.cx != null) {
				return false;
			}
		} else if (!cx.equals(other.cx)) {
			return false;
		}
		if (f == null) {
			if (other.f != null) {
				return false;
			}
		} else if (!f.equals(other.f)) {
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