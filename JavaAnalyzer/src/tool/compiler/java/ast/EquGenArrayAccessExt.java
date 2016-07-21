package tool.compiler.java.ast;

import polyglot.ast.ArrayAccess;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.ArrayMetaSetVariable;
import tool.compiler.java.visit.EquGenerator;

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
		
		// arr[idx] (ArrayAccess의 index는 하나임)
		ArrayMetaSetVariable arr = (ArrayMetaSetVariable) metaSetVar(arrAccess.array());
		Report.report(3, "\t[MetaSetVariable] " + arr + " (For return: From array)");
		
		setMetaSetVar(arr.base());
		
		return super.equGenLeave(v);
	}
}