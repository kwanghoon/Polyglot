package tool.compiler.java.ast;

import polyglot.ast.Ext;
import polyglot.ast.ExtFactory;

public final class EquGenExtFactory_c extends EquGenAbstractExtFactory_c {
	
	public EquGenExtFactory_c() {
		super();
	}
	
	public EquGenExtFactory_c(ExtFactory nextExtFactory) {
		super(nextExtFactory);
	}
	
	@Override
	protected Ext extNodeImpl() {
		return new EquGenExt();
	}
	
	
	// TODO: Override factory methods for new extension nodes in the current
	// extension.
	
	/**
	 * ClassDecl <: ClassMember <: Term <: Node	<br>
	 * ClassDecl <: CodeNode <: Term <: Node
	 */
	@Override
	protected Ext extClassDeclImpl() {
		return new EquGenClassDeclExt();
	}
	
	/**
	 * FieldDecl <: ClassMember <: Term <: Node	<br>
	 * FieldDecl <: CodeNode <: Term <: Node
	 */
	@Override
	protected Ext extFieldDeclImpl() {
//		return super.extFieldDeclImpl();
		return new EquGenFieldDeclExt();
	}
	
	/**
	 * ProcedureDecl <: CodeDecl <: ClassMember <: Term <: Node	<br>
	 * ProcedureDecl <: CodeDecl <: CodeBlock <: CodeNode <: Term <: Node
	 */
	@Override
	protected Ext extProcedureDeclImpl() {
//		return super.extProcedureDeclImpl();
		return new EquGenProcedureDeclExt();
	}
	
	/**
	 * Expr <: Term <: Node	<br>
	 * Expr <: Receiver <: Prefix <: Node
	 */
	@Override
	protected Ext extExprImpl() {
//		return super.extExprImpl();
		return new EquGenExprExt();
	}
	
	/**
	 * AmbExpr <: Expr <: Term <: Node	<br>
	 * AmbExpr <: Expr <: Receiver <: Prefix <: Node
	 */
	@Override
	protected Ext extAmbExprImpl() {
//		return super.extAmbExprImpl();
		return new EquGenAmbExprExt();
	}
	
	/**
	 * New <: Expr <: Term <: Node	<br>
	 * New <: Expr <: Receiver <: Prefix <: Node	<br>
	 * New <: ProcedureCall <: Term <: Node
	 */
	@Override
	protected Ext extNewImpl() {
//		return super.extNewImpl();
		return new EquGenNewExt();
	}
	
	/**
	 * Call <: Expr <: Term <: Node	<br>
	 * Call <: Expr <: Receiver <: Prefix <: Node	<br>
	 * Call <: ProcedureCall <: Term <: Node
	 */
	@Override
	protected Ext extCallImpl() {
//		return super.extCallImpl();
		return new EquGenCallExt();
	}
	
	/**
	 * NewArray <: Expr <: Term <: Node	<br>
	 * NewArray <: Expr <: Receiver <: Prefix <: Node
	 */
	@Override
	protected Ext extNewArrayImpl() {
//		return super.extNewArrayImpl();
		return new EquGenNewArrayExt();
	}
	
	/**
	 * ArrayInit <: Expr <: Term <: Node	<br>
	 * ArrayInit <: Expr <: Receiver <: Prefix <: Node
	 */
	@Override
	protected Ext extArrayInitImpl() {
//		return super.extArrayInitImpl();
		return new EquGenArrayInitExt();
	}
	
	/**
	 * Conditional <: Expr <: Term <: Node	<br>
	 * Conditional <: Expr <: Receiver <: Prefix <: Node
	 */
	@Override
	protected Ext extConditionalImpl() {
//		return super.extConditionalImpl();
		return new EquGenConditionalExt();
	}
	
	/**
	 * Special <: Expr <: Term <: Node	<br>
	 * Special <: Expr <: Receiver <: Prefix <: Node
	 */
	@Override
	protected Ext extSpecialImpl() {
//		return super.extSpecialImpl();
		return new EquGenSpecialExt();
	}
	
	/**
	 * Cast <: Expr <: Term <: Node	<br>
	 * Cast <: Expr <: Receiver <: Prefix <: Node
	 */
	@Override
	protected Ext extCastImpl() {
//		return super.extCastImpl();
		return new EquGenCastExt();
	}
	
