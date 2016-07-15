package tool.compiler.java.visit;

import java.util.ArrayList;
import java.util.Collection;

import polyglot.ext.jl5.types.JL5FieldInstance;
import polyglot.types.Type;

/**
 * C{X} <: D.f<br>
 * ... assign to a static field
 */
public class AssignStaticField implements Constraint {
	
	// fields
	
	/* ### Abstract Fields ###
	 * Type c;				// C
	 * SetVariable x;		// X
	 * Type d;				// D
	 * FieldInstance f;		// f
	 */
	
	/* ### Actual Fields ### */
	private AbsObjSet cx;			// C, X
	private JL5FieldInstance df;	// D, f
	
	
	// constructor
	
	/**
	 * C{X} <: D.f
	 * @param cx	set C, X	( C{X} )
	 * @param df	set D, f	( D.f )
	 */
	public AssignStaticField(AbsObjSet cx, JL5FieldInstance df) {
		super();
		this.cx = cx;
		this.df = df;
	}
	
	
	// substitution methods
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * C{X} <: D.f
	 * @param cx	set C, X	( C{X} )
	 * @return		Substituted New Constraint
	 */
	public AssignStaticField subst(TypedSetVariable cx) {
		if(!this.cx.equalsForType(cx)) {
			throw new IllegalArgumentException("The Type Mismatch for cx. "
					+ "(orig: " + this.cx.getType() + ", subst: " + cx.getType() + ")");
		}
		
		return new AssignStaticField(cx, this.df);
	}
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * C{X} <: D.f
	 * @param cx	set C, X	( C{X} )	(The size is 1.)
	 * @return		Substituted New Constraint
	 */
	@Override
	public Constraint subst(Collection<TypedSetVariable> cx) {
		if(cx.size() != 1) {
			throw new IllegalArgumentException("The Size of tsvs must be 1.");
		}
		return subst(cx.iterator().next());
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
	 * @return the D
	 */
	public Type getD() {
		return df.container();
	}
	
	/**
	 * @return the f
	 */
	public JL5FieldInstance getF() {
		return df;
	}
	
	
	@Override
	public ArrayList<AbsObjSet> getAllAbsObjSet() {
		ArrayList<AbsObjSet> abss = new ArrayList<>();
		abss.add(cx);
		return abss;
	}
	
	@Override
	public boolean contains(AbsObjSet aos) {
		if (cx.equals(aos)) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Form:	C{X} <: D.f
	 */
	@Override
	public String toString() {
		return getCX() + " <: " + getD() + "." + getF().name();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cx == null) ? 0 : cx.hashCode());
		result = prime * result + ((df == null) ? 0 : df.hashCode());
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
		AssignStaticField other = (AssignStaticField) obj;
		if (cx == null) {
			if (other.cx != null) {
				return false;
			}
		} else if (!cx.equals(other.cx)) {
			return false;
		}
		if (df == null) {
			if (other.df != null) {
				return false;
			}
		} else if (!df.equals(other.df)) {
			return false;
		}
		return true;
	}
}