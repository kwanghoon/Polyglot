package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ast.ProcedureDecl;
import polyglot.ext.jl5.types.JL5ProcedureInstance;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.MethodInfo;

/**
 * ProcedureDecl <: CodeDecl <: ClassMember <: Term <: Node	<br>
 * ProcedureDecl <: CodeDecl <: CodeBlock <: CodeNode <: Term <: Node
 * @author LHJ
 */
public class EquGenProcedureDeclExt extends EquGenExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ProcedureDecl procDecl = (ProcedureDecl) this.node();
		JL5ProcedureInstance procIns = (JL5ProcedureInstance) procDecl.procedureInstance();
//		Report.report(0, "[Enter] Procedure Declaration: " + pcdDecl/*.name()*/);
		
		// (선언) 메서드 인포 생성
		MethodInfo mtdInfo = new MethodInfo(procIns);
		v.addToSet(mtdInfo);
		Report.report(0, "[Enter] Procedure Declaration: " + mtdInfo);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ProcedureDecl procDecl = (ProcedureDecl) this.node();
		JL5ProcedureInstance procIns = (JL5ProcedureInstance) procDecl.procedureInstance();
		Report.report(0, "[Leave] Procedure Declaration: " + procDecl/*.name()*/);
		
		// T m(T1 x1, ... Tn xn) { ... }ㅣ,
		//   1. local env를 x1:T1{X1}, xn:Tn{Xn}으로 초기화
		//         X1~Xn은 method table에 기록된 TypedSetVariable들임
		
		// TODO: 구현 필요
		
		return super.equGenLeave(v);
	}
}