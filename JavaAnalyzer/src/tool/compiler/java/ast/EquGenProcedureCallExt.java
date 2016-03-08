package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ast.ProcedureCall;
import polyglot.ext.jl5.types.JL5ProcedureInstance;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.MethodCallInfo;

/**
 * ProcedureCall <: Term <: Node
 * @author LHJ
 */
public class EquGenProcedureCallExt extends EquGenExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ProcedureCall pc = (ProcedureCall) this.node();
//		Report.report(0, "Procedure Call: " + pc);
		
		// (호출) 메서드 인포 생성
		MethodCallInfo mtdInfo = new MethodCallInfo((JL5ProcedureInstance) pc.procedureInstance());
		v.addToSet(mtdInfo);
		Report.report(0, "Procedure Call (" + this.getClass().getSimpleName() + "): " + pc + ": " + mtdInfo);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGen(EquGenerator v) {
		return super.equGen(v);
	}
}