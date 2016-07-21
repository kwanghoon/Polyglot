package tool.compiler.java.ast;

import polyglot.ast.Field;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5FieldInstance;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.ReadField;
import tool.compiler.java.visit.ReadStaticField;
import tool.compiler.java.visit.MetaSetVariable;

/**
 * Field <: NamedVariable <: Variable <: Expr <: Term <: Node					<br>
 * Field <: NamedVariable <: Variable <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenFieldExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Field fld = (Field)this.node();
		Report.report(2, "[Enter] Field: " + fld/*.name()*/);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Field fld = (Field)this.node();
		JL5FieldInstance fldIns = (JL5FieldInstance) fld.fieldInstance();
		Report.report(2, "[Leave] Field: " + fld/*.name()*/);
		
		// e.f / C.f
		//   1. f의 타입 D를 가지오고, 새로운 집합변수 Chi2를 생성해서 D{Chi2}를 만들기 (구현은 CC에서 가져오도록 되어 있음)
		MetaSetVariable dchi2 = MetaSetVariable.create(fld.type());
//		MetaSetVariable dchi2 = new MetaSetVariable(fld.type());
		Report.report(3, "\t[MetaSetVariable] " + dchi2 + " (For return: New)");
//		MetaSetVariable dchi2 = v.getCurrCC().getField(fldIns);
//		Report.report(3, "\t[MetaSetVariable] " + dchi2 + " (For return: From Class Constraint)");
		
		//   2A. e.f	(non-static)
		//       e의 타입 C{Chi1}을 가져오고, C{Chi1}.f <: D{Chi2}를 제약식 집합에 추가
		if(!fldIns.flags().isStatic()) {
			MetaSetVariable cchi1 = EquGenExt.metaSetVar(fld.target());
			ReadField rf = new ReadField(cchi1, fldIns, dchi2);
			v.getCurrMC().addMetaConstraint(rf);
			Report.report(3, "\t[ReadField] " + rf);
		}
		
		//   2B. C.f	(static)
		//       C.f <: D{Chi2}를 제약식 집합에 추가
		else {
			ReadStaticField rsf = new ReadStaticField(fldIns, dchi2);
			v.getCurrMC().addMetaConstraint(rsf);
			Report.report(3, "\t[ReadStaticField] " + rsf);
		}
		
		//   3. D{Chi2}를 e.f의 타입으로 리턴
		setMetaSetVar(dchi2);
		
		return super.equGenLeave(v);
	}
}