package tool.compiler.java.ast;

import polyglot.ast.ClassLit;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * ClassLit <: Lit <: Expr <: Term <: Node					<br>
 * ClassLit <: Lit <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenClassLitExt extends EquGenLitExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ClassLit clzLit = (ClassLit) this.node();
		Report.report(0, "[Enter] Class Lit: " + clzLit/*.name()*/);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ClassLit clzLit = (ClassLit) this.node();
		Report.report(0, "[Leave] Class Lit: " + clzLit/*.name()*/);
		
		return super.equGenLeave(v);
	}
}