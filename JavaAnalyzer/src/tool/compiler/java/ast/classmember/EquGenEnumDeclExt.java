package tool.compiler.java.ast.classmember;

import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * ClassDecl <: ClassMember <: Term <: Node	<br>
 * ClassDecl <: CodeNode <: Term <: Node
 */
public class EquGenEnumDeclExt extends EquGenClassDeclExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Enum Declaration";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
//		ReportUtil.enterReport(this);
//		ClassDecl enumDecl = (ClassDecl)this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
//		ReportUtil.leaveReport(this);
//		ClassDecl enumDecl = (ClassDecl)this.node();
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}