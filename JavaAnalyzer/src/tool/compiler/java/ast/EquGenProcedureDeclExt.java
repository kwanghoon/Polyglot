package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ast.ProcedureDecl;
import polyglot.ext.jl5.types.JL5ProcedureInstance;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.env.MethodConstraint;
import tool.compiler.java.env.TypeEnvironment;
import tool.compiler.java.info.MethodInfo;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.visit.EquGenerator;

/**
 * ProcedureDecl <: CodeDecl <: ClassMember <: Term <: Node				<br>
 * ProcedureDecl <: CodeDecl <: CodeBlock <: CodeNode <: Term <: Node
 * @author LHJ
 */
public class EquGenProcedureDeclExt extends EquGenExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Procedure Declaration";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
		ProcedureDecl procDecl = (ProcedureDecl) this.node();
		JL5ProcedureInstance procIns = (JL5ProcedureInstance) procDecl.procedureInstance();
		
		// 로컬 환경 구성
		v.setTypeEnv(new TypeEnvironment());
		v.getTypeEnv().push();
		
		// MethodConstraint
		MethodConstraint mc = new MethodConstraint(procIns);
		v.addToSet(mc);
		ReportUtil.report(mc);
		
		// (선언) 메서드 인포 생성
		MethodInfo mtdInfo = new MethodInfo(procIns);
		v.addToSet(mtdInfo);
		ReportUtil.report(mtdInfo);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		ProcedureDecl procDecl = (ProcedureDecl) this.node();
		JL5ProcedureInstance procIns = (JL5ProcedureInstance) procDecl.procedureInstance();
		
		// 로컬 환경 해제
		v.getTypeEnv().pop();
		
		// T m(T1 x1, ... Tn xn) { ... }ㅣ,
		//   1. local env를 x1:T1{X1}, xn:Tn{Xn}으로 초기화
		//         X1~Xn은 method table에 기록된 TypedSetVariable들임
		
		// TODO: 구현 필요
		
		
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}