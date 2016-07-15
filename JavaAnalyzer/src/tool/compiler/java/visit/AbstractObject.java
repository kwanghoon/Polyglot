package tool.compiler.java.visit;

import polyglot.ast.ArrayInit;
import polyglot.ast.Binary;
import polyglot.ast.Expr;
import polyglot.ast.Instanceof;
import polyglot.ast.Lit;
import polyglot.ast.New;
import polyglot.ast.NewArray;
import polyglot.ast.Unary;
import polyglot.ext.jl5.types.JL5Subst;
import polyglot.ext.jl5.types.JL5SubstClassType;
import polyglot.types.ReferenceType;

import java.util.Collection;

public class AbstractObject extends AbsObjSet {
	
	public static final String KIND = "o";
	private Expr expr;		// New, NewArray, ArrayInit, Lit, Unary, Binary or Instanceof
	private static long idGen = 1;
	
	
	public AbstractObject(New newNode) {
		this((Expr)newNode);
	}
	
	public AbstractObject(NewArray newArrayNode) {
		this((Expr)newArrayNode);
	}
	
	public AbstractObject(ArrayInit arrayInitNode) {
		this((Expr)arrayInitNode);
	}
	
	public AbstractObject(Lit lit) {
		this((Expr)lit);
	}
	
	public AbstractObject(Unary unary) {
		this((Expr)unary);
	}
	
	public AbstractObject(Binary binary) {
		this((Expr)binary);
	}
	
	public AbstractObject(Instanceof instof) {
		this((Expr)instof);
	}
	
	private AbstractObject(Expr expr) {
		this.expr = expr;
		setType(expr.type());
		generateID();
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
	 * @return the expr
	 */
	public Expr getExpr() {
		return expr;
	}
	
	/**
	 * 클래스의 객체일 경우 true를 반환한다.
	 * @return 객체일 경우 true
	 */
	public boolean isObject() {
		return isScalar() || isArray();
	}
	
	/**
	 * 배열 클래스가 아닌 일반적인 스칼라 클래스의 객체인 경우 true를 반환한다.
	 * 즉, New 노드에서 생성된 경우 true를 반환한다.
	 * @return 스칼라 객체일 경우 true
	 */
	public boolean isScalar() {
		return expr instanceof New;
	}
	
	/**
	 * 배열 클래스의 객체일 경우 true를 반환한다.
	 * @return 배열일 경우 true
	 */
	public boolean isArray() {
		return isArrayFromNew() || isArrayFromInit();
	}
	
	/**
	 * new 키워드를 사용하여 생성한 배열 클래스의 객체일 경우 true를 반환한다.
	 * 즉, NewArray 노드에서 생성된 경우 true를 반환한다.<br>
	 * e.g. new int[3]
	 * @return new로 생성한 배열일 경우 true
	 */
	public boolean isArrayFromNew() {
		return expr instanceof NewArray;
	}
	
	/**
	 * 초기화 구문을 통해 생성한 배열 클래스의 객체일 경우 true를 반환한다.
	 * 즉, ArrayInit 노드에서 생성된 경우 true를 반환한다.<br>
	 * e.g. {1, 2, 3}
	 * @return 원소를 초기화하여 생성한 배열일 경우 true
	 */
	public boolean isArrayFromInit() {
		return expr instanceof ArrayInit;
	}
	
	/**
	 * 상수인 경우 true를 반환한다.
	 * @return 상수일 경우 true
	 */
	public boolean isLiteral() {
		return isLiteralFormLit() || isLiteralFromUnary() ||
				isLiteralFromBinary() || isLiteralFromInstanceof();
	}
	
	/**
	 * Lit 노드에서 생성된 상수일 경우 true를 반환한다.<br>
	 * null, int, char, float, boolean, string, class
	 * @return Lit 노드의 상수일 경우 true
	 */
	public boolean isLiteralFormLit() {
		return expr instanceof Lit;
	}
	
	/**
	 * Unary 노드에서 생성된 상수일 경우 true를 반환한다.<br>
	 * int, char, float, boolean
	 * @return Unary 노드의 상수일 경우 true
	 */
	public boolean isLiteralFromUnary() {
		return expr instanceof Unary;
	}
	
	/**
	 * Binary 노드에서 생성된 상수일 경우 true를 반환한다.<br>
	 * int, char, float, boolean, string
	 * @return Binary 노드의 상수일 경우 true
	 */
	public boolean isLiteralFromBinary() {
		return expr instanceof Binary;
	}
	
	/**
	 * Instanceof 노드에서 생성된 상수일 경우 true를 반환한다.<br>
	 * boolean
	 * @return Instanceof 노드의 상수일 경우 true
	 */
	public boolean isLiteralFromInstanceof() {
		return expr instanceof Instanceof;
	}
	
	/**
	 * 식별 문자열 설정
	 */
	@Override
	public String kind() {
		return KIND;
	}
	
	/**
	 * 아이디 번호 생성
	 */
	@Override
	protected long generateIDNum() {
		return idGen++;
	}
}