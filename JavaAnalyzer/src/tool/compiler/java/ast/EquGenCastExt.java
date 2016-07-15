package tool.compiler.java.ast;

import polyglot.ast.Cast;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.MetaSetVariable;
import tool.compiler.java.visit.XSubseteqY;

/**
 * Cast <: Expr <: Term <: Node					<br>
 * Cast <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenCastExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();

	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Cast cast = (Cast) this.node();
		Report.report(2, "[Enter] Cast: " + cast);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Cast cast = (Cast) this.node();
		Report.report(2, "[Leave] Cast: " + cast);
		
		// (D) e
		//   1. e의 타입 C{Chi1}를 가져온 다음
		MetaSetVariable cchi1 = MetaSetVar(cast.expr());
		
		//   2. 리턴할 타입 D{Chi2}를 만든다. (Chi는 새로 만들고 D는 이 노드 자신의 타입)
		MetaSetVariable dchi2 = new MetaSetVariable(cast.type());
		
		//   3. C{Chi1} <: D{Chi2} 제약식을 추가
		XSubseteqY xy = new XSubseteqY(cchi1, dchi2);
		v.getCurrMC().addMetaConstraint(xy);
		Report.report(3, "\t[XSubseteqY] " + xy);
		
		//   4. D{Chi2}를 리턴 타입으로 지정
		setMetaSetVar(dchi2);
		Report.report(3, "\t[MetaSetVariable] " + dchi2 + " (new)");
		
		return super.equGenLeave(v);
	}
}