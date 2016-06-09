package tool.compiler.java.ast;

import polyglot.ast.Local;
import polyglot.ast.Node;
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
		Report.report(0, "[Enter] Local: " + lcl/*.name()*/);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Local lcl = (Local)this.node();
		Report.report(0, "[Leave] Local: " + lcl/*.name()*/);
		
		// TODO: 아래는 임시 코드로, MetaSetVariable를 생성하고 가져오는 책임 관계를 다시 생각하여 작성할 것
		//setMetaSetVar(new MetaSetVariable(lcl.type()));
		setMetaSetVar(new MetaSetVariable(lcl.type()));
		v.getCurrMC().addChiLocal(MetaSetVar());
		
		return super.equGenLeave(v);
	}
}