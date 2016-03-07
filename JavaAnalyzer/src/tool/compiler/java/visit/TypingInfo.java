package tool.compiler.java.visit;

import polyglot.ext.jl5.types.JL5ClassType;
import polyglot.ext.jl5.types.JL5ParsedClassType;
import polyglot.ext.jl5.types.JL5SubstClassType;
import polyglot.ext.jl5.types.RawClass;
import polyglot.ext.jl5.types.TypeVariable;
import polyglot.ext.jl7.types.DiamondType;
import polyglot.types.ClassType;
import polyglot.types.MemberInstance;
import polyglot.types.ReferenceType;
import polyglot.types.Type;

import java.util.LinkedList;
import java.util.List;

public abstract class TypingInfo implements Info, Ops {
	
	private ReferenceType container;
	
	protected TypingInfo(MemberInstance memberInstance) {
		setTypeInstance(memberInstance);
		setContainerType(memberInstance.container());
	}
	
	/**
	 * get Delta
	 * @return
	 */
	@Override
	public List<TypeVariable> getBoundVariables() {
		try {
			return getBoundVariables((JL5ClassType)container, new LinkedList<TypeVariable>());
		} catch (ClassCastException e) {
			e.printStackTrace();
//			System.err.println("getBoundVariables(): ClassCastException: " + container.getClass());
			return null;
		}
	}
	
	/**
	 * get Delta
	 * @param classType
	 * @param boundVariables
	 * @return
	 */
	private final List<TypeVariable> getBoundVariables(JL5ClassType classType, List<TypeVariable> boundVariables) {
		ClassType outer = classType.outer();
		List<TypeVariable> result;
		if(outer != null) {
			result = getBoundVariables((JL5ClassType) outer, boundVariables);
		} else {
			result = boundVariables;
		}
		
		boundVariables.addAll(getTypeVariables(classType));
		
		return result;
	}
	
	/**
	 * get Type Variables
	 * @return	타입 변수 리스트
	 */
	@Override
	public List<TypeVariable> getTypeVariables() {
		try {
			return getTypeVariables((JL5ClassType) container);
		} catch (ClassCastException e) {
			e.printStackTrace();
//			System.err.println("getBoundVariables(): ClassCastException: " + container.getClass());
			return null;
		}
	}
	
	protected final List<TypeVariable> getTypeVariables(JL5ClassType classType) {
		
		JL5ParsedClassType parsedClass = null;
		
		if(classType instanceof JL5ParsedClassType) {
			parsedClass = (JL5ParsedClassType) classType;
		} else if (classType instanceof JL5SubstClassType){
			parsedClass =  ((JL5SubstClassType) classType).base();
		} else if (classType instanceof RawClass) {
			parsedClass = ((RawClass)classType).base();
		} else if (classType instanceof DiamondType) {
			parsedClass = ((DiamondType)classType).base();
		} else /*if (classType instanceof IntersectionType) */{
			throw new RuntimeException("Not JL5ParsedClassType, JL5SubstClassType, RawClass nor DiamondType");
		}
		
		return parsedClass.typeVariables();
	}
	
	/**
	 * get Class
	 * @return	포함되어 있는 클래스의 타입
	 */
	@Override
	public ReferenceType getContainerType() {
		return container;
	}
	
	@Override
	public ReferenceType getContainerBaseType() {
		if(container instanceof JL5SubstClassType) {
			return ((JL5SubstClassType)container).base();
		}
		return getContainerType();
	}
	
	/**
	 * @param containerType
	 */
	private void setContainerType(ReferenceType containerType) {
		this.container = containerType;
	}
	
	@Override
	public Type getBaseType() {
		Type type = getType();
		if(type != null && type instanceof JL5SubstClassType) {
			return ((JL5SubstClassType)type).base();
		}
		return type;
	}
	
	/**
	 * @return the Instance
	 */
	public abstract MemberInstance getTypeInstance();

	/**
	 * @param Instance the Instance to set
	 */
	protected abstract void setTypeInstance(MemberInstance memberInstance);

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean superResult = super.equals(obj);
		
		if(superResult) {
			return superResult;
		} else if(obj instanceof TypingInfo) {
			TypingInfo tiObj = (TypingInfo) obj;
			return this.getTypeInstance().equals(tiObj.getTypeInstance());
		} else {
			return false;
		}
	}
}