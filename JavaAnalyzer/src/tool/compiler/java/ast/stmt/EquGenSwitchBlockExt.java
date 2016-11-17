package tool.compiler.java.ast.stmt;

import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * SwitchBlock <: Block <: CompoundStmt <: Stmt <: Term <: Node	<br>
 * SwitchBlock <: SwitchElement <: Stmt <: Term <: Node
 * @author LHJ
 */
@Deprecated
public class EquGenSwitchBlockExt extends EquGenBlockExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Switch Block";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
//		ReportUtil.enterReport(this);
//		SwitchBlock switchBlock = (SwitchBlock) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
//		ReportUtil.leaveReport(this);
//		SwitchBlock switchBlock = (SwitchBlock) this.node();
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}