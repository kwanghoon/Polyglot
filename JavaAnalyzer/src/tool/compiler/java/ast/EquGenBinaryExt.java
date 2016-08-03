package tool.compiler.java.ast;

import polyglot.ast.Binary;
import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.AbstractObject;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.constraint.ObjsSubseteqX;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * Binary <: Expr <: Term <: Node				<br>
 * Binary <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenBinaryExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Binary";
	
	private AbstractObject absObj;
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
		Binary binary = (Binary) this.node();
		
		absObj = new AbstractObject(binary);
		v.addToSet(absObj);
		ReportUtil.report(absObj);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		Binary binary = (Binary) this.node();
		
		// e1 > e2 / e1 < e2 / e1 == e2 / e1 <= e2 / e1 >= e2 / e1 != e2 /
		// e1 || e2 / e1 && e2 / e1 + e2 / e1 - e2 / e1 * e2 / e1 / e2 / e1 % e2 /
		// e1 | e2 / e1 & e2 / e1 ^ e2 / e1 << e2 / e1 >> e2 / e1 >>> e2
		//   1. T{Chi} 변수 생성
		MetaSetVariable tchi = new MetaSetVariable(binary.type());
		ReportUtil.report(tchi, MetaSetVarSource.New, MetaSetVarGoal.Return);
		
		//   2. T{o} <: T{Chi} 제약식을 추가
		ObjsSubseteqX ox = new ObjsSubseteqX(absObj, tchi);
		v.getCurrCF().addMetaConstraint(ox);
		ReportUtil.report(ox);
		
		//   3. return T{Chi}
		setMetaSetVar(tchi);
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}