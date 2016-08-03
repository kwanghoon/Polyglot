package tool.compiler.java.ast;

import java.util.Collection;

import polyglot.ast.ArrayAccessAssign;
import polyglot.ast.Assign;
import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.AbstractObject;
import tool.compiler.java.aos.ArrayMetaSetVariable;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.aos.AbstractObject.Info;
import tool.compiler.java.constraint.ObjsSubseteqX;
import tool.compiler.java.constraint.XSubseteqY;
import tool.compiler.java.util.EquGenUtil;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * ArrayAccessAssign <: Assign <: Expr <: Term <: Node				<br>
 * ArrayAccessAssign <: Assign <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenArrayAccessAssignExt extends EquGenAssignExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Array Access Assign";
	
	private AbstractObject absObj;
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
		ArrayAccessAssign aasgn = (ArrayAccessAssign) this.node();
		
		if(aasgn.operator() != Assign.ASSIGN) {
			absObj = new AbstractObject(aasgn, Info.ArrayAccessAssignOp);
			v.addToSet(absObj);
			ReportUtil.report(absObj);
		}
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		ArrayAccessAssign aasgn = (ArrayAccessAssign) this.node();
		
		// a[i] = e / a[i] op= e	(a는 Field/Local 둘 다 가능)
		//   1. a[i]의 타입 C{Chi1}을 가져오고
		MetaSetVariable cchi1 = metaSetVar(aasgn.left());
		ReportUtil.report(cchi1, MetaSetVarSource.Lvalue, MetaSetVarGoal.Return);
		
		//   2A. a[i] = e
		if(aasgn.operator() == Assign.ASSIGN) {
			//   2A-1. e의 타입 D{Chi2}를 가져오고
			MetaSetVariable dchi2 = metaSetVar(aasgn.right());
			ReportUtil.report(dchi2, MetaSetVarSource.Rvalue, MetaSetVarGoal.Flow);
			
			//   2A-2. D{Chi2} <: C{Chi1} 제약식을 추가
			XSubseteqY xy = new XSubseteqY(dchi2, cchi1);
			v.getCurrCF().addMetaConstraint(xy);
			ReportUtil.report(xy);
			
			//   2A-3. 배열 변수인 경우, Top Level 아래의 MetaSetVariable의 데이터 플로우
			if(EquGenUtil.isArray(cchi1)) {
				Collection<XSubseteqY> xys = EquGenUtil.constrain(
						(ArrayMetaSetVariable) dchi2, 
						(ArrayMetaSetVariable) cchi1);
				v.getCurrCF().addMetaConstraints(xys);
			}
		}
		
		//   2B. a[i] op= e	(e.g. a[i] += e)
		else {
			//   2B-1. C{o} <: C{Chi1} 제약식을 추가
			ObjsSubseteqX ox = new ObjsSubseteqX(absObj, cchi1);
			v.getCurrCF().addMetaConstraint(ox);
			ReportUtil.report(ox);
			
			//   2B-2. TODO: aasgn.rignt()와 absObj의 관계를 표현해야 함.
		}
		
		//   3. C{Chi1}을 리턴할 타입으로 지정
		setMetaSetVar(cchi1);
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}