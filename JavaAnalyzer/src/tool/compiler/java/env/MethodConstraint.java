package tool.compiler.java.env;

import polyglot.ext.jl5.types.JL5ConstructorInstance;
import polyglot.ext.jl5.types.JL5MethodInstance;
import polyglot.ext.jl5.types.JL5ProcedureInstance;
import polyglot.ext.jl5.types.TypeVariable;
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
import java.util.List;

/**
 * lam (Y1, ..., Yl, Chi1, ..., Chin, Chi_ret). Constraint Set
 */
public class MethodConstraint extends CodeConstraint {
	
	private ArrayList<MetaSetVariable> chi_formals = null;
	private MetaSetVariable chi_ret = null;
	
	/**
	 * @param m
	 */
	public MethodConstraint(JL5ProcedureInstance m) {
		super(m);
		if (m instanceof JL5MethodInstance) {
			this.chi_ret = new MetaSetVariable(((JL5MethodInstance) m).returnType());
		} else/* if (m instanceof JL5ConstructorInstance)*/ {	//JL5ConstructorInstance
			this.chi_ret = null;
		}
	}
	
	
	// TODO: 제네릭 메서드 대응 필요: 현재 구현은, 타입 변수의 경우에 일치하는 타입이 없을 것이므로, subst 되지 않는 경우가 발생할 것 (제네릭 클래스는?)
	public ConstraintsPair apply(Collection<TypedSetVariable> XFormals) {
		
		// 앞에서 만든 X1~Xn과 X_e1~X_en을 자료흐름 관계를 제약식 집합 CS1으로 만든다.
		ArrayList<XSubseteqY> cs1 = new ArrayList<>();
		if (chi_formals != null && XFormals != null) {
			if (chi_formals.size() != XFormals.size()) {
				throw new IllegalArgumentException("XFormals size must be " + chi_formals.size() + ".");
			}
			Iterator<TypedSetVariable> iterator = XFormals.iterator();
			for (MetaSetVariable msvFormal : chi_formals) {
				TypedSetVariable tsvFormal = new TypedSetVariable(msvFormal.getType());
				cs1.add(new XSubseteqY(tsvFormal, iterator.next()));
			}
		}
		
		// 메소드 m을 실행할 때 생기는 자료흐름 CS2를 만든다.
		ArrayList<Constraint> cs2 = new ArrayList<>();
		HashMap<MetaSetVariable, TypedSetVariable> substLocals = new HashMap<>();
		LinkedHashSet<? extends Constraint> metaConstraints = getMetaConstraints();
		if (metaConstraints != null) {
			for (Constraint metaCon : metaConstraints) { // 가지고 있는 전체 제약식에 대해
				ArrayList<TypedSetVariable> substs = new ArrayList<>(); // subst한 aos
				// MetaSetVariable을 TypedSetVariable로 대치
				for (AbsObjSet aos : metaCon.getAllAbsObjSets()) {
					if (aos instanceof MetaSetVariable) {
						// Formal의 Chi인지 확인
						int pos = -1;
						if (chi_formals != null) {
							pos = chi_formals.indexOf(aos);
						}
						if (pos != -1) { // Formal의 Chi이면 (chi_formals에 존재하면)
							substs.add((TypedSetVariable) cs1.get(pos).getX());
						} else {
							// Local의 Chi인지 확인
							TypedSetVariable tsvLocal = substLocals.get(aos);
							if (tsvLocal != null) {
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
		}
		// Return의 Chi에 대한 새로운 TypedSetVariable을 생성한다.
		TypedSetVariable x_ret = null;
		if (chi_ret != null) {
			// Formal의 Chi인지 확인
			int pos = -1;
			if (chi_formals != null) {
				pos = chi_formals.indexOf(chi_ret);
			}
			if (pos != -1) {						// Formal의 Chi이면 (chi_formals에 존재하면)
				x_ret = (TypedSetVariable) cs1.get(pos).getX();
			} else {
			// Local의 Chi인지 확인
				TypedSetVariable tsvLocal = substLocals.get(chi_ret);
				if (tsvLocal != null) {
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
	@Override
	public JL5ProcedureInstance getInstance() {
		return (JL5ProcedureInstance) super.getInstance();
	}
	
	public List<TypeVariable> getTypeParams() {
		return getInstance().typeParams();
	}
	
	/**
	 * @return the chi_formals
	 */
	public LinkedHashSet<MetaSetVariable> getFormals() {
		return new LinkedHashSet<>(chi_formals);
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
	
	public boolean isConstructor() {
		return getInstance() instanceof JL5ConstructorInstance;
	}
	
	public boolean isNormal() {
		return getInstance() instanceof JL5MethodInstance;
	}
	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		List<TypeVariable> typeParams = getTypeParams();
		ArrayList<String> subResult = new ArrayList<>();
		if (typeParams != null && !typeParams.isEmpty()) {
			subResult.add(CollUtil.getStringOf(typeParams));
		}
		if (chi_formals != null && !chi_formals.isEmpty()) {
			subResult.add(CollUtil.getStringOf(chi_formals));
		}
		if (chi_ret != null) {
			subResult.add(chi_ret.toString());
		}
		StringBuilder result = new StringBuilder();
		result.append('M').append('C').append(' ').append(getInstance()).append(':').append(' ')
			.append('λ').append('(').append(CollUtil.getStringOf(subResult)).append(')');
		return result.toString();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((chi_formals == null) ? 0 : chi_formals.hashCode());
		result = prime * result + ((chi_ret == null) ? 0 : chi_ret.hashCode());
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
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MethodConstraint other = (MethodConstraint) obj;
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