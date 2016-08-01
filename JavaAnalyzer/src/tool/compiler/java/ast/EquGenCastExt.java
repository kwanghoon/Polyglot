package tool.compiler.java.ast;

import polyglot.ast.Cast;
import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.constraint.XSubseteqY;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * Cast <: Expr <: Term <: Node					<br>
 * Cast <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenCastExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Cast";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		Cast cast = (Cast) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		Cast cast = (Cast) this.node();
		
		// (D) e
		//   1. 리턴할 타입 D{Chi2}를 만든 다음, (Chi는 새로 만들고 D는 이 노드 자신의 타입)
		MetaSetVariable dchi2 = new MetaSetVariable(cast.type());
		ReportUtil.report(dchi2, MetaSetVarSource.New, MetaSetVarGoal.Return);
		
		//   2. e의 타입 C{Chi1}를 가져온다.
		MetaSetVariable cchi1 = metaSetVar(cast.expr());
		ReportUtil.report(cchi1, MetaSetVarSource.SubExpression, MetaSetVarGoal.Flow);
		
		//   3. C{Chi1} <: D{Chi2} 제약식을 추가
		XSubseteqY xy = new XSubseteqY(cchi1, dchi2);
		v.getCurrMC().addMetaConstraint(xy);
		ReportUtil.report(xy);
		
		//   4. D{Chi2}를 리턴 타입으로 지정
		setMetaSetVar(dchi2);
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}