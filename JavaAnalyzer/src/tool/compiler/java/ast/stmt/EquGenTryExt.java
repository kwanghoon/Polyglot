package tool.compiler.java.ast.stmt;

import polyglot.ast.Block;
import polyglot.ast.Catch;
import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ast.Try;
import polyglot.util.Pair;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.effect.EffectDifference;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSet;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.effect.EffectUnion;
import tool.compiler.java.effect.ExnEffectElem;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarGoal;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.visit.EquGenerator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Try <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenTryExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Try";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		Try tryStmt = (Try) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		Try tryStmt = (Try) this.node();
		
		// 주의! 여기서는 EffectSetVariable이 소모될 때 - 흘러갈 곳을 찾았을 때 - Report함!
		
		// try ( stmt_res0; ... ; stmt_resm ) { stmt0 } catch (C1 e1) { stmt1 } ... catch (Ck ek) { stmtk } finally { stmtn } 	(단, k = n-1)
		final LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> x_effs = new LinkedHashMap<>();
		
		//   1. stmt_res0, ... , stmt_resm를 분석해서 나오는 effects(exn, activity)s의 집합 X_eff_res을 가져오고, 
		final List<LocalDecl> resources = resources();
		if (resources != null) {
			EquGenStmtExt.effects(resources, x_effs, EffectSetVarSource.SubStatement);
		}
		
		//   2. stmt0을 분석하면 나오는 effects(exn, activity)인 X_eff0를 가져온 다음, 
		EquGenStmtExt.effects(tryStmt.tryBlock(), x_effs, EffectSetVarSource.SubStatement);
		
		//   3. exn effect인 경우, X_eff_res ∪ X_eff0 ∖ X_C를 구한다.
		final List<Catch> catchBlocks = tryStmt.catchBlocks();
		final Map<EffectSetVariable, EffectSetVarSource> exnEffects = x_effs.get(EffectName.ExnEff);
		if (catchBlocks != null && !catchBlocks.isEmpty()) {	// X_C가 공집합이면 차집합하는 것이 의미 없음
			if (exnEffects != null && !exnEffects.isEmpty()) {	// X_eff_res와 X_eff0가 둘 다 공집합이면 X_C를 차집합하는 것이 의미 없음
				//   3-1. X_eff_res ∪ X_eff0를 구하고,
				final Pair<EffectSetVariable, EffectSetVarSource> x_eff_try = EquGenStmtExt.unionize(exnEffects);
				
				//   3-2. catch되는 타입들 C1, ... , Ck을 가져와 그들의 합집합인 X_C를 만들어,
				final ArrayList<EffectSet<ExnEffectElem>> cks = new ArrayList<>();
				for (Catch catchBlock : catchBlocks) {
					final EffectSet<ExnEffectElem> ci = EquGenAbstractCatchExt.formalTypes(catchBlock);
					cks.add(ci);
				}
				EffectSetVarSource src_x_c = EffectSetVarSource.SubStatement;
				if (cks.size() > 1) {	// 새로운 합집합이 생성될 예정이면
					src_x_c = EffectSetVarSource.New;
					ReportUtil.report(cks, EffectSetVarSource.SubStatement, EffectSetVarGoal.Flow);
				}
				final EffectSetVariable x_c = EffectUnion.unionize(cks);
				
				//   3-3. X_eff_res ∪ X_eff0 ∖ X_C를 구한다.
				final EffectSetVariable x_eff_try_differed = EffectDifference.differ(x_eff_try.part1(), x_c);
				EffectSetVarSource src_x_eff_try_differed = x_eff_try.part2();
				if (x_eff_try_differed != x_eff_try.part1()) {
					src_x_eff_try_differed = EffectSetVarSource.New;
					ReportUtil.report(x_eff_try.part1(), x_eff_try.part2(), EffectSetVarGoal.Flow);
					ReportUtil.report(x_c, src_x_c, EffectSetVarGoal.Flow);
				}
				if (x_eff_try_differed != null) {
					exnEffects.clear();
					exnEffects.put(x_eff_try_differed, src_x_eff_try_differed);
				}
			}
			
		//   4. stmt1, ... , stmtk를 분석하면 나오는 effects(exn, activity)들인 X_eff1, ... , X_effk를 각각 가져오고, 
			EquGenStmtExt.effects(catchBlocks, x_effs, EffectSetVarSource.SubStatement);
		}
		
		//   5. stmtn을 분석하면 나오는 effects(exn, activity)인 X_effn를 가져온 다음,
		Block finallyBlock = tryStmt.finallyBlock();
		if (finallyBlock != null) {
			EquGenStmtExt.effects(finallyBlock, x_effs, EffectSetVarSource.SubStatement);
		}
		
		//   6. 최종적으로 X_eff_res ∪ X_eff0 ∖ X_C ∪ X_eff1 ∪ ... ∪ X_effk ∪ X_effn을 구하고, 이를 리턴할 effects(exn, activity)로 지정.
		setEffects(x_effs);
		
		setLocalEnv(v.peekTypeEnv().getCurrEnv());
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
	
	
	/**
	 * For EquGenTryWithResourcesExt
	 * @return null
	 */
	protected List<LocalDecl> resources() {
		// For EquGenTryWithResourcesExt
		return null;
	}
}