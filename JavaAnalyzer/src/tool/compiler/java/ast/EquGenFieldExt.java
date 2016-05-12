package tool.compiler.java.ast;

import polyglot.ast.Field;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5FieldInstance;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.ReadField;
import tool.compiler.java.visit.ReadStaticField;
import tool.compiler.java.visit.TypedSetVariable;

/**
 * Field <: NamedVariable <: Variable <: Expr <: Term <: Node					<br>
 * Field <: NamedVariable <: Variable <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenFieldExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
//		Field fld = (Field)this.node();
//		Report.report(0, "[Enter] Field: " + fld/*.name()*/);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Field fld = (Field)this.node();
		JL5FieldInstance fldIns = (JL5FieldInstance) fld.fieldInstance();
		Report.report(0, "[Leave] Field: " + fld/*.name()*/);
		
		// e.f / C.f
		//  1. f의 타입 D를 가지오고, 새로운 집합변수 X2를 생성해서 D{X2}를 만들기
		TypedSetVariable dx2 = new TypedSetVariable(fld.type());
		
		//  2-1. e의 타입 C{X1}을 가져오고, C{X1}.f <: D{X2}를 제약식 집합에 추가 (non-static)
		if(!fldIns.flags().isStatic()) {
			TypedSetVariable cx1 = EquGenExt.typedSetVar(fld.target());
			ReadField rf = new ReadField(cx1, fldIns, dx2);
			v.addToSet(rf);
			Report.report(1, "[Constraint] ReadField: " + rf);
		}
		
		//  2-2. C.f <: D{X2}를 제약식 집합에 추가 (static)
		else {
			ReadStaticField rsf = new ReadStaticField(fldIns, dx2);
			v.addToSet(rsf);
			Report.report(1, "[Constraint] ReadStaticField: " + rsf);
		}
		
		//  3. D{X2}를 e.f의 타입으로 리턴
		setTypedSetVar(dx2);
		
		return super.equGenLeave(v);
	}
}