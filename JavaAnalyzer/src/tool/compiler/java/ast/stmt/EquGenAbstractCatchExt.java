package tool.compiler.java.ast.stmt;

import java.util.HashMap;
import java.util.Map.Entry;

import polyglot.ast.Catch;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5LocalInstance;
import polyglot.types.Type;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.ast.EquGenExt;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSet;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.effect.ExnEffectElem;
import tool.compiler.java.env.LocalEnvironment;
import tool.compiler.java.env.TypeEnvironment;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarGoal;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * Catch <: CompoundStmt <: Stmt <: Term <: Node
 * @see tool.compiler.java.ast.stmt.EquGenCatchExt
 * @see tool.compiler.java.ast.stmt.EquGenMultiCatchExt
 * @author LHJ
 */
public abstract class EquGenAbstractCatchExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Catch";
	
	private EffectSet<ExnEffectElem> effect_formal = null;
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Catch catchStmt = (Catch) this.node();
		
		// 로컬 환경 구성 (Formal을 Local Env.에 등록하는 과정 포함)
		TypeEnvironment typeEnv = v.peekTypeEnv();
		typeEnv.push();
		Type catchType = catchStmt.catchType();
		MetaSetVariable chiFormal = MetaSetVariable.create(catchType);
		typeEnv.add((JL5LocalInstance) catchStmt.formal().localInstance(), chiFormal);
		ReportUtil.report(chiFormal, MetaSetVarSource.New, MetaSetVarGoal.LocalEnvironment);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Catch catchStmt = (Catch) this.node();
		
		// catch (C e) { stmt }
		//   1. stmt를 분석하면 나오는 effects(exn, activity)를 가져와
		final HashMap<EffectName, EffectSetVariable> effects = EquGenStmtExt.effects(catchStmt.body());
		
		//   2. 리턴할 effects(exn, activity)로 지정
		if (effects != null) {
			for (Entry<EffectName, EffectSetVariable> entry : effects.entrySet()) {
				EffectSetVariable effect = entry.getValue();
				addEffect(entry.getKey(), effect);
				ReportUtil.report(effect, EffectSetVarSource.SubStatement, EffectSetVarGoal.Return);
			} 
		}
		
		// 로컬 환경 해제
		LocalEnvironment localEnv = v.peekTypeEnv().pop();
		setLocalEnv(localEnv);
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
	
	
	/**
	 * @return the formalTypes
	 */
	public final EffectSet<ExnEffectElem> formalTypes() {
		return effect_formal;
	}
	
	/**
	 * @param n node
	 * @return the formalTypes of node n
	 */
	public static final EffectSet<ExnEffectElem> formalTypes(Catch n) {
		return ((EquGenAbstractCatchExt) EquGenExt.ext(n)).formalTypes();
	}
	
	/**
	 * @param formalTypes the formalTypes to set
	 */
	protected final void setFormalTypes(EffectSet<ExnEffectElem> formalTypes) {
		this.effect_formal = formalTypes;
	}
}