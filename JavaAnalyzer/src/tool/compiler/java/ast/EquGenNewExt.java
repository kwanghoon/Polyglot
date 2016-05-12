package tool.compiler.java.ast;

import java.util.ArrayList;

import polyglot.ast.Expr;
import polyglot.ast.New;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5ConstructorInstance;
import polyglot.ext.jl5.types.JL5ProcedureInstance;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.AbstractObjectInfo;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.InvokeMth;
import tool.compiler.java.visit.MethodCallInfo;
import tool.compiler.java.visit.ObjsSubseteqX;
import tool.compiler.java.visit.TypedSetVariable;

/**
 * New <: Expr <: Term <: Node					<br>
 * New <: Expr <: Receiver <: Prefix <: Node	<br>
 * New <: ProcedureCall <: Term <: Node
 * @author LHJ
 */
public class EquGenNewExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	private AbstractObjectInfo absObjInfo;
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		New nw = (New) this.node();
		JL5ConstructorInstance ctorIns = (JL5ConstructorInstance) nw.constructorInstance();
//		Report.report(0, "[Enter] New: " + nw);
		
		absObjInfo = new AbstractObjectInfo(ctorIns);
		v.addToSet(absObjInfo);
		
		// (호출) 메서드 인포 생성
		MethodCallInfo mtdInfo = new MethodCallInfo((JL5ProcedureInstance) nw.procedureInstance());
		v.addToSet(mtdInfo);
		
		Report.report(0, "[Enter] New: " + absObjInfo + ": " + mtdInfo);

		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		New nw = (New) this.node();
		JL5ConstructorInstance ctorIns = (JL5ConstructorInstance) nw.constructorInstance();
		Report.report(0, "[Leave] New: " + nw);
		
		// C<T1,...,Tn>{o} <: C<T1,...,Tn>{X}
		//  1. C<T1,...,Tn>{X} 변수 생성
		TypedSetVariable ctsx = new TypedSetVariable(ctorIns.container());
		
		//  2-1. C<T1,...,Tn>{o} <: C<T1,...,Tn>{X}
		ObjsSubseteqX o = new ObjsSubseteqX(absObjInfo, ctsx);
		v.addToSet(o);
		
		// C(e1, ..., en)
		//   2-2a. e1~en의 타입 Ci{Xi}를 가져온 다음
		ArrayList<TypedSetVariable> argSetVars = new ArrayList<>();
		for(Expr arg: nw.arguments()) {
			argSetVars.add(EquGenExt.typedSetVar(arg));
		}
		
		//   2-2b. C<T1,...,Tn>{X}.C <: (C1{X1}, ... , Cn{Xn}) -> D{X} 제약식을 추가
		InvokeMth im = new InvokeMth(ctsx, ctorIns, argSetVars, null);
		v.addToSet(im);
		
		//  3. return C<T1,...,Tn>{X}
		setTypedSetVar(ctsx);
		
		return super.equGenLeave(v);
	}
}