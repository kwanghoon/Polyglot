package tool.compiler.java.visit;

import polyglot.ext.jl5.types.JL5Subst;
import polyglot.ext.jl5.types.TypeVariable;
import polyglot.types.ReferenceType;
import polyglot.types.Type;
import tool.compiler.java.util.CollUtil;

import java.util.LinkedList;
import java.util.List;

public class MethodTableRow extends AbstractTableRow implements MethodOps {
	
	private LinkedList<TypedSetVariable> formalList;
	
	public MethodTableRow(AbstractObject abstractObjectInfo, MethodCallInfo info) {
		super(abstractObjectInfo, info);
		generateFormalSetVariables();
	}
	
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
	 * @see tool.compiler.java.visit.AbstractTableRow#getInfo()
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
			result += " -> " + getSetVariable();
		}
		
		return result;
	}
}