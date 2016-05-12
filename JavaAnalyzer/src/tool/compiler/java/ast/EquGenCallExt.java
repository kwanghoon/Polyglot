package tool.compiler.java.ast;

import java.util.ArrayList;

import polyglot.ast.Call;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5MethodInstance;
import polyglot.ext.jl5.types.JL5ProcedureInstance;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.InvokeMth;
import tool.compiler.java.visit.InvokeStaticMth;
import tool.compiler.java.visit.MethodCallInfo;
import tool.compiler.java.visit.TypedSetVariable;

/**
 * Call <: Expr <: Term <: Node					<br>
 * Call <: Expr <: Receiver <: Prefix <: Node	<br>
 * Call <: ProcedureCall <: Term <: Node
 * @author LHJ
 */
public class EquGenCallExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Call call = (Call) this.node();
		Report.report(0, "[Enter] Call: " + call/*.name()*/);
		
		// (호출) 메서드 인포 생성
		MethodCallInfo mtdInfo = new MethodCallInfo((JL5ProcedureInstance) call.procedureInstance());
		v.addToSet(mtdInfo);
		Report.report(0, "Call: " + call + ": " + mtdInfo);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Call call = (Call) this.node();
		JL5MethodInstance mthIns = (JL5MethodInstance) call.methodInstance();
		Report.report(0, "[Leave] Call: " + call/*.name()*/);
		
		// e.m(e1, ..., en) / C.m(e1, ..., en)
		//   1. e1~en의 타입 Ci{Xi}를 가져온 다음
		ArrayList<TypedSetVariable> csxs = new ArrayList<>();
		for(Expr arg: call.arguments()) {
			csxs.add(EquGenExt.typedSetVar(arg));
		}
		
		//   2. 리턴할 타입 D{X}를 만든다. (X는 새로 만들고 D는 이 노드 자신의 타입)
		TypedSetVariable dx = new TypedSetVariable(call.type());
		
		//   3-1. e의 타입 C{X0}를 가져오고, C{X0}.m <: (C1{X1}, ... , Cn{Xn}) -> D{X} 제약식을 추가
		if(!mthIns.flags().isStatic()) {
			TypedSetVariable cx0 = EquGenExt.typedSetVar(call.target());
			InvokeMth im = new InvokeMth(cx0, mthIns, csxs, dx);
			v.addToSet(im);
			Report.report(1, "[Constraint] InvokeMth: " + im);
		}
		
		//   3-2. C.m <: (C1{X1}, ... , Cn{Xn}) -> D{X} 제약식을 추가
		else {
			InvokeStaticMth ism = new InvokeStaticMth(mthIns, csxs, dx);
			v.addToSet(ism);
			Report.report(1, "[Constraint] InvokeStaticMth: " + ism);
		}
		
		//   4. D{X}를 리턴 타입으로 지정
		setTypedSetVar(dx);
		
		return super.equGenLeave(v);
	}
}