package tool.compiler.java.ast;

import polyglot.ast.LocalClassDecl;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * LocalClassDecl <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenLocalClassDeclExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		LocalClassDecl localClzDecl = (LocalClassDecl)this.node();
		Report.report(2, "[Enter] Local Class Declaration: " + localClzDecl);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		LocalClassDecl localClzDecl = (LocalClassDecl)this.node();
		Report.report(2, "[Leave] Local Class Declaration: " + localClzDecl);
		
		return super.equGenLeave(v);
	}
}