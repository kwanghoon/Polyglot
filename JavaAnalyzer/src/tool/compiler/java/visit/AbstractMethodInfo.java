package tool.compiler.java.visit;

import java.util.List;

import polyglot.ext.jl5.types.JL5ConstructorInstance;
import polyglot.ext.jl5.types.JL5MethodInstance;
import polyglot.ext.jl5.types.JL5ProcedureInstance;
import polyglot.ext.jl5.types.TypeVariable;
import polyglot.types.MemberInstance;
import polyglot.types.Type;
import tool.compiler.java.util.CollUtil;

public abstract class AbstractMethodInfo extends AbstractTypingInfo implements MethodOps {

	private JL5ProcedureInstance procIns;
	
	protected AbstractMethodInfo(JL5ProcedureInstance procedureInstance) {
		super(procedureInstance);
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
			result += " -> " + getType();
		}
		
		return result;
	}
}
