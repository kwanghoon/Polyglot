package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * ConstructorDecl <: ProcedureDecl <: CodeDecl <: ClassMember <: Term <: Node				<br>
 * ConstructorDecl <: ProcedureDecl <: CodeDecl <: CodeBlock <: CodeNode <: Term <: Node
 * @author LHJ
 */
public class EquGenConstructorDeclExt extends EquGenProcedureDeclExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Constructor Declaration";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
//		ReportUtil.enterReport(this);
//		ConstructorDecl ctorDecl = (ConstructorDecl) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
//		ReportUtil.leaveReport(this);
//		ConstructorDecl ctorDecl = (ConstructorDecl) this.node();
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}