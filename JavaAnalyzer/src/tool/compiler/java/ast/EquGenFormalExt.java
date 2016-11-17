package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.visit.EquGenerator;

/**
 * Formal <: VarDecl <: Term <: Node
 * @author LHJ
 */
@Deprecated
public class EquGenFormalExt extends EquGenExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Formal";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		Formal fm = (Formal) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
//		Formal fm = (Formal) this.node();
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}