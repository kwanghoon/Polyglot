package tool.compiler.java.ast;

import polyglot.ast.Initializer;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * Initializer <: CodeDecl <: ClassMember <: Term <: Node			<br>
 * Initializer <: CodeDecl <: CodeBlock <: CodeNode <: Term <: Node
 */
public class EquGenInitializerExt extends EquGenClassDeclExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Initializer init = (Initializer)this.node();
		Report.report(2, "[Enter] Initializer: " + init);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Initializer init = (Initializer)this.node();
		
		Report.report(2, "[Leave] Initializer: " + init);
		return super.equGenLeave(v);
	}	
}