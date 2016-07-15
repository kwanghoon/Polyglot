package tool.compiler.java.ast;

import polyglot.ast.ArrayAccess;
import polyglot.ast.Local;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5LocalInstance;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.MetaSetVariable;

/**
 * ArrayAccess <: Variable <: Expr <: Term <: Node				<br>
 * ArrayAccess <: Variable <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenArrayAccessExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();

	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ArrayAccess arrAccess = (ArrayAccess) this.node();
		Report.report(2, "[Enter] Array Access: " + arrAccess);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ArrayAccess arrAccess = (ArrayAccess) this.node();
		Report.report(2, "[Leave] Array Access: " + arrAccess);
		
		// TODO: arrAccess.array()을 Local로 캐스팅할 때 캐스팅 익셉션이 발생하는지 확인 필요
		JL5LocalInstance localInst = (JL5LocalInstance) ((Local)arrAccess.array()).localInstance();
		MetaSetVariable rtnMsv = v.getLocalEnv().lookUp(localInst);
		setMetaSetVar(rtnMsv);
		
		Report.report(3, "\t[MetaSetVariable] " + rtnMsv);
		return super.equGenLeave(v);
	}
}