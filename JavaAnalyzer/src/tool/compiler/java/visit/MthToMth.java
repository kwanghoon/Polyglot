package tool.compiler.java.visit;

import polyglot.types.Type;
import tool.compiler.java.util.CollUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * (C1{X1}, ..., Cn{Xn}) -- effect1 --> E1{X} 
 * 	<: (D1{Y1}, ..., Dn{Ym}) -- effect2 --> E2{Y}<br>
 * 	(Not mandatory)
 */
public class MthToMth implements Constraint {
	
	// fields
	
	/* ### Abstract Fields ###
	 * ArrayList<Type> cs;			// C1, ... , Cn
	 * ArrayList<SetVariable> xs;	// X1, ..., Xn
	 * (effect1)					// effect1
	 * Type e1;						// E1
	 * SetVariable x;				// X
	 * 
	 * ArrayList<Type> ds;			// D1, ... , Dn
	 * ArrayList<SetVariable> ys;	// Y1, ..., Yn
	 * (effect2)					// effect2
	 * Type e2;						// E2
	 * SetVariable y;				// Y
	 */
	
	/* ### Actual Fields ### */
	private ArrayList<AbsObjSet> cxs;	// Cs, Xs ( C1{X1}, ..., Cn{Xn} )
	private AbsObjSet e1x;				// E1, X
	private ArrayList<AbsObjSet> dys;	// Ds, Ys ( D1{Y1}, ..., Dn{Yn} )
	private AbsObjSet e2y;				// E2, Y
	
	
	// constructor
	
	/**
	 * (C1{X1}, ..., Cn{Xn}) -- effect1 --> E1{X} 
	 * 	<: (D1{Y1}, ..., Dn{Ym}) -- effect2 --> E2{Y}
	 * @param cxs	set Cs, Xs	( C1{X1}, ..., Cn{Xn} )
	 * @param e1x	set E1, X	( E1{X} )
	 * @param dys	set Ds, Ys	( D1{Y1}, ..., Dn{Yn} )
	 * @param e2y	set E2, Y	( E2{Y} )
	 */
	public MthToMth(Collection<AbsObjSet> cxs, AbsObjSet e1x, Collection<AbsObjSet> dys, AbsObjSet e2y) {
		super();
		try {
			this.cxs = new ArrayList<AbsObjSet>(cxs);
		} catch (NullPointerException e) {
			this.cxs = null;
		}
		this.e1x = e1x;
		try {
			this.dys = new ArrayList<AbsObjSet>(dys);
		} catch (NullPointerException e) {
			this.dys = null;
		}
		this.e2y = e2y;
	}
	
	
	// getter methods
	
	/**
	 * @return C1{X1}, ..., Cn{Xn}
	 */
	public List<AbsObjSet> getCXs() {
		return new ArrayList<AbsObjSet>(cxs);
	}
	
	/**
	 * @param i	index
	 * @return Ci{Xi}
	 */
	public AbsObjSet getCX(int i) {
		return cxs.get(i);
	}
	
	/**
	 * @return the E1{X}
	 */
	public AbsObjSet getE1X() {
		return e1x;
	}
	
	/**
	 * @return the E1
	 */
	public Type getE1() {
		return e1x.getType();
	}
	
	/**
	 * @return the X
	 */
	public String getX() {
		return e1x.getID();
	}
	
	/**
	 * @return D1{Y1}, ..., Dn{Yn}
	 */
	public List<AbsObjSet> getDYs() {
		return new ArrayList<AbsObjSet>(dys);
	}
	
	/**
	 * @param i	index
	 * @return Di{Yi}
	 */
	public AbsObjSet getDY(int i) {
		return dys.get(i);
	}
	
	/**
	 * @return the E2{Y}
	 */
	public AbsObjSet getE2Y() {
		return e2y;
	}
	
	/**
	 * @return the E2
	 */
	public Type getE2() {
		return e2y.getType();
	}
	
	/**
	 * @return the Y
	 */
	public String getY() {
		return e2y.getID();
	}
	
	
	/**
	 * Form:	(C1{X1}, ..., Cn{Xn}) -- effect1 --> E1{X} 
	 * 				<: (D1{Y1}, ..., Dn{Ym}) -- effect2 --> E2{Y}
	 */
	@Override
	public String toString() {
		return CollUtil.getStringOf(getCXs(), '{', '}') + " -- " + "effect1" + " --> "	+ getE1X() 
				+ " <: " + CollUtil.getStringOf(getDYs(), '{', '}') + " -- " + "effect2" + " --> "	+ getE2Y();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		try {
			for(AbsObjSet cx : cxs) {
				result = prime * result + ((cx == null) ? 0 : cx.hashCode());
			}
		} catch (NullPointerException ignored) {}
		result = prime * result + ((e1x == null) ? 0 : e1x.hashCode());
		try {
			for(AbsObjSet dy : dys) {
				result = prime * result + ((dy == null) ? 0 : dy.hashCode());
			}
		} catch (NullPointerException ignored) {}
		result = prime * result + ((e2y == null) ? 0 : e2y.hashCode());
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
		MthToMth other = (MthToMth) obj;
		if (cxs == null) {
			if (other.cxs != null) {
				return false;
			}
		} else if (!cxs.equals(other.cxs)) {
			return false;
		}
		if (dys == null) {
			if (other.dys != null) {
				return false;
			}
		} else if (!dys.equals(other.dys)) {
			return false;
		}
		if (e1x == null) {
			if (other.e1x != null) {
				return false;
			}
		} else if (!e1x.equals(other.e1x)) {
			return false;
		}
		if (e2y == null) {
			if (other.e2y != null) {
				return false;
			}
		} else if (!e2y.equals(other.e2y)) {
			return false;
		}
		return true;
	}
}