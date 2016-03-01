package tool.compiler.java.visit;

import java.util.Collection;
import java.util.Map;

import polyglot.ext.jl5.types.JL5ConstructorInstance;
import polyglot.ext.jl5.types.JL5SubstClassType;
import polyglot.ext.jl5.types.TypeVariable;
import polyglot.types.ReferenceType;

public class AbstractObjectInfo extends InfoVariable {
	
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
			return getSubstitutions().values();
		} catch (NullPointerException e) {	// getSubstitutions()이 null인 경우 무시
			return null;
		}
	}
	
	/**
	 * @return	타입 파라메터의 맵
	 */
	public Map<TypeVariable, ReferenceType> getSubstitutions() {
		if(getType() instanceof JL5SubstClassType) {
			return ((JL5SubstClassType)getType()).subst().substitutions();
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
	 * 식별 문자 설정
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
}