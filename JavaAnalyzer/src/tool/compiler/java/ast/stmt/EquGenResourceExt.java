package tool.compiler.java.ast.stmt;

import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * LocalDecl <: ForInit <: Stmt <: Term <: Node	<br>
 * LocalDecl <: VarDecl <: Term <: Node
 * @author LHJ
 */
@Deprecated
public class EquGenResourceExt extends EquGenLocalDeclExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Resource";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
//		ReportUtil.enterReport(this);
//		LocalDecl res = (LocalDecl) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
//		ReportUtil.leaveReport(this);
//		LocalDecl res = (LocalDecl) this.node();
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}