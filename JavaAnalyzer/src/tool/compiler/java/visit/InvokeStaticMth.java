package tool.compiler.java.visit;

import polyglot.ext.jl5.types.JL5MethodInstance;
import polyglot.types.Type;
import tool.compiler.java.util.CollUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * C.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
 */
public class InvokeStaticMth implements Constraint {
	
	// fields
	
	/* ### Abstract Fields ###
	 * Type c;						// C
	 * MethodInstance m;			// method name m
	 * ArrayList<Type> ds;			// D1, ... , Dn
	 * ArrayList<SetVariable> xs;	// X1, ..., Xn
	 * (effect)						// effect
	 * Type e;						// E
	 * SetVariable y;				// Y
	 */
	
	/* ### Actual Fields ### */
	private JL5MethodInstance cm;				// C, m
	private ArrayList<AbsObjSet> dxs;	// Ds, Xs ( D1{X1}, ..., Dn{Xn} )
	private AbsObjSet ey;				// E, Y
	
	
	// constructor
	
	/**
	 * C.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
	 * @param cm	set C, m	( C.m )
	 * @param dxs	set Ds, Xs	( D1{X1}, ..., Dn{Xn} )
	 * @param ey	set E, Y	( E{Y} )
	 */
	public InvokeStaticMth(JL5MethodInstance cm, Collection<? extends AbsObjSet> dxs, AbsObjSet ey) {
		super();
		this.cm = cm;
		if(dxs != null) {
			this.dxs = new ArrayList<AbsObjSet>(dxs);
		} else {
			this.dxs = null;
		}
		this.ey = ey;
	}
	
	
	// getter methods
	
	/**
	 * @return the C
	 */
	public Type getC() {
		return cm.container();
	}
	
	/**
	 * @return the m
	 */
	public JL5MethodInstance getM() {
		return cm;
	}
	
	/**
	 * @return D1{X1}, ..., Dn{Xn}
	 */
	public List<AbsObjSet> getDXs() {
		return new ArrayList<AbsObjSet>(dxs);
	}
	
	/**
	 * @param i	index
	 * @return Di{Xi}
	 */
	public AbsObjSet getDX(int i) {
		return dxs.get(i);
	}
	
	/**
	 * @return the E{Y}
	 */
	public AbsObjSet getEY() {
		return ey;
	}
	
	/**
	 * @return the E
	 */
	public Type getE() {
		return ey.getType();
	}
	
	/**
	 * @return the Y
	 */
	public String getY() {
		return ey.getID();
	}
	
	
	/**
	 * Form:	C.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
	 */
	@Override
	public String toString() {
		return getC() + "." + getM().name() + " <: " + CollUtil.getStringOf(getDXs(), '(', ')') 
				+ " -- " + "effect" + " --> "	+ getEY();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cm == null) ? 0 : cm.hashCode());
		if(dxs != null) {
			for(AbsObjSet dx : dxs) {
				result = prime * result + ((dx == null) ? 0 : dx.hashCode());
			}
		}
		result = prime * result + ((ey == null) ? 0 : ey.hashCode());
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
		InvokeStaticMth other = (InvokeStaticMth) obj;
		if (cm == null) {
			if (other.cm != null) {
				return false;
			}
		} else if (!cm.equals(other.cm)) {
			return false;
		}
		if (dxs == null) {
			if (other.dxs != null) {
				return false;
			}
		} else if (!dxs.equals(other.dxs)) {
			return false;
		}
		if (ey == null) {
			if (other.ey != null) {
				return false;
			}
		} else if (!ey.equals(other.ey)) {
			return false;
		}
		return true;
	}
}