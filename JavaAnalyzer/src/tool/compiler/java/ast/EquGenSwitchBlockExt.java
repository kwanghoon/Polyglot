package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ast.SwitchBlock;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * SwitchBlock <: Block <: CompoundStmt <: Stmt <: Term <: Node	<br>
 * SwitchBlock <: SwitchElement <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenSwitchBlockExt extends EquGenBlockExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		SwitchBlock switchBlock = (SwitchBlock)this.node();
		Report.report(2, "[Enter] Switch Block: " + switchBlock);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		SwitchBlock switchBlock = (SwitchBlock)this.node();
		
		Report.report(2, "[Leave] Switch Block: " + switchBlock);
		return super.equGenLeave(v);
	}
}