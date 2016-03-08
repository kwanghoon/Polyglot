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
	 * ConstructorCall <: Stmt <: Term <: Node	<br>
	 * ConstructorCall <: ProcedureCall <: Term <: Node
	 */
	@Override
	protected Ext extConstructorCallImpl() {
//		return super.extConstructorCallImpl();
		return new EquGenConstructorCallExt();
	}
	
	/**
	 * Block <: CompoundStmt <: Stmt <: Term <: Node
	 */
	@Override
	protected Ext extBlockImpl() {
//		return super.extBlockImpl();
		return new EquGenBlockExt();
	}
}