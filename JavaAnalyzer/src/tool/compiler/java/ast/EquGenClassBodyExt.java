package tool.compiler.java.ast;

import polyglot.ast.ClassBody;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * ClassBody <: Term <: Node
 */
public class EquGenClassBodyExt extends EquGenExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ClassBody clzBody = (ClassBody)this.node();
		Report.report(2, "[Enter] Class Body: " + clzBody);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ClassBody clzBody = (ClassBody)this.node();
		
		Report.report(2, "[Leave] Class Body: " + clzBody);
		return super.equGenLeave(v);
	}	
}