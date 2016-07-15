package tool.compiler.java.ast;

import polyglot.ast.LocalAssign;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5LocalInstance;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.MetaSetVariable;
import tool.compiler.java.visit.XSubseteqY;

/**
 * LocalAssign <: Assign <: Expr <: Term <: Node				<br>
 * LocalAssign <: Assign <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenLocalAssignExt extends EquGenAssignExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		LocalAssign lasgn = (LocalAssign) this.node();
		Report.report(2, "[Enter] Local Assign: " + lasgn);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		LocalAssign lasgn = (LocalAssign) this.node();
		JL5LocalInstance localIns = (JL5LocalInstance) lasgn.left().localInstance();
		Report.report(2, "[Leave] Local Assign: " + lasgn);
		
		// l = e
		//   1. l의 타입 C{Chi1}와 e의 타입 D{Chi2}를 가져오고
		MetaSetVariable cchi1 = v.getLocalEnv().lookUp(localIns);
		MetaSetVariable dchi2 = MetaSetVar(lasgn.right());
		
		//   2. D{Chi2} <: C{Chi1} 제약식을 추가
		XSubseteqY xy = new XSubseteqY(dchi2, cchi1);
		v.getCurrMC().addMetaConstraint(xy);
		Report.report(3, "\t[XSubseteqY] " + xy);
		
		//   3. C{Chi1}을 리턴할 타입으로 지정
		setMetaSetVar(cchi1);
		Report.report(3, "\t[MetaSetVariable] " + cchi1);
		
		return super.equGenLeave(v);
	}
}