package tool.compiler.java.env;

import polyglot.ast.TypeNode;
import polyglot.ext.jl5.types.JL5ClassType;
import polyglot.ext.jl5.types.JL5FieldInstance;
import polyglot.ext.jl5.types.JL5ParsedClassType;
import polyglot.ext.jl5.types.TypeVariable;
import polyglot.types.FieldInstance;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.constraint.Constraint;
import tool.compiler.java.util.CollUtil;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;

/**
 * lam (Y1, ..., Yl, Chi_this, Chi_f1, ..., Chi_fj). env_C
 */
public class ClassConstraint implements ConstraintFunction {
	
	private JL5ParsedClassType clz;
	private MetaSetVariable chi_this;
	private LinkedHashMap<JL5FieldInstance, MetaSetVariable> chi_fields;
	
	private ConstraintFunction outerConstraint = null;		// Not inheritance, but containment relationship
	private LinkedHashSet<ClassConstraint> innerClassConstraints;
	private LinkedHashSet<CodeConstraint> codeConstraints;	// MethodConstraint / InitializerConstraint
	private LinkedHashSet<Constraint> fieldConstraints;		// for Field Initialization (contain sub-nodes' constraints)
	
	/**
	 * @param type Class Type
	 */
	public ClassConstraint(JL5ParsedClassType type) {
		this.clz = type;
		generateThis(type);
		generateFields(type);
	}
	
	private void generateThis(JL5ClassType type) {
		// 배열 타입에 대한 ClassConstraint는 생성하지 않으므로, 
		// ArrayMetaSetVariable을 생성하는 경우는 없음
		this.chi_this = MetaSetVariable.create(type);
		ReportUtil.report(chi_this, MetaSetVarSource.New, MetaSetVarGoal.ClassEnvironment);
	}
	
	private void generateFields(JL5ClassType type) {
		List<? extends FieldInstance> fields = type.fields();
		if (!fields.isEmpty()) {
			this.chi_fields = new LinkedHashMap<>();
			for (FieldInstance field : fields) {
				MetaSetVariable chi_field = MetaSetVariable.create(field.type());
				this.chi_fields.put((JL5FieldInstance)field, chi_field);	// field
				ReportUtil.report(chi_field, MetaSetVarSource.New, MetaSetVarGoal.ClassEnvironment);
			}
		} else {
			this.chi_fields = null;
		}
	}
	
	
//	public ConstraintsPair apply() {
//		return null;
//	}
	
	
	/**
	 * @return the class
	 */
	public JL5ClassType getClassType() {
		return clz;
	}
	
	/**
	 * @return the chi_this
	 */
	public MetaSetVariable getThis() {
		return chi_this;
	}
	
	/**
	 * @return the chi_this
	 */
	public MetaSetVariable getThis(TypeNode qualifier) {
		if (qualifier == null) {
			return getThis();
		}
		if (qualifier.type().equals(chi_this.getType())) {
			return chi_this;
		} else if (outerConstraint instanceof ClassConstraint) {
			return ((ClassConstraint)outerConstraint).getThis(qualifier);
		} else/* if (outerConstraint instanceof CodeConstraint)*/ {
			return ((CodeConstraint)outerConstraint).getOuter().getThis(qualifier);
		}
	}
	
	/**
	 * TODO: 미완성, spuer에 대하여 대응 필요
	 * 잘못 구현한 듯? super는 상속관계에서 사용하는 것이 아닌가?
	 * @return the chi_this
	 */
	public MetaSetVariable getSuper() {
		return chi_this;
	}
	
	/**
	 * TODO: 미완성, spuer에 대하여 대응 필요
	 * 잘못 구현한 듯? super는 상속관계에서 사용하는 것이 아닌가?
	 * @param qualifier
	 * @return
	 */
	public MetaSetVariable getSuper(TypeNode qualifier) {
		if (qualifier == null) {
			return getSuper();
		}
		if (qualifier.type().equals(chi_this.getType())) {
			return chi_this;
		} else if (outerConstraint instanceof ClassConstraint) {
			return ((ClassConstraint) outerConstraint).getSuper(qualifier);
		} else/* if (outerConstraint instanceof CodeConstraint)*/ {
			return ((CodeConstraint)outerConstraint).getOuter().getSuper(qualifier);
		}
	}
	
	/**
	 * @return the typeVars
	 */
	public List<TypeVariable> getTypeVars() {
		return clz.typeVariables();
	}
	
