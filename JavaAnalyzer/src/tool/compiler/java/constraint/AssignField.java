package tool.compiler.java.constraint;

import java.util.ArrayList;
import java.util.List;

import polyglot.ext.jl5.types.JL5FieldInstance;
import polyglot.types.Type;
import tool.compiler.java.aos.AbsObjSet;
import tool.compiler.java.aos.DataFlowSetVariable;
import tool.compiler.java.aos.TypedSetVariable;

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
	private DataFlowSetVariable cx;		// C, X (NOT null)
	private DataFlowSetVariable dy;		// D, Y (NOT null)
	private JL5FieldInstance f;	// f (NOT null)
	
	
	// constructor
	
	/**
	 * C{X} <: D{Y}.f
	 * @param cx	set C, X	( C{X} )
	 * @param dy	set D, Y	( D{Y} )
	 * @param f		set f
	 */
	public AssignField(DataFlowSetVariable cx, DataFlowSetVariable dy, JL5FieldInstance f) {
		super();
		this.cx = cx;
		this.dy = dy;
		this.f = f;
	}
	
	
	// substitution methods
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * C{X} <: D{Y}.f
	 * @param cx	set C, X	( C{X} )
	 * @param dy	set D, Y	( D{Y} )
	 * @return		Substituted New Constraint
	 */
	public AssignField substitute(TypedSetVariable cx, TypedSetVariable dy) {
		if(!this.cx.equalsForType(cx)) {
			throw new IllegalArgumentException("The Type Mismatch for cx. "
					+ "(orig: " + this.cx.getType() + ", subst: " + cx.getType() + ")");
		}
		
		if(!this.dy.equalsForType(dy)) {
			throw new IllegalArgumentException("The Type Mismatch for dy. "
					+ "(orig: " + this.dy.getType() + ", subst: " + dy.getType() + ")");
		}
		
		return new AssignField(cx, dy, this.f);
	}
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * C{X} <: D{Y}.f
	 * @param cxdy	C{X} and D{Y}	(The size is 2)
	 * @return		Substituted New Constraint
	 */
	@Override
	public Constraint substitute(List<TypedSetVariable> cxdy) {
		if(cxdy.size() != substitutableSize()) {
			throw new IllegalArgumentException("The Size of tsvs must be " + substitutableSize() + ". "
					+ "(Current size is " + cxdy.size() + ".)");
		}
		Object[] cxdyArr = cxdy.toArray();
		return substitute((TypedSetVariable)cxdyArr[0], (TypedSetVariable)cxdyArr[1]);
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
	 * @return the D{Y}
	 */
	public DataFlowSetVariable getDY() {
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
	
	@Override
	public String getKind() {
		return this.getClass().getSimpleName();
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