package tool.compiler.java.visit;

import polyglot.ext.jl5.types.JL5ClassType;
import polyglot.ext.jl5.types.JL5FieldInstance;
import polyglot.ext.jl5.types.JL5ParsedClassType;
import polyglot.ext.jl5.types.JL5SubstClassType;
import polyglot.ext.jl5.types.TypeVariable;
import polyglot.types.FieldInstance;
import tool.compiler.java.util.CollUtil;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * lam (Y1,..., Yl, Chi_this,Chi_f1,...,Chi_fj). env_C
 */
public class ClassConstraint implements ConstraintFunction {
	
	private JL5ClassType clz;
	private MetaSetVariable chi_this;
	private LinkedHashSet<MetaSetVariable> chi_typeVars;
	private LinkedHashMap<JL5FieldInstance, MetaSetVariable> chi_fields;
	private LinkedHashSet<Constraint> metaConstraints;
	
	/**
	 * @param type Class Type
	 */
	public ClassConstraint(JL5ClassType type) {
		this.clz = type;
		this.chi_this = new MetaSetVariable(type);
	}
	
	public ClassConstraint(JL5ClassType type, Collection<MetaSetVariable> chiTypeVars) {
		this(type);
		if(chiTypeVars == null) {
			this.chi_typeVars = null;
		} else {
			this.chi_typeVars = new LinkedHashSet<>(chiTypeVars);
		}
	}
	
	public ClassConstraint(JL5ClassType type, Collection<MetaSetVariable> chiTypeVars, Map<JL5FieldInstance, MetaSetVariable> chiFields) {
		this(type, chiTypeVars);
		if(chiFields == null) {
			this.chi_fields = null;
		} else {
			this.chi_fields = new LinkedHashMap<>(chiFields);
		}
	}
	
	public ClassConstraint(MetaSetVariable chiThis) {
		try {
			this.clz = (JL5ClassType) chiThis.getType();
			this.chi_this = chiThis;
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("The type of MetaSetVariable is NOT class type. The type is " + chiThis.getType() + ".");
		}
	}
	
	public ClassConstraint(MetaSetVariable chiThis, Collection<MetaSetVariable> chiTypeVars) {
		this(chiThis);
		if(chiTypeVars == null) {
			this.chi_typeVars = null;
		} else {
			this.chi_typeVars = new LinkedHashSet<>(chiTypeVars);
		}
	}
	
	public ClassConstraint(MetaSetVariable chiThis, Collection<MetaSetVariable> chiTypeVars, Map<JL5FieldInstance, MetaSetVariable> chiFields) {
		this(chiThis, chiTypeVars);
		if(chiFields == null) {
			this.chi_fields = null;
		} else {
			this.chi_fields = new LinkedHashMap<>(chiFields);
		}
	}
	
	/**
	 * @param type Class Type
	 */
	public ClassConstraint(JL5ClassType type, boolean autoGen) {
		this(type);		//this
		
		if(autoGen) {
			if (type instanceof JL5ParsedClassType) {
				List<TypeVariable> typeVars = ((JL5ParsedClassType)type).typeVariables();
				
				if(!typeVars.isEmpty()) {
					this.chi_typeVars = new LinkedHashSet<>();
					
					for(TypeVariable tv : typeVars) {
						this.chi_typeVars.add(new MetaSetVariable(tv));	// field
					}
				}
			}
			
			@SuppressWarnings("unchecked")
			List<JL5FieldInstance> fields = (List<JL5FieldInstance>) type.fields();
			
			if(!fields.isEmpty()) {
				this.chi_fields = new LinkedHashMap<>();
				for(JL5FieldInstance field : fields) {
					this.chi_fields.put(field, new MetaSetVariable(field.type()));	// field
					System.out.println(chi_fields.get(field).getType().getClass());
				}
			}
		}
	}
	
	
	public ConstraintsPair apply() {
		return null;
	}
	
	
	/**
	 * @return the class
	 */
	public JL5ClassType getClassType() {
		return clz;
	}
	
	/**
	 * @return the chi_this
	 */
	public MetaSetVariable getChiThis() {
		return chi_this;
	}
	
	/**
	 * @param chiThis the chi_this to set
	 */
	public void setChiThis(MetaSetVariable chiThis) {
		if(!chiThis.getType().equals(clz)) {
			this.clz = (JL5ClassType) chiThis.getType();
		}
		this.chi_this = chiThis;
	}
	
	/**
	 * @return the chi_typeVars
	 */
	public LinkedHashSet<MetaSetVariable> getChiTypeVars() {
		return chi_typeVars;
	}
	
