package tool.compiler.java.info;

import polyglot.ext.jl5.types.JL5ProcedureInstance;
import polyglot.ext.jl5.types.TypeVariable;
import polyglot.types.ReferenceType;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;

import java.util.List;
import java.util.Map;

public class MethodInfo extends AbstractMethodInfo {
	
	/**
	 * @param procedureInstance
	 */
	public MethodInfo(JL5ProcedureInstance procedureInstance) {
		this(procedureInstance, null);
	}
	
	public MethodInfo(JL5ProcedureInstance procedureInstance, Map<EffectName, EffectSetVariable> effects) {
		super(procedureInstance, effects);
	}
	
	@Override
	public List<TypeVariable> getTypeParams() {
		return getTypeInstance().typeParams();
	}
	
	/**
	 * @return 결정되지 않았으므로 null 반환
	 */
	@Override
	public List<? extends ReferenceType> getMethodSubstitutionTypes() {
		return null;
	}
	
	@Override
	public boolean isGenericMethod() {
		try {
			return !getTypeParams().isEmpty();
		} catch (NullPointerException e) {
			return false;
		}
	}
}