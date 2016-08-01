package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * MethodDecl <: ProcedureDecl <: CodeDecl <: ClassMember <: Term <: Node				<br>
 * MethodDecl <: ProcedureDecl <: CodeDecl <: CodeBlock <: CodeNode <: Term <: Node
 * @author LHJ
 */
public class EquGenMethodDeclExt extends EquGenProcedureDeclExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Method Declaration";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
//		ReportUtil.enterReport(this);
//		MethodDecl methDecl = (MethodDecl) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
//		ReportUtil.leaveReport(this);
//		MethodDecl methDecl = (MethodDecl) this.node();
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}