package tool.compiler.java.ast;

import polyglot.ast.ClassDecl;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5ParsedClassType;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.env.ClassConstraint;
import tool.compiler.java.env.ConstraintFunction;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.visit.EquGenerator;

/**
 * ClassDecl <: ClassMember <: Term <: Node	<br>
 * ClassDecl <: CodeNode <: Term <: Node
 */
public class EquGenClassDeclExt extends EquGenClassMemberExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Class Declaration";
	
	private ClassConstraint cc;
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
		ClassDecl clzDecl = (ClassDecl)this.node();
		JL5ParsedClassType classType = (JL5ParsedClassType) clzDecl.type();
		
		// Class Constraint
		cc = new ClassConstraint(classType);
		ConstraintFunction outerCF = v.getCurrCF();
		if (outerCF != null) {
			cc.setOuter(outerCF);	// Outer Class/Method 설정
			outerCF.addInner(cc);	// Inner Class 설정
		}
		v.addToSet(cc);				// Vistor에, CC를 keep하고 현재 CC를 갱신
		ReportUtil.report(cc);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
//		ClassDecl clzDecl = (ClassDecl)this.node();
		
		// Inner Class 탈출 (현재 CC 갱신)
		v.leaveInnerCF();
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}