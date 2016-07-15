package tool.compiler.java.ast;

import polyglot.ast.Conditional;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.MetaSetVariable;
import tool.compiler.java.visit.XSubseteqY;

/**
 * Conditional <: Expr <: Term <: Node					<br>
 * Conditional <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenConditionalExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Conditional cond = (Conditional) this.node();
		Report.report(2, "[Enter] Conditional: " + cond);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Conditional cond = (Conditional) this.node();
		Report.report(2, "[Leave] Conditional: " + cond);
		
		// e ? e1 : e2
		//   1. e1의 타입 T1{Chi1}와 e2의 타입 T2{Chi2}를 가져온 다음
		MetaSetVariable t1chi1 = MetaSetVar(cond.consequent());
		MetaSetVariable t2chi2 = MetaSetVar(cond.alternative());
		
		//   2. 리턴할 타입 T{Chi}를 만든다. (Chi는 새로 만들고 T는 이 노드 자신의 타입)
		MetaSetVariable tchi = new MetaSetVariable(cond.type());
		
		//   3. T1{Chi1} <: T{Chi}와 T2{Chi2} <: T{Chi} 제약식을 추가
		XSubseteqY xy1 = new XSubseteqY(t1chi1, tchi);
		XSubseteqY xy2 = new XSubseteqY(t2chi2, tchi);
		v.getCurrMC().addMetaConstraint(xy1);
		v.getCurrMC().addMetaConstraint(xy2);
		Report.report(3, "\t[XSubseteqY] " + xy1);
		Report.report(3, "\t[XSubseteqY] " + xy2);
		
		//   4. T{Chi}를 리턴 타입으로 지정
		setMetaSetVar(tchi);
		Report.report(3, "\t[MetaSetVariable] " + tchi + " (new)");
		
		return super.equGenLeave(v);
	}
}