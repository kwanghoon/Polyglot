package tool.compiler.java.constraint;

import java.util.ArrayList;
import java.util.List;

import tool.compiler.java.aos.AbsObjSet;
import tool.compiler.java.aos.DataFlowSetVariable;
import tool.compiler.java.aos.TypedSetVariable;
import tool.compiler.java.effect.EffectSetVariable;

/**
 * Target(X) <: Y<br>
 * X는 Intent 객체의 집합
 *  e.g. X = { Intent { Target = MainActivity, ... }, ..., 
 *             Intent { Target = MenuActivity, ... } }
 * 
 * Target(X)는 각 Intents 안에 있는 타겟들의 집합
 *  e.g. Target(X) = { MainActivity, ... , MenuActivity }
 * 
 * Y는 Target(X)를 포함한다.
 * for android activity effect
 */
public class ReadTarget implements Constraint {
	
	// fields
	
	private DataFlowSetVariable x;	// X (NOT null) Intent 파라메터에 대한 MSV
	private EffectSetVariable y;	// Y (NOT null) Fresh하게 생성
	
	
	// constructor
	
	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public ReadTarget(DataFlowSetVariable x, EffectSetVariable y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	
	// substitution methods
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * Target(X) <: Y
	 * @param x	set X
	 * @return	Substituted New Constraint
	 */
	public ReadTarget substitute(TypedSetVariable x) {
		if(!this.x.equalsForType(x)) {
			throw new IllegalArgumentException("The Type Mismatch for x. "
					+ "(orig: " + this.x.getType() + ", subst: " + x.getType() + ")");
		}
		
		return new ReadTarget(x, this.y);
	}
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * Target(X) <: Y
	 * @param xy	X and Y	(The size is 2)
	 * @return		Substituted New Constraint
	 */
	@Override
	public Constraint substitute(List<TypedSetVariable> xy) {
		if(xy.size() != substitutableSize()) {
			throw new IllegalArgumentException("The Size of tsvs must be " + substitutableSize() + ". "
					+ "(Current size is " + xy.size() + ".)");
		}
		Object[] xyzArr = xy.toArray();
		return substitute((TypedSetVariable)xyzArr[0]);
	}
	
	
	// getter methods
	
	/**
	 * @return the x
	 */
	public DataFlowSetVariable getX() {
		return x;
	}
	
	/**
	 * @return the y
	 */
	public EffectSetVariable getY() {
		return y;
	}
	
	
	/**
	 * @see tool.compiler.java.constraint.Constraint#absObjSetSize()
	 */
	@Override
	public int absObjSetSize() {
		return 1;
	}
	
	/**
	 * @see tool.compiler.java.constraint.Constraint#substitutableSize()
	 */
	@Override
	public int substitutableSize() {
		return 1;
	}
	
	/**
	 * @see tool.compiler.java.constraint.Constraint#getAllAbsObjSets()
	 */
	@Override
	public List<? extends AbsObjSet> getAllAbsObjSets() {
		ArrayList<DataFlowSetVariable> abss = new ArrayList<>();
		abss.add(x);
		return abss;
	}
	
	/**
	 * @see tool.compiler.java.constraint.Constraint#contains(tool.compiler.java.aos.AbsObjSet)
	 */
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
	
	/**
	 * @see tool.compiler.java.constraint.Constraint#getKind()
	 */
	@Override
	public String getKind() {
		return this.getClass().getSimpleName();
	}
	
	/**
	 * Form:	Target(C{X}) <: D{Y}
	 */
	@Override
	public String toString() {
		return "Target(" + getX() + ") <: " + getY();
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
		ReadTarget other = (ReadTarget) obj;
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