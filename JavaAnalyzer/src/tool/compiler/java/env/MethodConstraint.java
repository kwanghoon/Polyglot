package tool.compiler.java.env;

import polyglot.ext.jl5.types.JL5ConstructorInstance;
import polyglot.ext.jl5.types.JL5MethodInstance;
import polyglot.ext.jl5.types.JL5ProcedureInstance;
import tool.compiler.java.aos.AbsObjSet;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.aos.TypedSetVariable;
import tool.compiler.java.constraint.Constraint;
import tool.compiler.java.constraint.XSubseteqY;
import tool.compiler.java.util.CollUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * lam (Chi1, ..., Chin, Chi_ret). Constraint Set
 */
public class MethodConstraint implements ConstraintFunction {
	
	private JL5ProcedureInstance method;
	private ArrayList<MetaSetVariable> chi_formals;
	private MetaSetVariable chi_ret;
	private LinkedHashSet<Constraint> metaConstraints;
	
	/**
	 * @param 
	 */
	public MethodConstraint(JL5ProcedureInstance m) {
		this.method = m;
	}
	
	public MethodConstraint(JL5ProcedureInstance m, Collection<MetaSetVariable> chiFormals) {
		this(m);
		this.chi_formals = new ArrayList<>(chiFormals);
	}
	
	public MethodConstraint(JL5ProcedureInstance m, Collection<MetaSetVariable> chiFormals, MetaSetVariable chiReturn) {
		this(m, chiFormals);
		this.chi_ret = chiReturn;
	}
	
	
	// TODO: 제네릭 메서드 대응 필요: 현재 구현은, 타입 변수의 경우에 일치하는 타입이 없을 것이므로, subst 되지 않는 경우가 발생할 것 (제네릭 클래스는?)
	public ConstraintsPair apply(Collection<TypedSetVariable> XFormals) {
		
		// 앞에서 만든 X1~Xn과 X_e1~X_en을 자료흐름 관계를 제약식 집합 CS1으로 만든다.
		ArrayList<XSubseteqY> cs1 = new ArrayList<>();
		if(chi_formals != null && XFormals != null) {
			if(chi_formals.size() != XFormals.size()) {
				throw new IllegalArgumentException("XFormals size must be " + chi_formals.size() + ".");
			}
			Iterator<TypedSetVariable> iterator = XFormals.iterator();
			for(MetaSetVariable msvFormal : chi_formals) {
				TypedSetVariable tsvFormal = new TypedSetVariable(msvFormal.getType());
				cs1.add(new XSubseteqY(tsvFormal, iterator.next()));
			}
		}
		
		// 메소드 m을 실행할 때 생기는 자료흐름 CS2를 만든다.
		ArrayList<Constraint> cs2 = new ArrayList<>();
		HashMap<MetaSetVariable, TypedSetVariable> substLocals = new HashMap<>();
		for(Constraint metaCon : metaConstraints) {	// 가지고 있는 전체 제약식에 대해
			ArrayList<TypedSetVariable> substs = new ArrayList<>();	// subst한 aos
			// MetaSetVariable을 TypedSetVariable로 대치
			for(AbsObjSet aos : metaCon.getAllAbsObjSets()) {
				if (aos instanceof MetaSetVariable) {
					// Formal의 Chi인지 확인
					int pos = -1;
					if(chi_formals != null) {
						pos = chi_formals.indexOf(aos);
					}
					if(pos != -1) {						// Formal의 Chi이면 (chi_formals에 존재하면)
						substs.add((TypedSetVariable) cs1.get(pos).getX());
					} else {
					// Local의 Chi인지 확인
						TypedSetVariable tsvLocal = substLocals.get(aos);
						if(tsvLocal != null) {
							substs.add(tsvLocal);
						} else {
							tsvLocal = new TypedSetVariable(aos.getType());
							substLocals.put((MetaSetVariable) aos, tsvLocal);
							substs.add(tsvLocal);
						}
					}
				} else if (aos instanceof TypedSetVariable) {
					substs.add((TypedSetVariable) aos);
				}
			}
			cs2.add(metaCon.substitute(substs));
		}
		
		// Return의 Chi에 대한 새로운 TypedSetVariable을 생성한다.
		TypedSetVariable x_ret = null;
		if(chi_ret != null) {
			// Formal의 Chi인지 확인
			int pos = -1;
			if(chi_formals != null) {
				pos = chi_formals.indexOf(chi_ret);
			}
			if(pos != -1) {						// Formal의 Chi이면 (chi_formals에 존재하면)
				x_ret = (TypedSetVariable) cs1.get(pos).getX();
			} else {
			// Local의 Chi인지 확인
				TypedSetVariable tsvLocal = substLocals.get(chi_ret);
				if(tsvLocal != null) {
					x_ret = tsvLocal;
				} else {
					x_ret = new TypedSetVariable(chi_ret.getType());
				}
			}
		}
		
		// CS1 U CS2, X_ret 두가지를 리턴한다.
		LinkedHashSet<Constraint> cs = new LinkedHashSet<>();
		cs.addAll(cs1);
		cs.addAll(cs2);
		
		return new ConstraintsPair(cs, x_ret);
	}
	
