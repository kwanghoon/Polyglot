package tool.compiler.java.ast.classmember;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;

import polyglot.ast.Block;
import polyglot.ast.Formal;
import polyglot.ast.Node;
import polyglot.ast.ProcedureDecl;
import polyglot.ext.jl5.types.JL5LocalInstance;
import polyglot.ext.jl5.types.JL5ProcedureInstance;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.ast.stmt.EquGenStmtExt;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.env.MethodConstraint;
import tool.compiler.java.env.TypeEnvironment;
import tool.compiler.java.info.MethodInfo;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarGoal;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * ProcedureDecl <: CodeDecl <: ClassMember <: Term <: Node				<br>
 * ProcedureDecl <: CodeDecl <: CodeBlock <: CodeNode <: Term <: Node
 * @author LHJ
 */
public class EquGenProcedureDeclExt extends EquGenClassMemberExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Procedure Declaration";
	private MethodConstraint mc;
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
		ProcedureDecl procDecl = (ProcedureDecl) this.node();
		JL5ProcedureInstance procIns = (JL5ProcedureInstance) procDecl.procedureInstance();
		
		// Method Constraint
		mc = new MethodConstraint(procIns);
		mc.setOuter(v.getCurrCC());
		v.getCurrCC().addCodeConstraint(mc);
		v.addToSet(mc);
		ReportUtil.report(mc);
		
		// (선언) 메서드 인포 생성
		MethodInfo mtdInfo = new MethodInfo(procIns);
		v.addToSet(mtdInfo);
		ReportUtil.report(mtdInfo);
		
		// 로컬 환경 구성 (Formals를 Local Env.에 등록하는 과정 포함)
		TypeEnvironment topLevelEnv = v.pushTypeEnv();
		topLevelEnv.push();
		// TODO: CC의 Field처럼, 구별을 위해 타입인스턴스도 Map으로 함께 저장할 필요가 있을 것도 같음
		LinkedHashSet<MetaSetVariable> chiFormals = mc.getFormals();
		if (chiFormals != null) {
			Iterator<Formal> nodeFormals = procDecl.formals().iterator();
			for (MetaSetVariable chiFormal : chiFormals) {
				topLevelEnv.add((JL5LocalInstance) nodeFormals.next().localInstance(), chiFormal);
				ReportUtil.report(chiFormal, MetaSetVarSource.MethodEnvironment, MetaSetVarGoal.LocalEnvironment);
			}
		}
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		ProcedureDecl procDecl = (ProcedureDecl) this.node();
		
		// Method Body의 Effects를 Method의 Effects로 지정
		Block body = procDecl.body();
		if (body != null) {
			HashMap<EffectName, EffectSetVariable> effects = EquGenStmtExt.effects(body);
			for (EffectSetVariable effect : effects.values()) {
				mc.addEffect(effect);
				ReportUtil.report(effect, EffectSetVarSource.SubStatement, EffectSetVarGoal.MethodEnvironment);
			}
		}
		
		// 로컬 환경 해제
		v.popTypeEnv().pop();
		
		v.leaveInnerCF();
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}