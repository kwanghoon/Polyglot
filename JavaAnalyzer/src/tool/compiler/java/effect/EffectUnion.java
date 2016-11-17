package tool.compiler.java.effect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarGoal;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;

/**
 * X ∪ Y <: Z		(Z is this)
 */
public class EffectUnion extends EffectSetVariable {
	
	// fields
	
	private EffectSetVariable x, y;	// X, Y (NOT null)
	
	
	// constructor
	
	/**
	 * @param x
	 * @param y
	 */
	public EffectUnion(EffectSetVariable x, EffectSetVariable y) {
		super(check(x, y));
		this.x = x;
		this.y = y;
	}
	
	private static EffectName check(EffectSetVariable x, EffectSetVariable y) {
		if (x.getEffectType().equals(y.getEffectType())){
			return x.getEffectType();
		} else {
			throw new IllegalArgumentException("Effect Type NOT match.");
		}
	}
	
	
	// Utilities
	
	/**
	 * EffectSetVariables를 합집합한다.
	 * @param x
	 * @param y
	 * @return	 EffectUnion
	 */
	public static final EffectSetVariable unionize(EffectSetVariable x, EffectSetVariable y) {
		final boolean xNotNull = x != null;
		final boolean yNotNull = y != null;
		if (xNotNull && yNotNull) {
			return new EffectUnion(x, y);
		} else if (xNotNull) {
			return x;
		} else if (yNotNull) {
			return y;
		}
		return null;
	}
	
	/**
	 * EffectSetVariable Collection을 합집합한다.
	 * @param effects		합집합할 effects
	 * @return	effects의 개수가 1을 초과하는 경우 EffectUnion, 1인 경우 EffectSetVariable, 
	 */
	public static final EffectSetVariable unionize(Collection<? extends EffectSetVariable> effects) {
		EffectSetVariable effect = null;
		for (EffectSetVariable curr : effects) {
			if (effect != null) {
				if (effect != curr) {	// 이전 effect에 대한 Report (결과 effect에 대해서는 메서드 밖에서 Report할 것)
					ReportUtil.report(effect, EffectSetVarSource.New, EffectSetVarGoal.Flow);
				}
				effect = new EffectUnion(effect, curr);
			} else {
				effect = curr;
			}
		}
		return effect;
	}
	
	
	// getter methods
	
	/**
	 * @return the x
	 */
	public EffectSetVariable getX() {
		return x;
	}
	
	/**
	 * @return the y
	 */
	public EffectSetVariable getY() {
		return y;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<? extends EffectSetVariable> getAllElements() {
		ArrayList<EffectSetVariable> elements = new ArrayList<>();
		getAllElements(elements);
		return elements;
	}
	
	/**
	 * only leaf nodes traversal
	 * @param elements
	 */
	private void getAllElements(ArrayList<EffectSetVariable> elements) {
		if (this.x instanceof EffectUnion) {
			((EffectUnion) this.x).getAllElements(elements);
		} else {
			elements.add(this.x);
		}
		if (this.y instanceof EffectUnion) {
			((EffectUnion) this.y).getAllElements(elements);
		} else {
			elements.add(this.y);
		}
	}
	
	
	public List<? extends EffectSetVariable> getAllEffectSetVariables() {
		ArrayList<EffectSetVariable> abss = new ArrayList<>();
		abss.add(x);
		abss.add(y);
		return abss;
	}
	
	public boolean contains(EffectSetVariable esv) {
		if (x.equals(esv)) {
			return true;
		}
		if (y.equals(esv)) {
			return true;
		}
		return false;
	}
	
	/**
	 * @see tool.compiler.java.constraint.Constraint#getKind()
	 */
	public String getKind() {
		return this.getClass().getSimpleName();
	}
	
	/**
	 * Form:	Effect{X} ∪ Effect{Y} <: Effect{Z}	(Z is this)
	 */
	@Override
	public String toString() {
		return getX() + " ∪ " + getY() + " <: " + super.toString();
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
		EffectUnion other = (EffectUnion) obj;
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