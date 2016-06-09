package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ast.Return;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * Return <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenReturnExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Return rtn = (Return)this.node();
		Report.report(0, "[Enter] Return: " + rtn/*.name()*/);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Return rtn = (Return)this.node();
		Report.report(0, "[Leave] Return: " + rtn/*.name()*/);
		
		v.getCurrMC().setChiRet(MetaSetVar(rtn.expr()));
		
		return super.equGenLeave(v);
	}
}