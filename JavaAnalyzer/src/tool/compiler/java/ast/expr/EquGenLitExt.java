package tool.compiler.java.ast.expr;

import polyglot.ast.ClassLit;
import polyglot.ast.Lit;
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
 * Lit <: Expr <: Term <: Node					<br>
 * Lit <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenLitExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Literal";
	
	private AbstractObject absObj;
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
		Lit lit = (Lit) this.node();
		
		if (!(lit instanceof ClassLit)) {
			absObj = new AbstractObject(lit);
			v.addToSet(absObj);
			ReportUtil.report(absObj);
//			Report.report(3, "\t[AbstractObject] " + absObj + " ( " + lit.getClass().getInterfaces()[0].getSimpleName() + " )");
		}
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		Lit lit = (Lit) this.node();
		
		// int / char / float / boolean / string / class / null
		//   1. T{Chi} 변수 생성
		MetaSetVariable tchi = MetaSetVariable.create(lit.type());
		ReportUtil.report(tchi, MetaSetVarSource.New, MetaSetVarGoal.Return);
		
		//   2. T{o} <: T{Chi} 제약식을 추가
		if (!(lit instanceof ClassLit)) {
			ObjsSubseteqX ox = new ObjsSubseteqX(absObj, tchi);
			v.getCurrCF().addMetaConstraint(ox);
			ReportUtil.report(ox);
		}
		
		//   3. return T{Chi}
		setMetaSetVar(tchi);
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}