	/**
	 * Instanceof <: Expr <: Term <: Node	<br>
	 * Instanceof <: Expr <: Receiver <: Prefix <: Node
	 */
	@Override
	protected Ext extInstanceofImpl() {
//		return super.extCastImpl();
		return new EquGenInstanceofExt();
	}
	
	/**
	 * Unary <: Expr <: Term <: Node	<br>
	 * Unary <: Expr <: Receiver <: Prefix <: Node
	 */
	@Override
	protected Ext extUnaryImpl() {
//		return super.extUnaryImpl();
		return new EquGenUnaryExt();
	}
	
	/**
	 * Binary <: Expr <: Term <: Node	<br>
	 * Binary <: Expr <: Receiver <: Prefix <: Node
	 */
	@Override
	protected Ext extBinaryImpl() {
//		return super.extBinaryImpl();
		return new EquGenBinaryExt();
	}
	
	/**
	 * Lit <: Expr <: Term <: Node	<br>
	 * Lit <: Expr <: Receiver <: Prefix <: Node
	 */
	@Override
	protected Ext extLitImpl() {
//		return super.extLitImpl();
		return new EquGenLitExt();
	}
	
	/**
	 * ClassLit <: Lit <: Expr <: Term <: Node	<br>
	 * ClassLit <: Lit <: Expr <: Receiver <: Prefix <: Node
	 */
	@Override
	protected Ext extClassLitImpl() {
//		return super.extClassLitImpl();
		return new EquGenClassLitExt();
	}
	
	/**
	 * ArrayAccess <: Variable <: Expr <: Term <: Node	<br>
	 * ArrayAccess <: Variable <: Expr <: Receiver <: Prefix <: Node
	 */
	@Override
	protected Ext extArrayAccessImpl() {
//		return super.extArrayAccessImpl();
		return new EquGenArrayAccessExt();
	}
	
	/**
	 * Field <: NamedVariable <: Variable <: Expr <: Term <: Node	<br>
	 * Field <: NamedVariable <: Variable <: Expr <: Receiver <: Prefix <: Node
	 */
	@Override
	protected Ext extFieldImpl() {
//		return super.extFieldImpl();
		return new EquGenFieldExt();
	}
	
	/**
	 * Local <: NamedVariable <: Variable <: Expr <: Term <: Node	<br>
	 * Local <: NamedVariable <: Variable <: Expr <: Receiver <: Prefix <: Node
	 */
	@Override
	protected Ext extLocalImpl() {
//		return super.extLocalImpl();
		return new EquGenLocalExt();
	}
	
	/**
	 * Assign <: Expr <: Term <: Node	<br>
	 * Assign <: Expr <: Receiver <: Prefix <: Node
	 */
	@Override
	protected Ext extAssignImpl() {
//		return super.extAssignImpl();
		return new EquGenAssignExt();
	}
	
	/**
	 * FieldAssign <: Assign <: Expr <: Term <: Node	<br>
	 * FieldAssign <: Assign <: Expr <: Receiver <: Prefix <: Node
	 */
	@Override
	protected Ext extFieldAssignImpl() {
//		return super.extAssignImpl();
		return new EquGenFieldAssignExt();
	}
	
	/**
	 * Stmt <: Term <: Node
	 */
	@Override
	protected Ext extStmtImpl() {
//		return super.extStmtImpl();
		return new EquGenStmtExt();
	}
	
	/**
	 * ConstructorCall <: Stmt <: Term <: Node	<br>
	 * ConstructorCall <: ProcedureCall <: Term <: Node
	 */
	@Override
	protected Ext extConstructorCallImpl() {
//		return super.extConstructorCallImpl();
		return new EquGenConstructorCallExt();
	}
	
	/**
	 * LocalDecl <: ForInit <: Stmt <: Term <: Node	<br>
	 * LocalDecl <: VarDecl <: Term <: Node
	 */
	@Override
	protected Ext extLocalDeclImpl() {
//		return super.extLocalDeclmpl();
		return new EquGenLocalDeclExt();
	}
	
	/**
	 * Block <: CompoundStmt <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extBlockImpl() {
//		return super.extBlockImpl();
		return new EquGenBlockExt();
	}
	
	/**
	 * Return <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extReturnImpl() {
//		return super.extReturnImpl();
		return new EquGenReturnExt();
	}
	
	/**
	 * Formal <: VarDecl <: Term <: Node
	 */
	@Override
	protected Ext extFormalImpl() {
//		return super.extFormalImpl();
		return new EquGenFormalExt();
	}
}