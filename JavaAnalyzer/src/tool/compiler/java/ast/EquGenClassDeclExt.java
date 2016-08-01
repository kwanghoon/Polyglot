package tool.compiler.java.ast;

import polyglot.ast.ClassDecl;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5ParsedClassType;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.env.ClassConstraint;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.visit.EquGenerator;

/**
 * ClassDecl <: ClassMember <: Term <: Node	<br>
 * ClassDecl <: CodeNode <: Term <: Node
 */
public class EquGenClassDeclExt extends EquGenExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Class Declaration";
	
	private ClassConstraint cc;
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
		ClassDecl clzDecl = (ClassDecl)this.node();
		JL5ParsedClassType classType = (JL5ParsedClassType) clzDecl.type();
		
//		ArrayList<MetaSetVariable> chiFields = new ArrayList<>();
//		for(FieldInstance field : classType.fields()) {
//			chiFields.add(new MetaSetVariable(field.type()));
//		}
		
		// Class Constraint
		cc = new ClassConstraint(classType, true);
		v.addToSet(cc);
		ReportUtil.report(cc);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
//		ClassDecl clzDecl = (ClassDecl)this.node();
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}