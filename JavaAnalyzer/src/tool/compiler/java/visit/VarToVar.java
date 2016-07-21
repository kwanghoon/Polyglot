package tool.compiler.java.visit;

import java.util.ArrayList;
import java.util.Collection;

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
	private SetVariable dy;	// D, Y
	
	
	// constructor
	
	/**
	 * C{X} <: D{Y}
	 * @param cx	set C, X	( C{X} )
	 * @param dy	set D, Y	( D{Y} )
	 */
	public VarToVar(AbsObjSet cx, SetVariable dy) {
		super();
		this.cx = cx;
		this.dy = dy;
	}
	
	
	// substitution methods
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * C{X} <: D{Y}
	 * @param cx	set C, X	( C{X} )
	 * @param dy	set D, Y	( D{Y} )
	 * @return		Substituted New Constraint
	 */
	public VarToVar substitute(TypedSetVariable cx, TypedSetVariable dy) {
		if(!this.cx.equalsForType(cx)) {
			throw new IllegalArgumentException("The Type Mismatch for cx. "
					+ "(orig: " + this.cx.getType() + ", subst: " + cx.getType() + ")");
		}
		
		if(!this.dy.equalsForType(dy)) {
			throw new IllegalArgumentException("The Type Mismatch for dy. "
					+ "(orig: " + this.dy.getType() + ", subst: " + dy.getType() + ")");
		}
		
		return new VarToVar(cx, dy);
	}
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * C{X} <: D{Y}
	 * @param cxdy	C{X} and D{Y}	(The size is 2)
	 * @return		Substituted New Constraint
	 */
	@Override
	public Constraint substitute(Collection<TypedSetVariable> cxdy) {
		if(cxdy.size() != substitutableSize()) {
			throw new IllegalArgumentException("The Size of tsvs must be " + substitutableSize() + ". "
					+ "(Current size is " + cxdy.size() + ".)");
		}
		Object[] cxdyArr = cxdy.toArray();
		return substitute((TypedSetVariable) cxdyArr[0], (TypedSetVariable) cxdyArr[1]);
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
	public SetVariable getDY() {
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
	public int absObjSetSize() {
		return 2;
	}
	
	@Override
	public int substitutableSize() {
		return 2;
	}
	
	@Override
	public ArrayList<? extends AbsObjSet> getAllAbsObjSets() {
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