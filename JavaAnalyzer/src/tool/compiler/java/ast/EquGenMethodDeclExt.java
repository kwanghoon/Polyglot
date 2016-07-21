package tool.compiler.java.ast;

import polyglot.ast.MethodDecl;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * MethodDecl <: ProcedureDecl <: CodeDecl <: ClassMember <: Term <: Node				<br>
 * MethodDecl <: ProcedureDecl <: CodeDecl <: CodeBlock <: CodeNode <: Term <: Node
 * @author LHJ
 */
public class EquGenMethodDeclExt extends EquGenProcedureDeclExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		MethodDecl methDecl = (MethodDecl) this.node();
		Report.report(2, "[Enter] Method Declaration: " + methDecl/*.name()*/);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		MethodDecl methDecl = (MethodDecl) this.node();
		Report.report(2, "[Leave] Method Declaration: " + methDecl/*.name()*/);
		
		return super.equGenLeave(v);
	}
}