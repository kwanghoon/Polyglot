package tool.compiler.java.ast;

import polyglot.ast.ClassDecl;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5ParsedClassType;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.ClassConstraint;
import tool.compiler.java.visit.EquGenerator;

/**
 * ClassDecl <: ClassMember <: Term <: Node	<br>
 * ClassDecl <: CodeNode <: Term <: Node
 */
public class EquGenClassDeclExt extends EquGenExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	private ClassConstraint cc;
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ClassDecl clzDecl = (ClassDecl)this.node();
		JL5ParsedClassType classType = (JL5ParsedClassType) clzDecl.type();
		Report.report(2, "[Enter] Class Declaration: " + clzDecl/*.name()*/);
		
//		ArrayList<MetaSetVariable> chiFields = new ArrayList<>();
//		for(FieldInstance field : classType.fields()) {
//			chiFields.add(new MetaSetVariable(field.type()));
//		}
		
		// Class Constraint
		cc = new ClassConstraint(classType, true);
		v.addToSet(cc);
		Report.report(3, "\t[ClassConstraint] " + cc);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ClassDecl clzDecl = (ClassDecl)this.node();
		Report.report(2, "[Leave] Class Declaration: " + clzDecl/*.name()*/);
		
		return super.equGenLeave(v);
	}	
}