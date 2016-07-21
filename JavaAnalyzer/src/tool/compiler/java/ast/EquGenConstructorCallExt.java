package tool.compiler.java.ast;

import polyglot.ast.ConstructorCall;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5ConstructorInstance;
import polyglot.ext.jl5.types.JL5ProcedureInstance;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.MethodCallInfo;

/**
 * ConstructorCall <: Stmt <: Term <: Node	<br>
 * ConstructorCall <: ProcedureCall <: Term <: Node
 * @author LHJ
 */
public class EquGenConstructorCallExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ConstructorCall ctorCall = (ConstructorCall) this.node();
		JL5ProcedureInstance porcIns = (JL5ProcedureInstance) ctorCall.procedureInstance();
		Report.report(2, "[Enter] Constructor Call: " + ctorCall);
		
		// (호출) 메서드 인포 생성
		MethodCallInfo mtdInfo = new MethodCallInfo(porcIns);
		v.addToSet(mtdInfo);
		Report.report(3, "\t[MethodCallInfo] " + mtdInfo);
		
//		ArrayList<TypedSetVariable> argSetVars = new ArrayList<>();
//		for(Expr arg: cc.arguments()) {
//			argSetVars.add(typedSetVar(arg));
//		}
//		
//		JL5MethodInstance ins = (JL5MethodInstance) cc.methodInstance();
//		v.addToSet(new InvokeMth(typedSetVar(), ins, argSetVars, new TypedSetVariable(call.type())));
		
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ConstructorCall ctorCall = (ConstructorCall) this.node();
		JL5ConstructorInstance ctorIns = (JL5ConstructorInstance) ctorCall.constructorInstance();
		Report.report(2, "[Leave] Constructor Call: " + ctorCall);
		
		return super.equGenLeave(v);
	}
}