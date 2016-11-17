package tool.compiler.java.constraint;

import java.util.ArrayList;
import java.util.List;

import polyglot.ext.jl5.types.JL5FieldInstance;
import polyglot.types.Type;
import tool.compiler.java.aos.AbsObjSet;
import tool.compiler.java.aos.DataFlowSetVariable;
import tool.compiler.java.aos.TypedSetVariable;

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
	private DataFlowSetVariable cx;			// C, X (NOT null)
	private JL5FieldInstance df;	// D, f (NOT null)
	
	
	// constructor
	
	/**
	 * C{X} <: D.f
	 * @param cx	set C, X	( C{X} )
	 * @param df	set D, f	( D.f )
	 */
	public AssignStaticField(DataFlowSetVariable cx, JL5FieldInstance df) {
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
	public AssignStaticField substitute(TypedSetVariable cx) {
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
	public Constraint substitute(List<TypedSetVariable> cx) {
		if(cx.size() != substitutableSize()) {
			throw new IllegalArgumentException("The Size of tsvs must be " + substitutableSize() + ". "
					+ "(Current size is " + cx.size() + ".)");
		}
		return substitute(cx.iterator().next());
	}
	
	
	// getter methods
	
	/**
	 * @return the C{X}
	 */
	public DataFlowSetVariable getCX() {
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
	public int absObjSetSize() {
		return 1;
	}
	
	@Override
	public int substitutableSize() {
		return 1;
	}
	
	@Override
	public List<? extends AbsObjSet> getAllAbsObjSets() {
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
	
	@Override
	public String getKind() {
		return this.getClass().getSimpleName();
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