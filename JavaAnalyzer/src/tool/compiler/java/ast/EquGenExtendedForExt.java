package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ext.jl5.ast.ExtendedFor;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * ExtendedFor <: Loop <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenExtendedForExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ExtendedFor forLoop = (ExtendedFor)this.node();
		Report.report(2, "[Enter] Extended For: " + forLoop);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ExtendedFor forLoop = (ExtendedFor)this.node();
		Report.report(2, "[Leave] Extended For: " + forLoop);
		
		return super.equGenLeave(v);
	}
}