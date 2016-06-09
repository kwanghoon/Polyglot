package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ast.Special;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.MetaSetVariable;

/**
 * Special <: Expr <: Term <: Node					<br>
 * Special <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenSpecialExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Special spc = (Special) this.node();
		Report.report(0, "[Enter] Special: " + spc);
		
		if(spc.kind() == Special.THIS) {
			setMetaSetVar(v.getCurrCC().getChiThis());
		} else {	// TODO: 이 프로그램을 위해, super의 타입은 무엇으로 봐야 하는가? (자식 클래스? 부모 클래스?)
			setMetaSetVar(new MetaSetVariable(spc.type()));
		}
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Special spc = (Special) this.node();
		Report.report(0, "[Leave] Special: " + spc);
		
		return super.equGenLeave(v);
	}
}