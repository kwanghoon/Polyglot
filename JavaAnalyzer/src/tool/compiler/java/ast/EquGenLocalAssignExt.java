package tool.compiler.java.ast;

import polyglot.ast.Assign;
import polyglot.ast.LocalAssign;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5LocalInstance;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.AbstractObject;
import tool.compiler.java.visit.AbstractObject.Info;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.MetaSetVariable;
import tool.compiler.java.visit.ObjsSubseteqX;
import tool.compiler.java.visit.XSubseteqY;

/**
 * LocalAssign <: Assign <: Expr <: Term <: Node				<br>
 * LocalAssign <: Assign <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenLocalAssignExt extends EquGenAssignExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	private AbstractObject absObj;
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		LocalAssign lasgn = (LocalAssign) this.node();
		Report.report(2, "[Enter] Local Assign: " + lasgn);
		
		if(lasgn.operator() != Assign.ASSIGN) {
			absObj = new AbstractObject(lasgn, Info.LocapAssignOp);
			v.addToSet(absObj);
			Report.report(3, "\t[AbstractObject] "  + absObj + " (Object " + absObj.getType() + ")");
		}
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		LocalAssign lasgn = (LocalAssign) this.node();
		JL5LocalInstance localIns = (JL5LocalInstance) lasgn.left().localInstance();
		Report.report(2, "[Leave] Local Assign: " + lasgn);
		
		// l = e / l op= e
		//   1. l의 타입 C{Chi1}을 가져오고
		MetaSetVariable cchi1 = v.getLocalEnv().lookUp(localIns);
		Report.report(3, "\t[MetaSetVariable] " + cchi1 + " (For return: From Local Env.)");
		
		//   2A. l = e
		if(lasgn.operator() == Assign.ASSIGN) {
			//   2A-1. e의 타입 D{Chi2}를 가져오고
			MetaSetVariable dchi2 = metaSetVar(lasgn.right());
			
			//   2A-2. D{Chi2} <: C{Chi1} 제약식을 추가
			XSubseteqY xy = new XSubseteqY(dchi2, cchi1);
			v.getCurrMC().addMetaConstraint(xy);
			Report.report(3, "\t[XSubseteqY] " + xy);
		}
		
		//   2B. l op= e	(e.g. l += e)
		else {
			//   2B-1. C{o} <: C{Chi1} 제약식을 추가
			ObjsSubseteqX ox = new ObjsSubseteqX(absObj, cchi1);
			v.getCurrMC().addMetaConstraint(ox);
			Report.report(3, "\t[ObjsSubseteqX] " + ox);
			
			//   2B-2. TODO: lasgn.rignt()와 absObj의 관계를 표현해야 함.
		}
		
		//   3. C{Chi1}을 리턴할 타입으로 지정
		setMetaSetVar(cchi1);
		
		return super.equGenLeave(v);
	}
}