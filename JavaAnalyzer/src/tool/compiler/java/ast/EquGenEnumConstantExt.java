package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ext.jl5.ast.EnumConstant;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

/**
 * EnumConstant <: Field <: NamedVariable <: Variable <: Expr <: Term <: Node				<br>
 * EnumConstant <: Field <: NamedVariable <: Variable <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenEnumConstantExt extends EquGenFieldExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		EnumConstant enumConst = (EnumConstant)this.node();
		Report.report(2, "[Enter] Enum Constant: " + enumConst/*.name()*/);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		EnumConstant enumConst = (EnumConstant)this.node();
		
		Report.report(2, "[Leave] Enum Constant: " + enumConst/*.name()*/);
		return super.equGenLeave(v);
	}
}