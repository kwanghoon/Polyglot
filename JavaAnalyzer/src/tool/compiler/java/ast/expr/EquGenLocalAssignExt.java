package tool.compiler.java.ast.expr;

import java.util.Collection;

import polyglot.ast.Assign;
import polyglot.ast.LocalAssign;
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
 * LocalAssign <: Assign <: Expr <: Term <: Node				<br>
 * LocalAssign <: Assign <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenLocalAssignExt extends EquGenAssignExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Local Assign";
	
	private AbstractObject absObj;
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
		LocalAssign lasgn = (LocalAssign) this.node();
		
		if(lasgn.operator() != Assign.ASSIGN) {
			absObj = new AbstractObject(lasgn, Info.LocalAssignOp);
			v.addToSet(absObj);
			ReportUtil.report(absObj);
		}
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		LocalAssign lasgn = (LocalAssign) this.node();
		
		// l = e / l op= e
		//   1. l의 타입 C{Chi1}을 가져오고
		MetaSetVariable cchi1 = metaSetVar(lasgn.left());
		ReportUtil.report(cchi1, MetaSetVarSource.Lvalue, MetaSetVarGoal.Return);
		
		//   2A. l = e
		if(lasgn.operator() == Assign.ASSIGN) {
			//   2A-1. e의 타입 D{Chi2}를 가져오고
			MetaSetVariable dchi2 = metaSetVar(lasgn.right());
			ReportUtil.report(dchi2, MetaSetVarSource.Rvalue, MetaSetVarGoal.Flow);
			
			//   2A-2. D{Chi2} <: C{Chi1} 제약식을 추가
			XSubseteqY xy = new XSubseteqY(dchi2, cchi1);
			v.getCurrCF().addMetaConstraint(xy);
			ReportUtil.report(xy);
			
			//   2A-3. 배열 변수인 경우, D{Chi2} <: C{Chi1}의 하위 레벨 제약식을 추가
			//	         (Top Level 아래의 MetaSetVariable(s)의 데이터 플로우)
			if(EquGenUtil.isArray(cchi1)) {
				Collection<XSubseteqY> xys = XSubseteqY.constrain(
						dchi2, (ArrayMetaSetVariable) cchi1);
				v.getCurrCF().addMetaConstraints(xys);
			}
		}
		
		//   2B. l op= e	(e.g. l += e)
		else {
			//   2B-1. C{o} <: C{Chi1} 제약식을 추가
			ObjsSubseteqX ox = new ObjsSubseteqX(absObj, cchi1);
			v.getCurrCF().addMetaConstraint(ox);
			ReportUtil.report(ox);
			
			//   2B-2. TODO: lasgn.rignt()와 absObj의 관계를 표현해야 함.
		}
		
		//   3. C{Chi1}을 리턴할 타입으로 지정
		setMetaSetVar(cchi1);
		
		//   4. l의 effect인 X_eff1와 e의 effect인 X_eff2의 합집합 
		//      X_eff1 ∪ X_eff2을 리턴할 effect로 지정 (EquGenAssignExt)
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}