	/**
	 * @return the chi_fields
	 */
	public LinkedHashSet<MetaSetVariable> getFields() {
		return new LinkedHashSet<>(chi_fields.values());
	}
	
	/**
	 * @return the chi_fields
	 */
	public LinkedHashMap<JL5FieldInstance, MetaSetVariable> getFieldMap() {
		return new LinkedHashMap<>(chi_fields);
	}
	
	/**
	 * @return the chi_fields
	 */
	public MetaSetVariable getField(JL5FieldInstance field) {
		return chi_fields.get(field);
	}
	
	/**
	 * @return the chi_fields
	 */
	public JL5FieldInstance getField(MetaSetVariable chi_field) {
		if (chi_fields.containsValue(chi_field)) {
			for (Entry<JL5FieldInstance, MetaSetVariable> entry : chi_fields.entrySet()) {
				if (entry.getValue() == chi_field) {
					return entry.getKey();
				}
			}
			return null;
		} else {
			return null;
		}
	}
	
	
	/**
	 * @return the outerConstraintFunction
	 */
	@Override
	public ConstraintFunction getOuter() {
		return outerConstraint;
	}
	
	/**
	 * @param outerConstraintFunction the outerClassConstraint to set
	 */
	public void setOuter(ConstraintFunction outerConstraintFunction) {
		this.outerConstraint = outerConstraintFunction;
	}
	
