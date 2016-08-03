package tool.compiler.java.ast;

import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5FieldInstance;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.constraint.ReadField;
import tool.compiler.java.constraint.ReadStaticField;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * Field <: NamedVariable <: Variable <: Expr <: Term <: Node					<br>
 * Field <: NamedVariable <: Variable <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenFieldExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Field";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		Field fld = (Field)this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		Field f = (Field)this.node();
		JL5FieldInstance fldIns = (JL5FieldInstance) f.fieldInstance();
		
		// e.f / C.f
		//   1. f의 타입 D를 가지오고, 새로운 집합변수 Chi2를 생성해서 D{Chi2}를 만들기
		MetaSetVariable dchi2 = MetaSetVariable.create(f.type());
//		MetaSetVariable dchi2 = new MetaSetVariable(fld.type());
		ReportUtil.report(dchi2, MetaSetVarSource.New, MetaSetVarGoal.Return);
//		MetaSetVariable dchi2 = v.getCurrCC().getField(fldIns);
//		Report.report(3, "\t[MetaSetVariable] " + dchi2 + " (For return: From Class Constraint)");
		
		//   2A. e.f	(non-static)
		//       e의 타입 C{Chi1}을 가져오고, C{Chi1}.f <: D{Chi2}를 제약식 집합에 추가
		if(!fldIns.flags().isStatic()) {
			MetaSetVariable cchi1 = EquGenExprExt.metaSetVar((Expr)f.target());
			ReportUtil.report(cchi1, MetaSetVarSource.Receiver, MetaSetVarGoal.Flow);
			
			ReadField rf = new ReadField(cchi1, fldIns, dchi2);
			v.getCurrCF().addMetaConstraint(rf);
			ReportUtil.report(rf);
		}
		
		//   2B. C.f	(static)
		//       C.f <: D{Chi2}를 제약식 집합에 추가
		else {
			ReadStaticField rsf = new ReadStaticField(fldIns, dchi2);
			v.getCurrCF().addMetaConstraint(rsf);
			ReportUtil.report(rsf);
		}
		
		//   3. D{Chi2}를 e.f의 타입으로 리턴
		setMetaSetVar(dchi2);
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}