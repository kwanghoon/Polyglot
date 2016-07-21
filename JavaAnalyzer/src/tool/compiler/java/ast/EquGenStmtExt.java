package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenStmtExt extends EquGenExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
//		Stmt stmt = (Stmt)this.node();
//		Report.report(0, "[Enter] Statement: " + stmt);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
//		Stmt stmt = (Stmt)this.node();
//		Report.report(0, "[Leave] Statement: " + stmt);
		
		return super.equGenLeave(v);
	}
}