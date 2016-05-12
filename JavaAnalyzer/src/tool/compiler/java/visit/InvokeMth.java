package tool.compiler.java.visit;

import polyglot.ext.jl5.types.JL5ConstructorInstance;
import polyglot.ext.jl5.types.JL5MethodInstance;
import polyglot.ext.jl5.types.JL5ProcedureInstance;
import polyglot.types.Type;
import tool.compiler.java.util.CollUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * C{X}.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
 */
public class InvokeMth extends Constraint {
	
	// fields
	
	/* ### Abstract Fields ###
	 * Type c;						// C
	 * SetVariable x;				// X
	 * MethodInstance m;			// method name m
	 * ArrayList<Type> ds;			// D1, ... , Dn
	 * ArrayList<SetVariable> xs;	// X1, ..., Xn
	 * (effect)						// effect
	 * Type e;						// E
	 * SetVariable y;				// Y
	 */
	
	/* ### Actual Fields ### */
	private TypedSetVariable cx;				// C, X
	private JL5ProcedureInstance m;				// m
	private ArrayList<TypedSetVariable> dxs;	// Ds, Xs ( D1{X1}, ..., Dn{Xn} )
	private TypedSetVariable ey;				// E, Y
	
	
	// constructors
	
	/**
	 * C{X}.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
	 * @param cx	set C, X	( C{X} )
	 * @param m		set m
	 * @param dxs	set Ds, Xs	( D1{X1}, ..., Dn{Xn} )
	 * @param ey	set E, Y	( E{Y} )
	 */
	public InvokeMth(TypedSetVariable cx, JL5ProcedureInstance m, Collection<TypedSetVariable> dxs, TypedSetVariable ey) {
		super();
		this.cx = cx;
		this.m = m;
		if(dxs != null) {
			this.dxs = new ArrayList<TypedSetVariable>(dxs);
		} else {
			this.dxs = null;
		}
		this.ey = ey;
	}
	
	/**
	 * C{X}.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
	 * @param cx	set C, X	( C{X} )
	 * @param m		set m
	 * @param dxs	set Ds, Xs	( D1{X1}, ..., Dn{Xn} )
	 * @param ey	set E, Y	( E{Y} )
	 */
	public InvokeMth(TypedSetVariable cx, JL5MethodInstance m, Collection<TypedSetVariable> dxs, TypedSetVariable ey) {
		this(cx, (JL5ProcedureInstance) m, dxs, ey);
	}
	
	/**
	 * C{X}.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
	 * @param cx	set C, X	( C{X} )
	 * @param m		set m
	 * @param dxs	set Ds, Xs	( D1{X1}, ..., Dn{Xn} )
	 * @param ey	set E, Y	( E{Y} )
	 */
	public InvokeMth(TypedSetVariable cx, JL5ConstructorInstance m, Collection<TypedSetVariable> dxs, TypedSetVariable ey) {
		this(cx, (JL5ProcedureInstance) m, dxs, ey);
	}
	
	
	// getter methods
	
	/**
	 * @return the C{X}
	 */
	public TypedSetVariable getCX() {
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
	 * @return the m
	 */
	public JL5ProcedureInstance getM() {
		return m;
	}
	
	/**
	 * @return D1{X1}, ..., Dn{Xn}
	 */
	public List<TypedSetVariable> getDXs() {
		return dxs;
	}
	
	/**
	 * @param i	index
	 * @return Di{Xi}
	 */
	public TypedSetVariable getDX(int i) {
		return dxs.get(i);
	}
	
	/**
	 * @return the E{Y}
	 */
	public TypedSetVariable getEY() {
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
	 * Form:	C{X}.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
	 */
	@Override
	public String toString() {
		return getCX() + "." + getName()
				+ " <: " + CollUtil.getStringOf(getDXs(), '(', ')') 
				+ " -- " + "effect" + " --> "	+ getEY();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cx == null) ? 0 : cx.hashCode());
		result = prime * result + ((m == null) ? 0 : m.hashCode());
		if(dxs != null) {
			for(TypedSetVariable dx : dxs) {
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
		InvokeMth other = (InvokeMth) obj;
		if (cx == null) {
			if (other.cx != null) {
				return false;
			}
		} else if (!cx.equals(other.cx)) {
			return false;
		}
		if (m == null) {
			if (other.m != null) {
				return false;
			}
		} else if (!m.equals(other.m)) {
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
	
	
	protected final String getName() {
		return getM() instanceof JL5MethodInstance ? ((JL5MethodInstance)getM()).name() : getM().container().toString();
	}
	
	public boolean isConstructor() {
		return m instanceof JL5ConstructorInstance;
	}
}