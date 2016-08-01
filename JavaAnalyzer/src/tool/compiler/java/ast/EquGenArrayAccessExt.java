package tool.compiler.java.ast;

import polyglot.ast.ArrayAccess;
import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.ArrayMetaSetVariable;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * ArrayAccess <: Variable <: Expr <: Term <: Node				<br>
 * ArrayAccess <: Variable <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenArrayAccessExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Array Access";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		ArrayAccess arrAccess = (ArrayAccess) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		ArrayAccess arrAccess = (ArrayAccess) this.node();
		
		// arr[idx] (ArrayAccess의 index는 하나임)
		ArrayMetaSetVariable arrMSV = (ArrayMetaSetVariable) metaSetVar(arrAccess.array());
		MetaSetVariable tchi = arrMSV.base();
		ReportUtil.report(tchi, MetaSetVarSource.ArrayBase, MetaSetVarGoal.Return);
		
		setMetaSetVar(tchi);
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}