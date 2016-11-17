package tool.compiler.java.ast;

import polyglot.ast.Ext;
import polyglot.ast.ExtFactory;
import tool.compiler.java.ast.classmember.EquGenClassDeclExt;
import tool.compiler.java.ast.classmember.EquGenClassMemberExt;
import tool.compiler.java.ast.classmember.EquGenEnumDeclExt;
import tool.compiler.java.ast.classmember.EquGenFieldDeclExt;
import tool.compiler.java.ast.classmember.EquGenInitializerExt;
import tool.compiler.java.ast.classmember.EquGenProcedureDeclExt;
import tool.compiler.java.ast.expr.EquGenArrayAccessAssignExt;
import tool.compiler.java.ast.expr.EquGenArrayAccessExt;
import tool.compiler.java.ast.expr.EquGenArrayInitExt;
import tool.compiler.java.ast.expr.EquGenAssignExt;
import tool.compiler.java.ast.expr.EquGenBinaryExt;
import tool.compiler.java.ast.expr.EquGenCallExt;
import tool.compiler.java.ast.expr.EquGenCastExt;
import tool.compiler.java.ast.expr.EquGenConditionalExt;
import tool.compiler.java.ast.expr.EquGenEnumConstantExt;
import tool.compiler.java.ast.expr.EquGenExprExt;
import tool.compiler.java.ast.expr.EquGenFieldAssignExt;
import tool.compiler.java.ast.expr.EquGenFieldExt;
import tool.compiler.java.ast.expr.EquGenInstanceofExt;
import tool.compiler.java.ast.expr.EquGenLitExt;
import tool.compiler.java.ast.expr.EquGenLocalAssignExt;
import tool.compiler.java.ast.expr.EquGenLocalExt;
import tool.compiler.java.ast.expr.EquGenNewArrayExt;
import tool.compiler.java.ast.expr.EquGenNewExt;
import tool.compiler.java.ast.expr.EquGenSpecialExt;
import tool.compiler.java.ast.expr.EquGenUnaryExt;
import tool.compiler.java.ast.stmt.EquGenAssertExt;
import tool.compiler.java.ast.stmt.EquGenBlockExt;
import tool.compiler.java.ast.stmt.EquGenBranchExt;
import tool.compiler.java.ast.stmt.EquGenCaseExt;
import tool.compiler.java.ast.stmt.EquGenCatchExt;
import tool.compiler.java.ast.stmt.EquGenConstructorCallExt;
import tool.compiler.java.ast.stmt.EquGenDoExt;
import tool.compiler.java.ast.stmt.EquGenEmptyExt;
import tool.compiler.java.ast.stmt.EquGenEvalExt;
import tool.compiler.java.ast.stmt.EquGenExtendedForExt;
import tool.compiler.java.ast.stmt.EquGenForExt;
import tool.compiler.java.ast.stmt.EquGenIfExt;
import tool.compiler.java.ast.stmt.EquGenLabeledExt;
import tool.compiler.java.ast.stmt.EquGenLocalClassDeclExt;
import tool.compiler.java.ast.stmt.EquGenLocalDeclExt;
import tool.compiler.java.ast.stmt.EquGenMultiCatchExt;
import tool.compiler.java.ast.stmt.EquGenResourceExt;
import tool.compiler.java.ast.stmt.EquGenReturnExt;
import tool.compiler.java.ast.stmt.EquGenStmtExt;
import tool.compiler.java.ast.stmt.EquGenSwitchBlockExt;
import tool.compiler.java.ast.stmt.EquGenSwitchExt;
import tool.compiler.java.ast.stmt.EquGenSynchronizedExt;
import tool.compiler.java.ast.stmt.EquGenThrowExt;
import tool.compiler.java.ast.stmt.EquGenTryExt;
import tool.compiler.java.ast.stmt.EquGenTryWithResourcesExt;
import tool.compiler.java.ast.stmt.EquGenWhileExt;

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
	 * ClassMember <: Term <: Node
	 */
	@Override
	protected Ext extClassMemberImpl() {
//		return super.extClassMemberImpl();
		return new EquGenClassMemberExt();
	}
	
	/**
	 * ClassDecl <: ClassMember <: Term <: Node	<br>
	 * ClassDecl <: CodeNode <: Term <: Node
	 */
	@Override
	protected Ext extClassDeclImpl() {
//		return super.extClassDeclImpl();
		return new EquGenClassDeclExt();
	}
	
	/**
	 * ClassDecl <: ClassMember <: Term <: Node	<br>
	 * ClassDecl <: CodeNode <: Term <: Node
	 */
	@Override
	protected Ext extEnumDeclImpl() {
//		return super.extEnumDeclImpl();
		return new EquGenEnumDeclExt();
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
	 * Initializer <: CodeDecl <: ClassMember <: Term <: Node	<br>
	 * Initializer <: CodeDecl <: CodeBlock <: CodeNode <: Term <: Node
	 */
	@Override
	protected Ext extInitializerImpl() {
//		return super.extInitializerImpl();
		return new EquGenInitializerExt();
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
	@Deprecated
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
	 * EnumConstant <: Field <: NamedVariable <: Variable <: Expr <: Term <: Node	<br>
	 * EnumConstant <: Field <: NamedVariable <: Variable <: Expr <: Receiver <: Prefix <: Node
	 */
	@Override
	protected Ext extEnumConstantImpl() {
//		return super.extEnumConstantImpl();
		return new EquGenEnumConstantExt();
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
//		return super.extFieldAssignImpl();
		return new EquGenFieldAssignExt();
	}
	
	/**
	 * LocalAssign <: Assign <: Expr <: Term <: Node	<br>
	 * LocalAssign <: Assign <: Expr <: Receiver <: Prefix <: Node
	 */
	@Override
	protected Ext extLocalAssignImpl() {
//		return super.extLocalAssignImpl();
		return new EquGenLocalAssignExt();
	}
	
	/**
	 * ArrayAccessAssign <: Assign <: Expr <: Term <: Node	<br>
	 * ArrayAccessAssign <: Assign <: Expr <: Receiver <: Prefix <: Node
	 */
	@Override
	protected Ext extArrayAccessAssignImpl() {
//		return super.extArrayAccessAssignImpl();
		return new EquGenArrayAccessAssignExt();
	}
	
	/**
	 * Formal <: VarDecl <: Term <: Node
	 */
	@Override
	protected Ext extFormalImpl() {
//		return super.extFormalImpl();
		return new EquGenFormalExt();
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
	 * Assert <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extAssertImpl() {
//		return super.extAssertImpl();
		return new EquGenAssertExt();
	}
	
	/**
	 * Empty <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extEmptyImpl() {
//		return super.extEmptyImpl();
		return new EquGenEmptyExt();
	}
	
	/**
	 * LocalClassDecl <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extLocalClassDeclImpl() {
//		return super.extLocalClassDeclImpl();
		return new EquGenLocalClassDeclExt();
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
	 * Throw <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extThrowImpl() {
//		return super.extThrowImpl();
		return new EquGenThrowExt();
	}
	
	/**
	 * Branch <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extBranchImpl() {
//		return super.extBranchImpl();
		return new EquGenBranchExt();
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
//		return super.extLocalDeclImpl();
		return new EquGenLocalDeclExt();
	}
	
	/**
	 * LocalDecl <: ForInit <: Stmt <: Term <: Node	<br>
	 * LocalDecl <: VarDecl <: Term <: Node
	 */
	@Override
	protected Ext extResourceImpl() {
//		return super.extResourcIempl();
		return new EquGenResourceExt();
	}
	
	/**
	 * Eval <: ForInit <: Stmt <: Term <: Node	<br>
	 * Eval <: ForUpdate <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extEvalImpl() {
//		return super.extEvalImpl();
		return new EquGenEvalExt();
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
	 * If <: CompoundStmt <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extIfImpl() {
//		return super.extIfImpl();
		return new EquGenIfExt();
	}
	
	/**
	 * Labeled <: CompoundStmt <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extLabeledImpl() {
//		return super.extLabeledImpl();
		return new EquGenLabeledExt();
	}
	
	/**
	 * Synchronized <: CompoundStmt <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extSynchronizedImpl() {
//		return super.extSynchronizedImpl();
		return new EquGenSynchronizedExt();
	}
	
	/**
	 * Try <: CompoundStmt <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extTryImpl() {
//		return super.extTryImpl();
		return new EquGenTryExt();
	}
	
	/**
	 * TryWithResources <: Try <: CompoundStmt <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extTryWithResourcesImpl() {
//		return super.extTryWithResourcesImpl();
		return new EquGenTryWithResourcesExt();
	}
	
	/**
	 * Catch <: CompoundStmt <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extCatchImpl() {
//		return super.extCatchImpl();
		return new EquGenCatchExt();
	}
	
	/**
	 * MultiCatch <: Catch <: CompoundStmt <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extMultiCatchImpl() {
//		return super.extMultiCatchImpl();
		return new EquGenMultiCatchExt();
	}
	
	/**
	 * Do <: Loop <: CompoundStmt <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extDoImpl() {
//		return super.extDoImpl();
		return new EquGenDoExt();
	}
	
	/**
	 * While <: Loop <: CompoundStmt <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extWhileImpl() {
//		return super.extWhileImpl();
		return new EquGenWhileExt();
	}
	
	/**
	 * For <: Loop <: CompoundStmt <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extForImpl() {
//		return super.extForImpl();
		return new EquGenForExt();
	}
	
	/**
	 * ExtendedFor <: Loop <: CompoundStmt <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extExtendedForImpl() {
//		return super.extExtendedForImpl();
		return new EquGenExtendedForExt();
	}
	
	/**
	 * Switch <: CompoundStmt <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extSwitchImpl() {
//		return super.extSwitchImpl();
		return new EquGenSwitchExt();
	}
	
	/**
	 * SwitchBlock <: Block <: CompoundStmt <: Stmt <: Term <: Node	<br>
	 * SwitchBlock <: SwitchElement <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extSwitchBlockImpl() {
//		return super.extSwitchBlockImpl();
		return new EquGenSwitchBlockExt();
	}
	
	/**
	 * Case <: SwitchElement <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extCaseImpl() {
//		return super.extCaseImpl();
		return new EquGenCaseExt();
	}
}