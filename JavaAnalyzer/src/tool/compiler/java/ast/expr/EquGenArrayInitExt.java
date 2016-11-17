package tool.compiler.java.ast.expr;

import polyglot.ast.ArrayInit;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.AbstractObject;
import tool.compiler.java.aos.ArrayMetaSetVariable;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.aos.AbstractObject.Info;
import tool.compiler.java.constraint.ObjsSubseteqX;
import tool.compiler.java.constraint.XSubseteqY;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.util.EquGenUtil;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.EffectSetVarSource;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;
import tool.compiler.java.visit.EquGenerator;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ArrayInit <: Expr <: Term <: Node				<br>
 * ArrayInit <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenArrayInitExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Array Initialization";
	
	private AbstractObject absObj;
	private AbstractObject length;
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
		ArrayInit arrInit = (ArrayInit) this.node();
		
		absObj = new AbstractObject(arrInit);
		v.addToSet(absObj);
		ReportUtil.report(absObj);
		
		if (!arrInit.type().isNull()) {
			length = new AbstractObject(arrInit, Info.ArrayInitLength);
			v.addToSet(length);
			ReportUtil.report(length);
		}
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		ArrayInit arrInit = (ArrayInit) this.node();
		
		// {e1, ... , en}
		//   1. C[]{Chi} 변수 생성
		MetaSetVariable cchi = MetaSetVariable.create(arrInit.type());
		ReportUtil.report(cchi, MetaSetVarSource.New, MetaSetVarGoal.Return);
		
		//   2-1. C[]{o} <: C[]{Chi} 제약식을 추가
		ObjsSubseteqX ox = new ObjsSubseteqX(absObj, cchi);
		v.getCurrCF().addMetaConstraint(ox);
		ReportUtil.report(ox);
		
		if (!arrInit.type().isNull()) {
			//   2-2. C{Chi}.length에 대한 제약식 생성
			ArrayMetaSetVariable cchi_arr = (ArrayMetaSetVariable) cchi;
			MetaSetVariable cchi_length = cchi_arr.length();
			ReportUtil.report(cchi_length, MetaSetVarSource.ArrayLength, MetaSetVarGoal.ArraySubFlow);
			ox = new ObjsSubseteqX(length, cchi_length);
			v.getCurrCF().addMetaConstraint(ox);
			ReportUtil.report(ox);
			
			//   2-3. elements에 대한 데이터 플로우
				//   2-3a. C[]{Chi}의 base의 타입 C[]{Chi}.base를 가져오고
			MetaSetVariable cchi_base = cchi_arr.base();
			ReportUtil.report(cchi_base, MetaSetVarSource.ArrayBase, MetaSetVarGoal.ArraySubFlow);
			
			for(Expr ei : arrInit.elements()) {
				//   2-3b. ei의 타입 Ci{Chii}를 가져온 다음 (element의 타입에 대한 MSV)
				MetaSetVariable cichii = metaSetVar(ei);
				ReportUtil.report(cichii, MetaSetVarSource.ArrayElement, MetaSetVarGoal.ArraySubFlow);
				
				//   2-3c. Ci{Chii} <: C[]{Chi}.base 제약식을 추가 (element에 대한 Top Level)
				XSubseteqY xy = new XSubseteqY(cichii, cchi_base);
				v.getCurrCF().addMetaConstraint(xy);
				ReportUtil.report(xy);
				
				//   2-3d. ei가 배열인 경우, Ci{Chii} <: C[]{Chi}.base의 하위 레벨 제약식을 집합에 추가
				//         (Top Level 아래의 MetaSetVariable(s)의 데이터 플로우)
				if(EquGenUtil.isArray(ei.type())) {
					Collection<XSubseteqY> xys = XSubseteqY.constrain(
							cichii, (ArrayMetaSetVariable) cchi_base);
					v.getCurrCF().addMetaConstraints(xys);
				}
			}
		}
		
		//   3. return C[]{Chi}
		setMetaSetVar(cchi);
		
		//   4. e1, ... , en를 분석해서 나오는 effects(exn, activity)인 X_eff1, ... , X_effn를 찾아 
		//      X_eff1 ∪ ... ∪ X_effn를 구하고,
		final LinkedHashMap<EffectName, Map<EffectSetVariable, EffectSetVarSource>> x_effs = new LinkedHashMap<>();
		EquGenExprExt.effects(arrInit.elements(), x_effs, EffectSetVarSource.SubExpression);
		
		//   5. 이를 리턴할 effects(exn, activity)로 지정
		setEffects(x_effs);
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}