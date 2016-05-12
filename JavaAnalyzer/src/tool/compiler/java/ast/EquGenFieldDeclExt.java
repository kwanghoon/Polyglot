package tool.compiler.java.ast;

import polyglot.ast.FieldDecl;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5FieldInstance;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.FieldInfo;

/**
 * FieldDecl <: ClassMember <: Term <: Node	<br>
 * FieldDecl <: CodeNode <: Term <: Node
 * @author LHJ
 */
public class EquGenFieldDeclExt extends EquGenExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		FieldDecl fldDecl = (FieldDecl) this.node();
//		Report.report(0, "Field Declaration: " + fldDecl/*.name()*/);
		
		FieldInfo fldInfo = new FieldInfo((JL5FieldInstance) fldDecl.fieldInstance());
		v.addToSet(fldInfo);
		Report.report(0, "Field Declaration: " + fldInfo);
		
		if(fldDecl.init() != null) {
			
		}
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		return super.equGenLeave(v);
	}
}