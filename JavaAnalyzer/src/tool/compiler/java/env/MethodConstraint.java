package tool.compiler.java.env;

import polyglot.ext.jl5.types.JL5ConstructorInstance;
import polyglot.ext.jl5.types.JL5MethodInstance;
import polyglot.ext.jl5.types.JL5ProcedureInstance;
import polyglot.ext.jl5.types.TypeVariable;
import polyglot.types.Type;
import polyglot.util.Pair;
import tool.compiler.java.aos.AbsObjSet;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.aos.TypedSetVariable;
import tool.compiler.java.constraint.Constraint;
import tool.compiler.java.constraint.XSubseteqY;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.util.CollUtil;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * lam (Y1, ..., Yl, Chi1, ..., Chin, Chi_ret). Constraint Set
 */
public class MethodConstraint extends CodeConstraint {
	
	private MetaSetVariable chi_ret = null;
	private ArrayList<MetaSetVariable> chi_formals = null;
	private HashMap<EffectName, EffectSetVariable> effects = null;
	
	/**
	 * @param m
	 */
	public MethodConstraint(JL5ProcedureInstance m) {
		super(m);
		generateReturn(m);
		generateFormals(m);
//		generateExceptions(m);
	}
	
	private void generateReturn(JL5ProcedureInstance m) {
		if (m instanceof JL5MethodInstance) {	// return type이 void인 경우에도 MetaSetVariable을 생성함 (단, ID는 없음)
			this.chi_ret = MetaSetVariable.create(((JL5MethodInstance) m).returnType());
			ReportUtil.report(chi_ret, MetaSetVarSource.New, MetaSetVarGoal.MethodEnvironment);
		} else/* if (m instanceof JL5ConstructorInstance)*/ {	// JL5ConstructorInstance
			this.chi_ret = null;
		}
	}
	
	private void generateFormals(JL5ProcedureInstance m) {
		List<? extends Type> formals = m.formalTypes();
		if (!formals.isEmpty()) {
			this.chi_formals = new ArrayList<>();
			for (Type type : formals) {
				MetaSetVariable chi_formal = MetaSetVariable.create(type);
				this.chi_formals.add(chi_formal);	// formal
				ReportUtil.report(chi_formal, MetaSetVarSource.New, MetaSetVarGoal.MethodEnvironment);
			}
		} else {
			this.chi_formals = null;
		}
	}
	
//	private void generateExceptions(JL5ProcedureInstance m) {
//		if (!m.throwTypes().isEmpty()) {
//			this.effects = new HashMap<>();	// TODO: Activity Effect를 커버하려면 언제 이 객체를 생성하는 것이 좋은가?
//			EffectVariable chi_exnEffect = new EffectVariable(EffectName.ExnEff);
//			this.effects.put(EffectName.ExnEff, chi_exnEffect);
//			ReportUtil.report(chi_exnEffect, EffectSetVarSource.New, EffectSetVarGoal.MethodEnvironment);
//		}
//	}
	
	
	// TODO: 제네릭 메서드 대응 필요: 현재 구현은, 타입 변수의 경우에 일치하는 타입이 없을 것이므로, subst 되지 않는 경우가 발생할 것 (제네릭 클래스는?)
	public Pair<Collection<? extends Constraint>, TypedSetVariable> apply(Collection<TypedSetVariable> XFormals) {
		
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
		
		return new Pair<Collection<? extends Constraint>, TypedSetVariable>(cs, x_ret);
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
		try {
			return new LinkedHashSet<>(chi_formals);
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @return the chi_formal
	 */
	public MetaSetVariable getFormal(int i) {
		try {
			return chi_formals.get(i);
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @return the chi_ret
	 */
	public MetaSetVariable getReturn() {
		return chi_ret;
	}
	
	/**
	 * @return the Exception Effect
	 */
	public EffectSetVariable getException() {
		return getEffect(EffectName.ExnEff);
	}
	
	/**
	 * @param exceptionEffect the Exception Effect to set
	 */
	public void setException(EffectSetVariable exceptionEffect) {
		try {
			setEffect(EffectName.ExnEff, exceptionEffect);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("The exceptionEffect's type must be 'EffectName.ExnEff'.");
		}
	}
	
	/**
	 * @return the Activity Effect
	 */
	public EffectSetVariable getActivityEffect() {
		return getEffect(EffectName.ActivityEff);
	}
	
	/**
	 * @param activityEffect the Activity Effect to set
	 */
	public void setActivityEffect(EffectSetVariable activityEffect) {
		try {
			setEffect(EffectName.ActivityEff, activityEffect);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("The activityEffect's type must be 'EffectName.ActivityEff'.");
		}
	}
	
	/**
	 * @return the effect
	 */
	public EffectSetVariable getEffect(EffectName type) {
		try {
			return effects.get(type);
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @param effect the Effect to set
	 */
	public void setEffect(EffectName type, EffectSetVariable effect) {
		if (effect != null) {
			if (effect.getEffectType().equals(type)) {
				if (effects == null) {
					effects = new HashMap<>();
				}
				effects.put(type, effect);
			} else {
				throw new IllegalArgumentException("type mismatch");
			}
		} else if (effects != null) {
			effects.remove(type);
		}
	}
	
	/**
	 * @param effect the Effect to add
	 */
	public void addEffect(EffectSetVariable effect) {
		if (effect != null) {
			if (effects == null) {
				effects = new HashMap<>();
			}
			effects.put(effect.getEffectType(), effect);
		}
	}
	
	/**
	 * @return the effects
	 */
	public LinkedHashMap<EffectName, EffectSetVariable> getEffectMap() {
		if (this.effects != null && !this.effects.isEmpty()) {
			LinkedHashMap<EffectName, EffectSetVariable> effectMap = new LinkedHashMap<>();
			for (EffectName type : EffectName.values()) {
				EffectSetVariable effect = this.effects.get(type);
				if (effect != null) {
					effectMap.put(type, effect);
				}
			}
			return effectMap;
		} else {
			return null;
		}
	}
	
	public void addEffects(Collection<EffectSetVariable> effects) {
		if (effects != null && !effects.isEmpty()) {
			if (this.effects == null) {
				this.effects = new HashMap<>();
			}
			for (EffectSetVariable effect : effects) {
				this.effects.put(effect.getEffectType(), effect);
			}
		}
	}
	
	public boolean isConstructor() {
		return getInstance() instanceof JL5ConstructorInstance;
	}
	
	public boolean isNormalMethod() {
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
}