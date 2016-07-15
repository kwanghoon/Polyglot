package tool.compiler.java.visit;

import polyglot.ext.jl5.types.JL5MethodInstance;
import polyglot.types.Type;
import tool.compiler.java.util.CollUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
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
	private JL5MethodInstance cm;		// C, m
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
	
	
	// substitution methods
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * C.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
	 * @param dxs	set Ds, Xs	( D1{X1}, ..., Dn{Xn} )
	 * @param ey	set E, Y	( E{Y} )
	 * @return		Substituted New Constraint
	 */
	public InvokeStaticMth subst(Collection<TypedSetVariable> dxs, TypedSetVariable ey) {
		if(this.dxs.size() != dxs.size()) {
			throw new IllegalArgumentException("The Size Mismatch for dxs.");
		}
		
		int i = 0;
		for(TypedSetVariable dx : dxs) {
			AbsObjSet thisdx = this.dxs.get(i);
			if(!thisdx.equalsForType(dx)) {
				throw new IllegalArgumentException("The Type Mismatch for dx" + ++i + ". "
						+ "(orig: " + thisdx.getType() + ", subst: " + dx.getType() + ")");
			}
			i++;
		}
		
		if(!this.ey.equalsForType(ey)) {
			throw new IllegalArgumentException("The Type Mismatch for ey. "
					+ "(orig: " + this.ey.getType() + ", subst: " + ey.getType() + ")");
		}
		
		return new InvokeStaticMth(this.cm, dxs, ey);
	}
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * C{X}.f <: D{Y}
	 * @param dxsey	C{X} and D{Y}	(The size is 2)
	 * @return		Substituted New Constraint
	 */
	@Override
	public Constraint subst(Collection<TypedSetVariable> dxsey) {
		int size = 1 + this.dxs.size();
		if(dxsey.size() != size) {
			throw new IllegalArgumentException("The Size of tsvs must be " + size + ".");
		}
		LinkedList<TypedSetVariable> dxs = new LinkedList<>(dxsey);
		TypedSetVariable ey = dxs.removeLast();
		return subst(dxs, ey);
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
	
	
	@Override
	public ArrayList<AbsObjSet> getAllAbsObjSet() {
		ArrayList<AbsObjSet> abss = new ArrayList<>();
		abss.addAll(dxs);
		abss.add(ey);
		return abss;
	}
	
	@Override
	public boolean contains(AbsObjSet aos) {
		if (dxs.contains(aos)) {
			return true;
		}
		if (ey.equals(aos)) {
			return true;
		}
		return false;
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