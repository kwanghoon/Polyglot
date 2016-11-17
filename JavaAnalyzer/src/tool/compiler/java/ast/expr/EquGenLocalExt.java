package tool.compiler.java.ast.expr;

import polyglot.ast.Local;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5LocalInstance;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.env.TypeEnvironment;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * Local <: NamedVariable <: Variable <: Expr <: Term <: Node				<br>
 * Local <: NamedVariable <: Variable <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenLocalExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Local";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		Local lcl = (Local)this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		Local lcl = (Local) this.node();
		
		// 현재의 타입 환경에서 로컬 변수를 찾음 (타입 환경: 메서드/이니셜라이저 블록 내에서 유효한 변수 환경)
		MetaSetVariable tchi = v.peekTypeEnv().lookUp((JL5LocalInstance) lcl.localInstance());
		
		// 현재의 타입 환경에서 찾을 수 없는 경우 조상 타입 환경에서 찾음
		//     (메서드 A 내에서 선언된 Anonymous Class의 멤버 메서드 B에서, 메서드 A의 로컬 변수에 접근하는 경우로, final로 선언된 경우만 찾음)
		if (tchi == null && lcl.flags().isFinal()) {
			for (TypeEnvironment typeEnv : v.getAncestorTypeEnvs()) {
				tchi = typeEnv.lookUp((JL5LocalInstance) lcl.localInstance());
				if (tchi != null) {
					break;
				}
			}
		}
		ReportUtil.report(tchi, MetaSetVarSource.LocalEnvironment, MetaSetVarGoal.Return);
		
		setMetaSetVar(tchi);
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}