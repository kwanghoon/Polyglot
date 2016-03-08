package tool.compiler.java.visit;

import polyglot.ext.jl5.types.JL5ConstructorInstance;
import polyglot.ext.jl5.types.JL5MethodInstance;
import polyglot.ext.jl5.types.JL5ProcedureInstance;
import polyglot.ext.jl5.types.JL5Subst;
import polyglot.ext.jl5.types.JL5TypeSystem;
import polyglot.ext.jl5.types.TypeVariable;
import polyglot.types.ReferenceType;
import tool.compiler.java.util.EquGenUtil;

import java.util.LinkedList;
import java.util.List;

public class MethodCallInfo extends AbstractMethodInfo {
	
	private JL5ProcedureInstance origProcIns;
	private JL5Subst typeArgs = null;
	private List<ReferenceType> substitutionTypes = null;
	
	public MethodCallInfo(JL5ProcedureInstance procedureInstance) {
		super(procedureInstance);
		this.origProcIns = getOrigInstanceOf(getTypeInstance());
		if(isGenericMethod()) {
			this.typeArgs = inferTypeArgs();
			generateSubstitutionTypes();
		}
	}
	
	@Override
	public List<TypeVariable> getTypeParams() {
		return origProcIns.typeParams();
	}
	
	/**
	 * @return the substitutions
	 */
	public JL5Subst getSubstitutions() {
		return typeArgs;
	}
	
	/**
	 * @return the actual type arguments
	 */
	@Override
	public List<? extends ReferenceType> getMethodSubstitutionTypes() {
		return substitutionTypes;
	}
	
	/**
	 * set the substitutionTypes
	 */
	private final void generateSubstitutionTypes() {
		if(typeArgs != null) {
			substitutionTypes = new LinkedList<>();
			for(TypeVariable typeVar: getTypeParams()) {
				substitutionTypes.add(typeArgs.substitutions().get(typeVar));
			}
		} else {
			substitutionTypes = null;
		}
	}

	/**
	 * Infer type arguments for origProcIns, when it is called with arguments of type argTypes.
	 */
	protected final JL5Subst inferTypeArgs() {
		return EquGenUtil.inferTypeArgs(origProcIns, getFormalTypes(), getType(), (JL5TypeSystem) origProcIns.typeSystem());
	}
	
	/**
	 * @return the original ProcedureInstance
	 */
	public JL5ProcedureInstance getOrigTypeInstance() {
		return origProcIns;
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
	
	@Override
	public boolean isGenericMethod() {
		return getTypeInstance() != origProcIns;
	}
	
	public static boolean isGenericMethod(JL5ProcedureInstance procedureInstance) {
		return procedureInstance != getOrigInstanceOf(procedureInstance);
	}
}