package tool.compiler.java.ast;

import polyglot.ast.ConstructorDecl;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * ConstructorDecl <: ProcedureDecl <: CodeDecl <: ClassMember <: Term <: Node				<br>
 * ConstructorDecl <: ProcedureDecl <: CodeDecl <: CodeBlock <: CodeNode <: Term <: Node
 * @author LHJ
 */
public class EquGenConstructorDeclExt extends EquGenProcedureDeclExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ConstructorDecl ctorDecl = (ConstructorDecl) this.node();
		Report.report(2, "[Enter] Constructor Declaration: " + ctorDecl/*.name()*/);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ConstructorDecl ctorDecl = (ConstructorDecl) this.node();
		Report.report(2, "[Leave] Constructor Declaration: " + ctorDecl/*.name()*/);
		
		return super.equGenLeave(v);
	}
}