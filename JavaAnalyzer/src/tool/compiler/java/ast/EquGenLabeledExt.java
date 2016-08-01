package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.visit.EquGenerator;

/**
 * Labeled <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenLabeledExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Labeled";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		Labeled labeled = (Labeled)this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
//		Labeled labeled = (Labeled)this.node();
		
		setLocalEnv(v.getTypeEnv().getCurrEnv());
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}