	/**
	 * @return innerClassConstraints
	 */
	@Override
	public LinkedHashSet<ClassConstraint> getInners() {
		try {
			return new LinkedHashSet<>(innerClassConstraints);
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @param innerClassConstraints innerClassConstraints to set
	 */
	@Override
	public void setInners(Collection<ClassConstraint> innerClassConstraints) {
		if (innerClassConstraints != null) {
			if (this.innerClassConstraints == null) {
				this.innerClassConstraints = new LinkedHashSet<>(innerClassConstraints);
			} else {
				this.innerClassConstraints.clear();
				this.innerClassConstraints.addAll(innerClassConstraints);
			}
		} else {
			this.innerClassConstraints = null;
		}
	}
	
	/**
	 * @param innerClassConstraints innerClassConstraints to add
	 */
	@Override
	public void addInners(Collection<ClassConstraint> innerClassConstraints) {
		if (innerClassConstraints != null) {
			if (this.innerClassConstraints == null) {
				this.innerClassConstraints = new LinkedHashSet<>(innerClassConstraints);
			} else {
				this.innerClassConstraints.addAll(innerClassConstraints);
			}
		}
	}
	
	/**
	 * @param innerClassConstraint the innerClassConstraint to add
	 */
	@Override
	public void addInner(ClassConstraint innerClassConstraint) {
		if (innerClassConstraint != null) {
			try {
				this.innerClassConstraints.add(innerClassConstraint);
			} catch (NullPointerException e) {
				this.innerClassConstraints = new LinkedHashSet<>();
				this.innerClassConstraints.add(innerClassConstraint);
			}
		}
	}
	
	/**
	 * @return MethodConstraints and/or InitializerConstraints
	 */
	public LinkedHashSet<? extends CodeConstraint> getCodeConstraints() {
		try {
			return new LinkedHashSet<>(codeConstraints);
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @param codeConstraints MethodConstraints and/or InitializerConstraints to set
	 */
	public void setCodeConstraints(Collection<? extends CodeConstraint> codeConstraints) {
		if (codeConstraints != null) {
			if (this.codeConstraints == null) {
				this.codeConstraints = new LinkedHashSet<>(codeConstraints);
			} else {
				this.codeConstraints.clear();
				this.codeConstraints.addAll(codeConstraints);
			}
		} else {
			this.codeConstraints = null;
		}
	}
	
	/**
	 * @param codeConstraints MethodConstraints and/or InitializerConstraints to add
	 */
	public void addCodeConstraints(Collection<? extends CodeConstraint> codeConstraints) {
		if (codeConstraints != null) {
			if (this.codeConstraints == null) {
				this.codeConstraints = new LinkedHashSet<>(codeConstraints);
			} else {
				this.codeConstraints.addAll(codeConstraints);
			}
		}
	}
	
	/**
	 * @param codeConstraint MethodConstraint or InitializerConstraint to add
	 */
	public void addCodeConstraint(CodeConstraint codeConstraint) {
		if (codeConstraint != null) {
			try {
				this.codeConstraints.add(codeConstraint);
			} catch (NullPointerException e) {
				this.codeConstraints = new LinkedHashSet<>();
				this.codeConstraints.add(codeConstraint);
			}
		}
	}
	
	/**
	 * @return fieldConstraints
	 */
	@Override
	public LinkedHashSet<Constraint> getMetaConstraints() {
		try {
			return new LinkedHashSet<>(fieldConstraints);
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @param fieldConstraints fieldConstraints to set
	 */
	@Override
	public void setMetaConstraints(Collection<? extends Constraint> fieldConstraints) {
		if (fieldConstraints != null) {
			if (this.fieldConstraints == null) {
				this.fieldConstraints = new LinkedHashSet<>(fieldConstraints);
			} else {
				this.fieldConstraints.clear();
				this.fieldConstraints.addAll(fieldConstraints);
			}
		} else {
			this.fieldConstraints = null;
		}
	}
	
	/**
	 * @param fieldConstraints fieldConstraints to add
	 */
	@Override
	public void addMetaConstraints(Collection<? extends Constraint> fieldConstraints) {
		if (fieldConstraints != null) {
			if (this.fieldConstraints == null) {
				this.fieldConstraints = new LinkedHashSet<>(fieldConstraints);
			} else {
				this.fieldConstraints.addAll(fieldConstraints);
			}
		}
	}
	
	/**
	 * @param fieldConstraint the fieldConstraint to add
	 */
	@Override
	public void addMetaConstraint(Constraint fieldConstraint) {
		if (fieldConstraint != null) {
			try {
				this.fieldConstraints.add(fieldConstraint);
			} catch (NullPointerException e) {
				this.fieldConstraints = new LinkedHashSet<>();
				this.fieldConstraints.add(fieldConstraint);
			}
		}
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
		List<TypeVariable> typeVars = getTypeVars();
		ArrayList<String> subResult = new ArrayList<>();
		if (typeVars != null && !typeVars.isEmpty()) {
			subResult.add(CollUtil.getStringOf(typeVars));
		}
		if (chi_this != null) {
			subResult.add(chi_this.toString());
		}
		if (chi_fields != null && !chi_fields.isEmpty()) {
			subResult.add(CollUtil.getStringOf(chi_fields.values()));
		}
		StringBuilder result = new StringBuilder();
		result.append('C').append('C').append(' ').append(clz).append(':').append(' ')
			.append('λ').append('(').append(CollUtil.getStringOf(subResult)).append(')');
		return result.toString();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clz == null) ? 0 : clz.hashCode());
		result = prime * result + ((chi_this == null) ? 0 : chi_this.hashCode());
//		result = prime * result + ((chi_fields == null) ? 0 : chi_fields.hashCode());
//		result = prime * result + ((outerConstraint == null) ? 0 : outerConstraint.hashCode());
//		result = prime * result + ((innerClassConstraints == null) ? 0 : innerClassConstraints.hashCode());
//		result = prime * result + ((codeConstraints == null) ? 0 : codeConstraints.hashCode());
//		result = prime * result + ((fieldConstraints == null) ? 0 : fieldConstraints.hashCode());
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
		ClassConstraint other = (ClassConstraint) obj;
		if (clz == null) {
			if (other.clz != null) {
				return false;
			}
		} else if (!clz.equals(other.clz)) {
			return false;
		}
		if (chi_this == null) {
			if (other.chi_this != null) {
				return false;
			}
		} else if (!chi_this.equals(other.chi_this)) {
			return false;
		}
//		if (chi_fields == null) {
//			if (other.chi_fields != null) {
//				return false;
//			}
//		} else if (!chi_fields.equals(other.chi_fields)) {
//			return false;
//		}
//		if (outerConstraint == null) {
//			if (other.outerConstraint != null) {
//				return false;
//			}
//		} else if (!outerConstraint.equals(other.outerConstraint)) {
//			return false;
//		}
//		if (innerClassConstraints == null) {
//			if (other.innerClassConstraints != null) {
//				return false;
//			}
//		} else if (!innerClassConstraints.equals(other.innerClassConstraints)) {
//			return false;
//		}
//		if (codeConstraints == null) {
//			if (other.codeConstraints != null) {
//				return false;
//			}
//		} else if (!codeConstraints.equals(other.codeConstraints)) {
//			return false;
//		}
//		if (fieldConstraints == null) {
//			if (other.fieldConstraints != null) {
//				return false;
//			}
//		} else if (!fieldConstraints.equals(other.fieldConstraints)) {
//			return false;
//		}
		return true;
	}
}