	/**
	 * @param chiTypeVars the chi_typeVars to set
	 */
	public void setChiTypeVars(Collection<MetaSetVariable> chiTypeVars) {
		if(this.chi_typeVars == null) {
			this.chi_typeVars = new LinkedHashSet<>(chiTypeVars);
		} else {
			this.chi_typeVars.clear();
			this.chi_typeVars.addAll(chiTypeVars);
		}
	}
	
	/**
	 * @param chiTypeVars the chi_typeVars to add
	 */
	public void addChiTypeVars(Collection<MetaSetVariable> chiTypeVars) {
		if(this.chi_typeVars == null) {
			this.chi_typeVars = new LinkedHashSet<>(chiTypeVars);
		} else {
			this.chi_typeVars.addAll(chiTypeVars);
		}
	}
	
	/**
	 * @param chiTypeVar
	 */
	public void addChiTypeVar(MetaSetVariable chiTypeVar) {
		try {
			this.chi_typeVars.add(chiTypeVar);
		} catch (NullPointerException e) {
			this.chi_typeVars = new LinkedHashSet<>();
			this.chi_typeVars.add(chiTypeVar);
		}
	}
	
	/**
	 * @return the chi_fields
	 */
	public LinkedHashSet<MetaSetVariable> getChiFields() {
		return new LinkedHashSet<>(chi_fields.values());
	}
	
	/**
	 * @return the chi_fields
	 */
	public LinkedHashMap<JL5FieldInstance, MetaSetVariable> getChiFieldMap() {
		return new LinkedHashMap<>(chi_fields);
	}
	
	/**
	 * @return the chi_fields
	 */
	public MetaSetVariable getChiField(JL5FieldInstance field) {
		return chi_fields.get(field);
	}
	
	/**
	 * @return the chi_fields
	 */
	public JL5FieldInstance getField(MetaSetVariable chiField) {
		if(chi_fields.containsValue(chiField)) {
			for(Entry<JL5FieldInstance, MetaSetVariable> entry : chi_fields.entrySet()) {
				if(entry.getValue() == chiField) {
					return entry.getKey();
				}
			}
			return null;
		} else {
			return null;
		}
	}
	
	/**
	 * @param chiFields the chi_fields to set
	 */
	public void setChiFields(Map<JL5FieldInstance, MetaSetVariable> chiFields) {
		if(this.chi_fields == null) {
			this.chi_fields = new LinkedHashMap<>(chiFields);
		} else {
			this.chi_fields.clear();
			this.chi_fields.putAll(chiFields);
		}
	}
	
	/**
	 * @param chiFields the chi_fields to add
	 */
	public void addChiFields(Map<JL5FieldInstance, MetaSetVariable> chiFields) {
		if(this.chi_fields == null) {
			this.chi_fields = new LinkedHashMap<>(chiFields);
		} else {
			this.chi_fields.putAll(chiFields);
		}
	}
	
	/**
	 * @param field
	 * @param chiField
	 */
	public void addChiField(JL5FieldInstance field, MetaSetVariable chiField) {
		try {
			this.chi_fields.put(field, chiField);
		} catch (NullPointerException e) {
			this.chi_fields = new LinkedHashMap<>();
			this.chi_fields.put(field, chiField);
		}
	}
	
	/**
	 * @return the metaConstraints
	 */
	public LinkedHashSet<Constraint> getMetaConstraints() {
		return new LinkedHashSet<>(metaConstraints);
	}
	
	/**
	 * @param metaConstraints the metaConstraints to set
	 */
	public void setMetaConstraints(Collection<Constraint> metaConstraints) {
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
	public void addMetaConstraints(Collection<Constraint> metaConstraints) {
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
	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result =  "CC " + clz + ": λ(";
		result += (chi_typeVars != null && !chi_typeVars.isEmpty()) ? (CollUtil.getStringOf(chi_typeVars) + ", ") : "";
		result += chi_this;
		result += (chi_fields != null && !chi_fields.isEmpty()) ? (", " + CollUtil.getStringOf(chi_fields.values())) : "";
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
		result = prime * result + ((clz == null) ? 0 : clz.hashCode());
		result = prime * result + ((chi_this == null) ? 0 : chi_this.hashCode());
		result = prime * result + ((chi_typeVars == null) ? 0 : chi_typeVars.hashCode());
		result = prime * result + ((chi_fields == null) ? 0 : chi_fields.hashCode());
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
		if (chi_typeVars == null) {
			if (other.chi_typeVars != null) {
				return false;
			}
		} else if (!chi_typeVars.equals(other.chi_typeVars)) {
			return false;
		}
		if (chi_fields == null) {
			if (other.chi_fields != null) {
				return false;
			}
		} else if (!chi_fields.equals(other.chi_fields)) {
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
}