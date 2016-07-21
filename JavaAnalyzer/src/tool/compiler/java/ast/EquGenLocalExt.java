package tool.compiler.java.ast;

import polyglot.ast.Local;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5LocalInstance;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.MetaSetVariable;

/**
 * Local <: NamedVariable <: Variable <: Expr <: Term <: Node				<br>
 * Local <: NamedVariable <: Variable <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenLocalExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Local lcl = (Local)this.node();
		Report.report(2, "[Enter] Local: " + lcl/*.name()*/);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Local lcl = (Local)this.node();
		Report.report(2, "[Leave] Local: " + lcl/*.name()*/);
		
		MetaSetVariable rtnMsv = v.getLocalEnv().lookUp((JL5LocalInstance) lcl.localInstance());
		Report.report(3, "\t[MetaSetVariable] " + rtnMsv + " (For return: From Local Env.)");
		
		setMetaSetVar(rtnMsv);
		
		return super.equGenLeave(v);
	}
}