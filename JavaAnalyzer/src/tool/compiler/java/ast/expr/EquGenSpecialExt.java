package tool.compiler.java.ast.expr;

import polyglot.ast.Node;
import polyglot.ast.Special;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * Special <: Expr <: Term <: Node					<br>
 * Special <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenSpecialExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Special";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		Special spc = (Special) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		Special spc = (Special) this.node();
		
		// this / super
		MetaSetVariable cchi;
		if(spc.kind() == Special.THIS) {
			cchi = v.getCurrCC().getThis(spc.qualifier());
			ReportUtil.report(cchi, MetaSetVarSource.ClassEnvironment, MetaSetVarGoal.Return);
		} else {	// TODO: 이 프로그램을 위해, super의 타입은 무엇으로 봐야 하는가? (자식 클래스? 부모 클래스?)
			cchi = v.getCurrCC().getSuper(spc.qualifier());
			ReportUtil.report(cchi, MetaSetVarSource.ClassEnvironment, MetaSetVarGoal.Return);
		}
		
		setMetaSetVar(cchi);
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}