	/**
	 * @return the method
	 */
	public JL5ProcedureInstance getMethod() {
		return method;
	}
	
	/**
	 * @return the chi_formals
	 */
	public LinkedHashSet<MetaSetVariable> getFormals() {
		return new LinkedHashSet<>(chi_formals);
	}
	
	/**
	 * @param chi_formals the chi_formals to set
	 */
	public void setFormals(Collection<MetaSetVariable> chi_formals) {
		if(this.chi_formals == null) {
			this.chi_formals = new ArrayList<MetaSetVariable>(chi_formals);
		} else {
			this.chi_formals.clear();
			this.chi_formals.addAll(chi_formals);
		}
	}
	
	/**
	 * @param chi_formals the chi_formals to add
	 */
	public void addFormals(Collection<MetaSetVariable> chi_formals) {
		if(this.chi_formals == null) {
			this.chi_formals = new ArrayList<MetaSetVariable>(chi_formals);
		} else {
			this.chi_formals.addAll(chi_formals);
		}
	}
	
	/**
	 * @param chi_formal
	 */
	public void addFormal(MetaSetVariable chi_formal) {
		try {
			this.chi_formals.add(chi_formal);
		} catch (NullPointerException e) {
			this.chi_formals = new ArrayList<>();
			this.chi_formals.add(chi_formal);
		}
	}
	
	/**
	 * @return the chi_ret
	 */
	public MetaSetVariable getReturn() {
		return chi_ret;
	}
	
	/**
	 * @param chiRet the chi_ret to set
	 */
	public void setReturn(MetaSetVariable chi_return) {
		this.chi_ret = chi_return;
	}
	
	/**
	 * @return the metaConstraints
	 */
	public LinkedHashSet<? extends Constraint> getMetaConstraints() {
		return new LinkedHashSet<>(metaConstraints);
	}
	
	/**
	 * @param metaConstraints the metaConstraints to set
	 */
	public void setMetaConstraints(Collection<? extends Constraint> metaConstraints) {
		if(this.metaConstraints == null) {
			this.metaConstraints = new LinkedHashSet<>(metaConstraints);
		} else {
			this.metaConstraints.clear();
			this.metaConstraints.addAll(metaConstraints);
		}
	}
	
	/**
	 * @param metaConstraints the metaConstraints to add
	 */
	public void addMetaConstraints(Collection<? extends Constraint> metaConstraints) {
		if(this.metaConstraints == null) {
			this.metaConstraints = new LinkedHashSet<>(metaConstraints);
		} else {
			this.metaConstraints.addAll(metaConstraints);
		}
	}
	
	/**
	 * @param metaConstraint the metaConstraint to add
	 */
	public void addMetaConstraint(Constraint metaConstraint) {
		try {
			this.metaConstraints.add(metaConstraint);
		} catch (NullPointerException e) {
			this.metaConstraints = new LinkedHashSet<>();
			this.metaConstraints.add(metaConstraint);
		}
	}
	
	public boolean isConstructor() {
		return method instanceof JL5ConstructorInstance;
	}
	
	public boolean isNormal() {
		return method instanceof JL5MethodInstance;
	}
	
	@Override
	public String getKind() {
		return this.getClass().getSimpleName();
	}
	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result =  "MC " + method + ": λ(";
		result += chi_formals != null ? CollUtil.getStringOf(chi_formals) : "";
		result += chi_ret != null ? (chi_formals != null ? ", " : "") + chi_ret : "";
		result += ")";
		
		return result;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((chi_formals == null) ? 0 : chi_formals.hashCode());
		result = prime * result + ((chi_ret == null) ? 0 : chi_ret.hashCode());
		result = prime * result + ((metaConstraints == null) ? 0 : metaConstraints.hashCode());
		
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
		MethodConstraint other = (MethodConstraint) obj;
		if (method == null) {
			if (other.method != null) {
				return false;
			}
		} else if (!method.equals(other.method)) {
			return false;
		}
		if (chi_formals == null) {
			if (other.chi_formals != null) {
				return false;
			}
		} else if (!chi_formals.equals(other.chi_formals)) {
			return false;
		}
		if (chi_ret == null) {
			if (other.chi_ret != null) {
				return false;
			}
		} else if (!chi_ret.equals(other.chi_ret)) {
			return false;
		}
		if (metaConstraints == null) {
			if (other.metaConstraints != null) {
				return false;
			}
		} else if (!metaConstraints.equals(other.metaConstraints)) {
			return false;
		}
		return true;
	}
	
	public static class ConstraintsPair {
		
		private Collection<? extends Constraint> cs;
		private TypedSetVariable xret;
		
		/**
		 * @param cs
		 * @param xret
		 */
		protected ConstraintsPair(Collection<? extends Constraint> cs, TypedSetVariable xret) {
			super();
			this.cs = cs;
			this.xret = xret;
		}
		
		/**
		 * @return the cs
		 */
		public Collection<? extends Constraint> getCS() {
			return cs;
		}
		
		/**
		 * @return the xret
		 */
		public TypedSetVariable getXret() {
			return xret;
		}
	}
}