package tool.compiler.java.aos;

import polyglot.ast.ArrayInit;
import polyglot.ast.Assign;
import polyglot.ast.Binary;
import polyglot.ast.Expr;
import polyglot.ast.Instanceof;
import polyglot.ast.Lit;
import polyglot.ast.New;
import polyglot.ast.NewArray;
import polyglot.ast.Unary;
import polyglot.ext.jl5.types.JL5ArrayType;
import polyglot.ext.jl5.types.JL5PrimitiveType;
import polyglot.ext.jl5.types.JL5Subst;
import polyglot.ext.jl5.types.JL5SubstClassType;
import polyglot.types.ReferenceType;
import polyglot.types.Type;

import java.util.Collection;

public class AbstractObject extends AbsObjSet {
	
	public static final String KIND = "o";
	private Expr expr;		// 생성 위치 노드
	private static long idFactor = 1;
	/**
	 * info == null => Abstract Object에 해당하는 프로그램 택스트 상의 Node가 있는 경우
	 * info != null => Node와 Type을 아래 Info에 따라 해석
	 */
	private Info info = null;
	
	/**
	 * Abstract Object의 유형에 대한 정보<p>
	 * 
	 * ArrayInitLength:		Node가 {e1, ... , ek}를 가리킨다. k라고 해석.<br>
	 * FieldAssignOp:		Node가 o.f op= e를 가리킨다. o.f op e라고 해석.<br>
	 * LocalAssignOp:		Node가 x op= e를 가리킨다. x op e라고 해석.<br>
	 * ArrayAccessAssignOp:	Node가 o.f[idx] op= e를 가리킨다. o.f[idx] op e라고 해석.
	 * 						Node가 x[idx] op= e를 가리킨다. x[idx] op e라고 해석.
	 */
	public static enum Info {
		ArrayInitLength,		// Node가 {e1, ... , ek}를 가리킨다. k라고 해석.
		FieldAssignOp,			// Node가 o.f op= e를 가리킨다. o.f op e라고 해석.
		LocalAssignOp,			// Node가 x op= e를 가리킨다. x op e라고 해석.
		ArrayAccessAssignOp		// Node가 o.f[idx] op= e를 가리킨다. o.f[idx] op e라고 해석.
								// Node가 x[idx] op= e를 가리킨다. x[idx] op e라고 해석.
	}
	
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
		this(expr, null);
	}
	
	/**
	 * Abstract Object 생성
	 * @param expr	Abstract Object가 생성된 노드
	 * @param info	Abstract Object의 유형 정보 (노드 자체에 대한 Abstract Object인 경우 null)
	 * @see AbstractObject.Info
	 */
	public AbstractObject(Expr expr, Info info) {
		super(inferType(expr, info));
		this.expr = expr;
		this.info = info;
	}
	
	/**
	 * @param expr	Abstract Object가 생성된 노드
	 * @param infoAbstract Object의 유형 정보 (노드 자체에 대한 Abstract Object인 경우 null)
	 * @return 찾아낸 타입
	 */
	private static Type inferType(Expr expr, Info info) {
		try {
			if(info != null) {
				switch (info) {
				case ArrayInitLength:
					JL5ArrayType arrType = (JL5ArrayType) ((ArrayInit) expr).type();
					return arrType.lengthField().type();
				case FieldAssignOp:
				case LocalAssignOp:
				case ArrayAccessAssignOp:
					Assign asgn = (Assign) expr;
					return asgn.left().type();
				}
			}
			return expr.type();
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("expr's type and info are NOT matched.");
		}
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
	 * @return the info
	 */
	public Info getInfo() {
		return info;
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
		return getType() instanceof JL5PrimitiveType;
	}
	
	/**
	 * Lit 노드에서 생성된 상수일 경우 true를 반환한다.<br>
	 * null, int, char, float, boolean, string, class
	 * @return Lit 노드의 상수일 경우 true
	 */
	public boolean isLiteralFromLit() {
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
		return idFactor++;
	}
}