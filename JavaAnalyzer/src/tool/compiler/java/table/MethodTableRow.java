package tool.compiler.java.table;

import polyglot.ext.jl5.types.JL5Subst;
import polyglot.ext.jl5.types.TypeVariable;
import polyglot.types.ReferenceType;
import polyglot.types.Type;
import tool.compiler.java.aos.AbstractObject;
import tool.compiler.java.aos.TypedSetVariable;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.info.MethodCallInfo;
import tool.compiler.java.util.CollUtil;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MethodTableRow extends AbstractTableRow implements MethodOps {
	
	private LinkedList<TypedSetVariable> formalList;
	private LinkedHashMap<EffectName, EffectSetVariable> effects;
	
	public MethodTableRow(AbstractObject abstractObjectInfo, MethodCallInfo info) {
		super(abstractObjectInfo, info);
		Map<EffectName, EffectSetVariable> effectMap = info.getEffects();
		if(effectMap != null) {
			this.effects = new LinkedHashMap<>(effectMap);
		}
		generateFormalSetVariables();
	}
	
	@Deprecated
	public MethodTableRow(MethodCallInfo info) {
		this(null, info);
	}
	
	@Override
	public List<TypeVariable> getTypeParams() {
		return getInfo().getTypeParams();
	}
	
	public JL5Subst getMethodSubstitutions() {
		return getInfo().getSubstitutions();
	}
	
	@Override
	public List<? extends ReferenceType> getMethodSubstitutionTypes() {
		return getInfo().getMethodSubstitutionTypes();
	}
	
	@Override
	public List<? extends Type> getFormalTypes() {
		return getInfo().getFormalTypes();
	}
	
	public List<TypedSetVariable> getFormalSetVariables() {
		return formalList;
	}
	
	private void generateFormalSetVariables() {
		if(formalList == null) {
			formalList = new LinkedList<TypedSetVariable>();
		} else {
			formalList.clear();
		}
		
		for(Type type: getFormalTypes()) {
			formalList.add(new TypedSetVariable(type));
		}
	}
	
	/**
	 * @return effects
	 */
	@Override
	public Map<EffectName, EffectSetVariable> getEffects() {
		try {
			return new LinkedHashMap<>(effects);
		} catch(NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @param type	Effect Type
	 * @return effect
	 */
	@Override
	public EffectSetVariable getEffect(EffectName type) {
		try {
			return effects.get(type);
		} catch(NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @see tool.compiler.java.table.AbstractTableRow#getInfo()
	 */
	@Override
	public MethodCallInfo getInfo() {
		return (MethodCallInfo) super.getInfo();
	}
	
	@Override
	public boolean isConstructor() {
		return getInfo().isConstructor();
	}
	
	@Override
	public boolean isNormalMethod() {
		return getInfo().isNormalMethod();
	}
	
	@Override
	public boolean isGenericMethod() {
		return getInfo().isGenericMethod();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result =  "M("
				+ CollUtil.getStringOf(getBoundVariables(), '{', '}') + ", "
				+ getContainerBaseType();
		
		if(getTypeVariables().isEmpty()) {
			result += "<>";
		}
		
		result += ", "
				+ getAbstractObjectInfo() + (!isStatic() ? CollUtil.getStringOf(getContainerSubstitutionTypes(), '<', '>') : "") + ", "
				+ getName() + CollUtil.getStringOf(getMethodSubstitutionTypes(), '<', '>') + ") = "
				+ CollUtil.getStringOf(getFormalSetVariables(), '(', ')');
		
		if(isNormalMethod()) {
			result += " --" + (effects != null ? " " + effects + " " : "") + "--> " + getSetVariable();
		}
		
		return result;
	}
}