package tool.compiler.java.effect;

import java.util.List;

import polyglot.ast.Call;
import polyglot.ast.Expr;
import polyglot.types.MethodInstance;
import polyglot.types.ReferenceType;
import polyglot.types.Type;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.ast.expr.EquGenExprExt;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;

public class ActivityEffectVariable extends EffectVariable {
	
	private MethodName mthName;
	
	public static final String CONTAINER = "android.app.Activity";
	public static enum MethodName { 
		startActivity, startActivities, startActivityForResult, 
		startActivityFromChild, startActivityFromFragment, 
		startActivityIfNeeded, startNextMatchingActivity;
		
		private final int getIntentParamIndex() {
			switch (this) {
//			case startActivity:
//			case startActivities:
//			case startActivityForResult:
//			case startActivityIfNeeded:
//			case startNextMatchingActivity:
//				return 0;
			case startActivityFromChild:
			case startActivityFromFragment:
				return 1;
			default:
				return 0;
			}
		}
	};
	
	/**
	 * 
	 */
	public ActivityEffectVariable() {
		super(null);
	}
	
	/**
	 * @param chi_effect
	 */
	protected ActivityEffectVariable(MetaSetVariable chi_intent) {
		super(EffectName.ActivityEff, chi_intent);
	}
	
	/**
	 * android.content.Activity를 상속받은 클래스의 메서드 중, startActivity()류의 메서드에 대하여 ActivityEffectVariable을 생성한다.
	 * @param callNode Call Node
	 * @return 생성된 ActivityEffectVariable, 생성 실패할 경우 null
	 */
	public static ActivityEffectVariable create(Call callNode) {
		MethodName mthName = getMethodName(callNode);	// Check Creatable
		if (mthName != null) {
			MetaSetVariable intent = getIntentMetaSetVar(mthName, callNode.arguments());
			ReportUtil.report(intent, MetaSetVarSource.Argument, MetaSetVarGoal.Effect);
			ActivityEffectVariable activityEff = new ActivityEffectVariable(intent);
			activityEff.setMthName(mthName);
			return activityEff;
		}
		return null;
	}
	

	/**
	 * Intent Param의 MetaSetVariable을 반환
	 * @param callNode
	 * @return
	 */
	private static MetaSetVariable getIntentMetaSetVar(MethodName mthName, List<Expr> args) {
		Expr intentExpr = args.get(mthName.getIntentParamIndex());	// NullPointerException
		return EquGenExprExt.metaSetVar(intentExpr);
	}
	
	/**
	 * android.app.Activity로부터 상속받은 Starting Activity 메서드인지 확인하고, 
	 * 그 메서드의 이름을 반환
	 * @param callNode
	 * @return
	 */
	private static MethodName getMethodName(Call callNode) {
		try {
			MethodName mthName = MethodName.valueOf(callNode.name());	// IllegalArgumentException
			if (checkContainer(callNode.methodInstance())) {
				return mthName;
			} 
		} catch (IllegalArgumentException e) {
			return null;
		}
		return null;
	}
	
	/**
	 * 부모 또는 조상 클래스에 android.app.Activity가 있는지 확인
	 * @param mthIns
	 * @return
	 */
	private static boolean checkContainer(MethodInstance mthIns) {
		if (!mthIns.flags().isStatic()) {
			Type superType = mthIns.container();
			ReferenceType type;
			do {
				type = (ReferenceType) superType;
				if (type.toString().equals(CONTAINER)) {
					return true;
				}
				superType = type.superType();
			} while (superType != null && superType instanceof ReferenceType);
			// TODO: android.app.Activity를 상속받지 않고 android.content.Context로부터 상속 받은 클래스의,
			// startActivity()와 startActivities()에 대해 대응할 필요가 있음?
		} 
		return false;
	}
	
	/**
	 * Activity Effect를 생성할 수 있는 Call Node인지 확인
	 * @param callNode
	 * @return
	 */
	public static boolean checkCreatable(Call callNode) {
		return getMethodName(callNode) != null;
	}
	
	/**
	 * @return the mthName
	 */
	public MethodName getMthName() {
		return mthName;
	}
	
	/**
	 * @param mthName the mthName to set
	 */
	private void setMthName(MethodName mthName) {
		this.mthName = mthName;
	}
}