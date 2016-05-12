package tool.compiler.java.ast;

import polyglot.ast.Block;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * Block <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenBlockExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Block blk = (Block)this.node();
		Report.report(0, "[Enter] Block: " + blk);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Block blk = (Block)this.node();
		Report.report(0, "[Leave] Block: " + blk);
		
		return super.equGenLeave(v);
	}
}