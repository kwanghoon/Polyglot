package tool.compiler.java.ast.classmember;

import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.ast.EquGenExt;
import tool.compiler.java.visit.EquGenerator;

/**
 * ClassMember <: Term <: Node
 * @author LHJ
 */
public class EquGenClassMemberExt extends EquGenExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Class Member";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
//		ReportUtil.enterReport(this);
//		ClassMember clzMem = (ClassMember)this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
//		ReportUtil.leaveReport(this);
//		ClassMember clzMem = (ClassMember)this.node();
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}