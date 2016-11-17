package tool.compiler.java.ast.expr;

import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * EnumConstant <: Field <: NamedVariable <: Variable <: Expr <: Term <: Node				<br>
 * EnumConstant <: Field <: NamedVariable <: Variable <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
@Deprecated
public class EquGenEnumConstantExt extends EquGenFieldExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Enum Constant";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
//		ReportUtil.enterReport(this);
//		EnumConstant enumConst = (EnumConstant)this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
//		ReportUtil.leaveReport(this);
//		EnumConstant enumConst = (EnumConstant)this.node();
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}