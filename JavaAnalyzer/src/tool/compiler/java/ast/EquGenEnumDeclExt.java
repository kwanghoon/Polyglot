package tool.compiler.java.ast;

import polyglot.ast.ClassDecl;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * ClassDecl <: ClassMember <: Term <: Node	<br>
 * ClassDecl <: CodeNode <: Term <: Node
 */
public class EquGenEnumDeclExt extends EquGenClassDeclExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ClassDecl enumDecl = (ClassDecl)this.node();
		Report.report(2, "[Enter] Enum Declaration: " + enumDecl);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ClassDecl enumDecl = (ClassDecl)this.node();
		
		Report.report(2, "[Leave] Enum Declaration: " + enumDecl);
		return super.equGenLeave(v);
	}	
}