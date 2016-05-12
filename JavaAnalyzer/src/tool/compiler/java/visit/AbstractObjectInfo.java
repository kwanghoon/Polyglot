package tool.compiler.java.visit;

import polyglot.ext.jl5.types.JL5ConstructorInstance;
import polyglot.ext.jl5.types.JL5Subst;
import polyglot.ext.jl5.types.JL5SubstClassType;
import polyglot.types.ReferenceType;

import java.util.Collection;

public class AbstractObjectInfo extends TypedVariable {
	
	private JL5ConstructorInstance ctorIns;
	private static long idGen = 1;
	
	/**
	 * @param JL5 Constructor Instance
	 */
	public AbstractObjectInfo(JL5ConstructorInstance constructorInstance) {
		setType(constructorInstance.container());
		generateID();
		this.ctorIns = constructorInstance;
	}
	
	/**
	 * @return	타입 파라메터로 전달한 클래스 타입의 컬렉션
	 */
	public Collection<ReferenceType> getSubstitutionTypes() {
		try {
			return getSubstitutions().substitutions().values();
		} catch (NullPointerException e) {	// getSubstitutions()이 null인 경우 무시
			return null;
		}
	}
	
	/**
	 * @return	타입 파라메터의 맵
	 */
	public JL5Subst getSubstitutions() {
		if(getType() instanceof JL5SubstClassType) {
			return (JL5Subst) ((JL5SubstClassType)getType()).subst();
		}
		return null;
	}
	
	/**
	 * @return	JL5 Constructor Instance
	 */
	public JL5ConstructorInstance getInstance() {
		return ctorIns;
	}
	
	/**
	 * 식별 문자열 설정
	 */
	@Override
	protected String kind() {
		return "o";
	}
	
	/**
	 * 아이디 번호 생성
	 */
	@Override
	protected long generateIDNum() {
		return idGen++;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (idNum() ^ (idNum() >>> 32));
		return result;
	}
	
	/**
	 * ID가 같아야 같은 Object이다.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AbstractObjectInfo other = (AbstractObjectInfo) obj;
		if (idNum() != other.idNum()) {
			return false;
		}
		return true;
	}
}