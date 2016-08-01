package tool.compiler.java.ast;

import java.util.Collection;

import polyglot.ast.Expr;
import polyglot.ast.FieldDecl;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5FieldInstance;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.ArrayMetaSetVariable;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.constraint.AssignField;
import tool.compiler.java.constraint.AssignStaticField;
import tool.compiler.java.constraint.XSubseteqY;
import tool.compiler.java.info.FieldInfo;
import tool.compiler.java.util.EquGenUtil;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * FieldDecl <: ClassMember <: Term <: Node	<br>
 * FieldDecl <: CodeNode <: Term <: Node
 * @author LHJ
 */
public class EquGenFieldDeclExt extends EquGenExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Field Declaration";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
		FieldDecl fldDecl = (FieldDecl) this.node();
		
		FieldInfo fldInfo = new FieldInfo((JL5FieldInstance) fldDecl.fieldInstance());
		v.addToSet(fldInfo);
		ReportUtil.report(fldInfo);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		FieldDecl fldDecl = (FieldDecl) this.node();
		JL5FieldInstance fldIns = (JL5FieldInstance) fldDecl.fieldInstance();
		
		// 필드 선언시 초기화 하는 경우에 한해
		Expr e2 = fldDecl.init();
		if(e2 != null) {
			// D f = e2 / static D f = e2
			//   1. e2의 타입 E{Chi3}를 가져오고
			MetaSetVariable echi3 = EquGenExprExt.metaSetVar(e2);
			ReportUtil.report(echi3, MetaSetVarSource.Rvalue, MetaSetVarGoal.Flow);
			
			//   2-1. 스칼라 변수, 또는 배열 변수의 Top Level의 MetaSetVariable의 데이터 플로우
			//   2-1A. D f = e2	(non-static)
			//         e1(this)의 타입 C{Chi1}을 가져오고, E{Chi3} <: C{Chi1}.f 제약식을 추가
			if(!fldIns.flags().isStatic()) {
				MetaSetVariable cchi1 = v.getCurrCC().getThis();		// 선언시이므로 무조건 this
				ReportUtil.report(cchi1, MetaSetVarSource.Receiver, MetaSetVarGoal.Flow);
				
				AssignField af = new AssignField(echi3, cchi1, fldIns);
				v.getCurrCC().addMetaConstraint(af);
				ReportUtil.report(af);
			}
			
			//   2-1B. static D f = e2	(static)
			//         E{Chi3} <: C.f 제약식을 추가
			else {
				AssignStaticField asf = new AssignStaticField(echi3, fldIns);
				v.getCurrCC().addMetaConstraint(asf);
				ReportUtil.report(asf);
			}
			
			//   2-2. 배열 변수인 경우, Top Level 아래의 MetaSetVariable의 데이터 플로우
			if(EquGenUtil.isArray(fldDecl.type().type())) {
				Collection<XSubseteqY> xys = EquGenUtil.constrain(
						(ArrayMetaSetVariable) echi3, 
						(ArrayMetaSetVariable) v.getCurrCC().getField(fldIns));	// D[]에 대한 MetaSetVariable
				v.getCurrMC().addMetaConstraints(xys);
			}
		}
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}