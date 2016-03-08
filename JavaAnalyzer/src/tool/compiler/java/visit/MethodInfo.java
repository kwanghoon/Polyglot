package tool.compiler.java.visit;

import polyglot.ext.jl5.types.JL5ProcedureInstance;
import polyglot.ext.jl5.types.TypeVariable;
import polyglot.types.ReferenceType;

import java.util.List;

public class MethodInfo extends AbstractMethodInfo {
	
	/**
	 * @param procedureInstance
	 */
	public MethodInfo(JL5ProcedureInstance procedureInstance) {
		super(procedureInstance);
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