package tool.compiler.java.ast;

import polyglot.ast.Empty;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * Empty <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenEmptyExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Empty empty = (Empty)this.node();
		Report.report(2, "[Enter] Empty: " + empty);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Empty empty = (Empty)this.node();
		
		Report.report(2, "[Leave] Empty: " + empty);
		return super.equGenLeave(v);
	}
}