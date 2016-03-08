package tool.compiler.java.visit;

import polyglot.ext.jl5.types.JL5Subst;
import polyglot.ext.jl5.types.JL5SubstClassType;
import polyglot.ext.jl5.types.TypeVariable;
import polyglot.types.ReferenceType;
import polyglot.types.Type;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractTable implements Ops {
	
	private TypingInfo info;
	private AbstractObjectInfo absObjInfo;	// 추상 객체
	private SetVariable setVar;						// 집합 변수 (필드타입/메서드리턴타입)
	private List<ReferenceType> substitutionTypes;
	
	/**
	 * @param abstractObjectInfo
	 * @param info
	 */
	protected AbstractTable(AbstractObjectInfo abstractObjectInfo, TypingInfo info) {
		checkArguments(abstractObjectInfo, info);	// TODO: 생성자에서 타입을 검사할지 판단
		
		this.info = info;
		this.absObjInfo = abstractObjectInfo;
		
		if(getType() != null) {
			generateSetVariable();
		}
		
		setSubstitutionTypes();
	}
	
	@Override
	public List<TypeVariable> getBoundVariables() {
		return info.getBoundVariables();
	}

	@Override
	public List<TypeVariable> getTypeVariables() {
		return info.getTypeVariables();
	}

	@Override
	public ReferenceType getContainerType() {
		return info.getContainerType();
	}
	
	@Override
	public ReferenceType getContainerBaseType() {
		return info.getContainerBaseType();
	}
	
	public JL5Subst getContainerSubstitutions() {
		return absObjInfo.getSubstitutions();
	}
	
	/**
	 * @return
	 */
	public List<ReferenceType> getContainerSubstitutionTypes() {
		return substitutionTypes;
	}
	
	private final void setSubstitutionTypes() {
		if(absObjInfo.getType() instanceof JL5SubstClassType) {
			substitutionTypes = new LinkedList<>();
			for(TypeVariable typeVar: info.getTypeVariables()) {
				substitutionTypes.add(getContainerSubstitutions().substitutions().get(typeVar));
			}
			
		} else {
			substitutionTypes = null;
		}
		// TODO: Nested Class에 대한 대응 필요 (별도 메서드 만드는 편이 좋을 듯)
	}
	
	/**
	 * @return the absObjInfo
	 */
	public final AbstractObjectInfo getAbstractObjectInfo() {
		return absObjInfo;
	}
	
	/**
	 * @param absObjInfo the absObjInfo to set
	 */
	protected final void setAbstractObjectInfo(AbstractObjectInfo abstractObjectInfo) {
		this.absObjInfo = abstractObjectInfo;
	}
	
	
	@Override
	public String getName() {
		return info.getName();
	}
	
	@Override
	public Type getType() {
		return info.getType();
	}
	
	/**
	 * @return the setVar
	 */
	public final SetVariable getSetVariable() {
		return setVar;
	}
	
	protected void generateSetVariable() {
		setVar = new SetVariable(getType());
	}
	
	protected SetVariable generateSetVariable(Type type) {
		return new SetVariable(type);
	}
	
	/**
	 * AbstractObjectInfo와 TypingInfo의 타입이 일치하지 않는 경우를 검사.
	 * 두 타입이 일치하지 않는 경우에 IllegalArgumentException가 발생함.
	 * @param abstractObjectInfo
	 * @param info
	 */
	public static boolean checkArguments(AbstractObjectInfo abstractObjectInfo, TypingInfo info) {
		if(abstractObjectInfo.getBaseType().equals(info.getContainerBaseType())) {
			return true;
		} else {
			throw new IllegalArgumentException("The type of 'abstractObjectInfo'("+abstractObjectInfo.getBaseType()+") does NOT correspond with the type of 'FieldInfo' or 'MethodInfo.'("+ info.getContainerBaseType() +")");
		}
	}
	
	/**
	 * @return the info
	 */
	public TypingInfo getInfo() {
		return info;
	}
	
	/**
	 * @param info the info to set
	 */
	protected final void setInfo(TypingInfo info) {
		this.info = info;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean superResult = super.equals(obj);
		
		if(superResult) {
			return superResult;
		} else if(!(obj instanceof AbstractTable)) {
			return false;
		} else {
			AbstractTable absTableObj = (AbstractTable) obj;
			return this.getInfo().equals(absTableObj.getInfo())
					&& this.getAbstractObjectInfo().equals(absTableObj.getAbstractObjectInfo());
		}
	}
}