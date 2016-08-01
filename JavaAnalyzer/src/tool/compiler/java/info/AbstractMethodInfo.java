package tool.compiler.java.info;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import polyglot.ext.jl5.types.JL5ConstructorInstance;
import polyglot.ext.jl5.types.JL5MethodInstance;
import polyglot.ext.jl5.types.JL5ProcedureInstance;
import polyglot.ext.jl5.types.TypeVariable;
import polyglot.types.MemberInstance;
import polyglot.types.Type;
import tool.compiler.java.effect.Effect;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.table.MethodOps;
import tool.compiler.java.util.CollUtil;

public abstract class AbstractMethodInfo extends AbstractTypingInfo implements MethodOps {

	private JL5ProcedureInstance procIns;
	private LinkedHashMap<EffectName, Effect> effects;
	
	protected AbstractMethodInfo(JL5ProcedureInstance procedureInstance) {
		super(procedureInstance);
	}
	
	protected AbstractMethodInfo(JL5ProcedureInstance procedureInstance, Map<EffectName, Effect> effects) {
		this(procedureInstance);
		if(effects != null) {
			this.effects = new LinkedHashMap<>();
			
			for(EffectName type : EffectName.values()) {
				Effect effect = effects.get(type);
				if (effect != null) {
					this.effects.put(type,effect);
				}
			}
		} else {
			this.effects = null;
		}
	}
	
	protected AbstractMethodInfo(JL5ProcedureInstance procedureInstance, Collection<Effect> effects) {
		this(procedureInstance);
		if(effects != null) {
			this.effects = new LinkedHashMap<>();
			
			for(EffectName type : EffectName.values()) {
				for(Effect effect : effects) {
					if(!this.effects.containsValue(effect) && effect.getType().equals(type)) {
						this.effects.put(type, effect);
					}
				}
			}
		} else {
			this.effects = null;
		}
	}
	
	@Override
	public List<TypeVariable> getBoundVariables() {
		List<TypeVariable> result = super.getBoundVariables();
		result.addAll(getTypeParams());
		return result;
	}
	
	@Override
	public String getName() {
		return isNormalMethod() ? ((JL5MethodInstance)procIns).name() : procIns.container().toString();
	}
	
	@Override
	public List<? extends Type> getFormalTypes() {
		return procIns.formalTypes();
	}
	
	@Override
	public Type getType() {
		return isNormalMethod() ? ((JL5MethodInstance)procIns).returnType() : null;
	}
	
	/**
	 * @return the ProcedureInstance
	 */
	@Override
	public JL5ProcedureInstance getTypeInstance() {
		return procIns;
	}
	
	/**
	 * @param ProcedureInstance the ProcedureInstance to set
	 */
	@Override
	protected final void setTypeInstance(MemberInstance procedureInstance) {
		this.procIns = (JL5ProcedureInstance) procedureInstance;	// ClassCastingException이 발생하는 경우가 없음.
	}
	
	/**
	 * @return effects
	 */
	@Override
	public List<Effect> getEffects() {
		try {
			return new ArrayList<Effect>(effects.values());
		} catch(NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @return effects
	 */
	public Map<EffectName, Effect> getEffectMap() {
		return effects;
	}
	
	/**
	 * @param type	Effect Type
	 * @return effect
	 */
	@Override
	public Effect getEffect(EffectName type) {
		try {
			return effects.get(type);
		} catch(NullPointerException e) {
			return null;
		}
	}
	
	@Override
	public boolean isConstructor() {
		return procIns instanceof JL5ConstructorInstance;
	}
	
	@Override
	public boolean isNormalMethod() {
		return procIns instanceof JL5MethodInstance;
	}
	
	public static boolean isGenericMethod(JL5ProcedureInstance procedureInstance) {
		if(procedureInstance != getOrigInstanceOf(procedureInstance)) {
			return true;
		} else {
			try {
				return !procedureInstance.typeParams().isEmpty();
			} catch (NullPointerException e) {
				return false;
			}
		}
	}
	
	protected final static JL5ProcedureInstance getOrigInstanceOf(JL5ProcedureInstance procedureInstance) {
		if(procedureInstance instanceof JL5MethodInstance) {
			return (JL5MethodInstance) ((JL5MethodInstance)procedureInstance).orig();
		} else if(procedureInstance instanceof JL5ConstructorInstance) {
			return (JL5ConstructorInstance) ((JL5ConstructorInstance)procedureInstance).orig();
		} else {		// 발생하지 않는 경우임.
			return null;
		}
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result =  "MI("
				+ CollUtil.getStringOf(getBoundVariables(), '{', '}') + ", "
				+ getContainerBaseType();
		
		if(getTypeVariables().isEmpty()) {
			result += "<>";
		}
		
		result += ", "
				+ getName() + CollUtil.getStringOf(getMethodSubstitutionTypes(), '<', '>') + ") = "
				+ CollUtil.getStringOf(getFormalTypes(), '(', ')');
		
		if(isNormalMethod()) {
			result += " --" + (effects != null ? " " + effects + " " : "") + "--> " + getType();
		}
		
		return result;
	}
}
