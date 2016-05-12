package tool.compiler.java.visit;

import polyglot.ext.jl5.types.JL5Subst;
import polyglot.ext.jl5.types.JL5SubstClassType;
import polyglot.ext.jl5.types.TypeVariable;
import polyglot.types.ReferenceType;
import polyglot.types.Type;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractTableRow implements Ops {
	
	private TypingInfo info;
	private AbstractObjectInfo absObjInfo = null;	// 추상 객체
	private TypedSetVariable setVar;				// 집합 변수 (필드타입/메서드리턴타입)
	private List<ReferenceType> substitutionTypes;
	
	/**
	 * @param abstractObjectInfo
	 * @param info
	 */
	protected AbstractTableRow(AbstractObjectInfo abstractObjectInfo, TypingInfo info) {
		checkArguments(abstractObjectInfo, info);	// TODO: 생성자에서 타입을 검사할지 결정
		
		this.info = info;
		
		if(!info.isStatic()) {
			this.absObjInfo = abstractObjectInfo;
		}
		
		if(getType() != null) {
			generateSetVariable();
		}
		
		setSubstitutionTypes();
	}
	
	protected AbstractTableRow(TypingInfo info) {
		this(null, info);
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
		if(!isStatic() && absObjInfo.getType() instanceof JL5SubstClassType) {
			substitutionTypes = new LinkedList<>();
			for(TypeVariable typeVar: info.getTypeVariables()) {
				substitutionTypes.add(getContainerSubstitutions().substitutions().get(typeVar));
			}
			
		} else {
			substitutionTypes = null;
		}
		// TODO: Nested Class에 대한 대응 필요 (별도의 메서드를 만드는 편이 좋을 듯)
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
	public final TypedSetVariable getSetVariable() {
		return setVar;
	}
	
	protected void generateSetVariable() {
		setVar = new TypedSetVariable(getType());
	}
	
	protected TypedSetVariable generateSetVariable(Type type) {
		return new TypedSetVariable(type);
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
			throw new IllegalArgumentException(
					"The type of '" + abstractObjectInfo.getClass().getSimpleName() + "'"
						+ "("+abstractObjectInfo.getBaseType()+") "
						+ "does NOT correspond with the type of "
						+ "'" + info.getClass().getSimpleName() + ".'"
						+ "("+ info.getContainerBaseType() +")");
		}
	}
	
	@Override
	public boolean isStatic() {
		if(absObjInfo == null) {
			return true;
		} else {
			return info.isStatic();
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
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((info == null) ? 0 : info.hashCode());
		result = prime * result + ((absObjInfo == null) ? 0 : absObjInfo.hashCode());
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
		AbstractTableRow other = (AbstractTableRow) obj;
		if (info == null) {
			if (other.info != null) {
				return false;
			}
		} else if (!info.equals(other.info)) {
			return false;
		}
		if (absObjInfo == null) {
			if (other.absObjInfo != null) {
				return false;
			}
		} else if (!absObjInfo.equals(other.absObjInfo)) {
			return false;
		}
		return true;
	}
}