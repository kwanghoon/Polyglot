package tool.compiler.java.ast.stmt;

import polyglot.ast.Block;
import polyglot.ast.Node;
import polyglot.ast.Stmt;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.env.LocalEnvironment;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.visit.EquGenerator;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Block <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenBlockExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Block";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		Block block = (Block) this.node();
		
		// 로컬 환경 구성
		v.peekTypeEnv().push();
		
		return super.equGenEnter(v);
	}
	 
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		Block block = (Block) this.node();
		
		// { stmt1 ... stmtn }
		final LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> x_effs = new LinkedHashMap<>();
		
		//   1. stmt1, ... , stmtn를 분석해서 나오는 effects(exn, activity)인 X_eff1, ... , X_effn를 찾아, 
		for (Stmt subStmt : block.statements()) {
			EquGenStmtExt.effects(subStmt, x_effs, EffectSetVarSource.SubStatement);
		}
		
		//   2. X_eff1 ∪ ... ∪ X_effn를 구하고, 이를 리턴할 effects(exn, activity)로 지정.
		setEffects(x_effs);
		
		// 로컬 환경 해제
		LocalEnvironment localEnv = v.peekTypeEnv().pop();
		setLocalEnv(localEnv);
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}