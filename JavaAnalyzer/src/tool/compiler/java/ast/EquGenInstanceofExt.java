package tool.compiler.java.ast;

import polyglot.ast.Instanceof;
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
 * Instanceof <: Expr <: Term <: Node				<br>
 * Instanceof <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenInstanceofExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Instanceof";
	
	private AbstractObject absObj;
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
		Instanceof insof = (Instanceof) this.node();
		
		absObj = new AbstractObject(insof);
		v.addToSet(absObj);
		ReportUtil.report(absObj);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		Instanceof insof = (Instanceof) this.node();
		
		// instanceof
		//   1. boolean{Chi} 변수 생성
		MetaSetVariable tchi = new MetaSetVariable(insof.type());
		ReportUtil.report(tchi, MetaSetVarSource.New, MetaSetVarGoal.Return);
		
		//   2. boolean{o} <: boolean{Chi} 제약식을 추가
		ObjsSubseteqX ox = new ObjsSubseteqX(absObj, tchi);
		v.getCurrMC().addMetaConstraint(ox);
		ReportUtil.report(ox);
		
		//   3. return boolean{Chi}
		setMetaSetVar(tchi);
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}