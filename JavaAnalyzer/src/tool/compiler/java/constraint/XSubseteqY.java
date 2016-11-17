package tool.compiler.java.constraint;

import java.util.ArrayList;
import java.util.List;

import tool.compiler.java.aos.AbsObjSet;
import tool.compiler.java.aos.ArrayMetaSetVariable;
import tool.compiler.java.aos.DataFlowSetVariable;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.aos.TypedSetVariable;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;

/**
 * X <: Y
 */
public class XSubseteqY implements Constraint {
	
	// fields
	
	private DataFlowSetVariable x, y;	// X, Y (NOT null)
	
	
	// constructor
	
	/**
	 * X <: Y
	 * @param x	set X
	 * @param y	set Y
	 */
	public XSubseteqY(DataFlowSetVariable x, DataFlowSetVariable y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	
	// Utilities
	
	/**
	 * X[] <: Y[]		<br>
	 * 주의! Top Level의 배열 타입에 대한 제약식은 생성하지 않으므로 별도로 생성해야 함!
	 *      Top Level의 length에 대한 제약식은 생성함!
	 * @param x	set X[]
	 * @param y	set Y[]
	 * @return	Generated Constraints for Array (XSubseteqY)
	 */
	public static final List<XSubseteqY> constrain(ArrayMetaSetVariable x, ArrayMetaSetVariable y) {
		ArrayList<XSubseteqY> constraints = new ArrayList<>();
		XSubseteqY xy;
		ArrayMetaSetVariable cichi;			// C{Chi1}의 하위 레벨 base Ci{Chi}
		ArrayMetaSetVariable dichi;			// D{Chi2}의 하위 레벨 base Di{Chi}
		MetaSetVariable cichi_length;
		MetaSetVariable dichi_length;
		MetaSetVariable cichi_base = y;
		MetaSetVariable dichi_base = x;
		
		do {
			// 현재 레벨 갱신
			cichi = (ArrayMetaSetVariable) cichi_base;	// 현재 Ci{Chi} 갱신
			dichi = (ArrayMetaSetVariable) dichi_base;	// 현재 Di{Chi} 갱신
			
			// 각각의 length를 가져와
			cichi_length = cichi.length();	// Ci{Chi}의 length
			ReportUtil.report(cichi_length, MetaSetVarSource.ArrayLength, MetaSetVarGoal.ArraySubFlow);
			dichi_length = dichi.length();	// Di{Chi}의 length
			ReportUtil.report(dichi_length, MetaSetVarSource.ArrayLength, MetaSetVarGoal.ArraySubFlow);
			
			// Di{Chi}.length <: Ci{Chi}.length 제약식 추가 (각각 int 타입)
			xy = new XSubseteqY(dichi_length, cichi_length);
			constraints.add(xy);
			ReportUtil.report(xy);
			
			// 각각의 base를 가져와
			cichi_base = cichi.base();	// Ci{Chi}의 base
			ReportUtil.report(cichi_base, MetaSetVarSource.ArrayBase, MetaSetVarGoal.ArraySubFlow);
			dichi_base = dichi.base();	// Di{Chi}의 base
			ReportUtil.report(dichi_base, MetaSetVarSource.ArrayBase, MetaSetVarGoal.ArraySubFlow);
			
			// Di{Chi}.base <: Ci{Chi}.base 제약식 추가
			xy = new XSubseteqY(dichi_base, cichi_base);
			constraints.add(xy);
			ReportUtil.report(xy);
		} while(cichi_base instanceof ArrayMetaSetVariable && 
				dichi_base instanceof ArrayMetaSetVariable);
		// TODO: cichi_base와 dichi_base 둘 중 하나만 
		// ArrayMetaSetVariable가 되는 경우가 발생하는가
		
		return constraints;
	}
	
	public static final List<XSubseteqY> constrain(MetaSetVariable x, ArrayMetaSetVariable y) {
		try {
			return constrain((ArrayMetaSetVariable) x, y);
		} catch (ClassCastException e) {
			return new ArrayList<>();
//			return null;
		}
	}
	
	
	// substitution methods
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * X <: Y
	 * @param x	set X
	 * @param y	set Y
	 * @return	Substituted New Constraint
	 */
	public XSubseteqY substitute(TypedSetVariable x, TypedSetVariable y) {
		if(!this.x.equalsForType(x)) {
			throw new IllegalArgumentException("The Type Mismatch for x. "
					+ "(orig: " + this.x.getType() + ", subst: " + x.getType() + ")");
		}
		
		if(!this.y.equalsForType(y)) {
			throw new IllegalArgumentException("The Type Mismatch for y. "
					+ "(orig: " + this.y.getType() + ", subst: " + y.getType() + ")");
		}
		
		return new XSubseteqY(x, y);
	}
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * X <: Y
	 * @param xy	X and Y	(The size is 2)
	 * @return		Substituted New Constraint
	 */
	@Override
	public Constraint substitute(List<TypedSetVariable> xy) {
		if(xy.size() != substitutableSize()) {
			throw new IllegalArgumentException("The Size of tsvs must be " + substitutableSize() + ". "
					+ "(Current size is " + xy.size() + ".)");
		}
		Object[] xyArr = xy.toArray();
		return substitute((TypedSetVariable)xyArr[0], (TypedSetVariable)xyArr[1]);
	}
	
	
	// getter methods
	
	/**
	 * @return the X
	 */
	public AbsObjSet getX() {
		return x;
	}
	
	/**
	 * @return the Y
	 */
	public AbsObjSet getY() {
		return y;
	}
	
	
	@Override
	public int absObjSetSize() {
		return 2;
	}
	
	@Override
	public int substitutableSize() {
		return 2;
	}
	
	@Override
	public List<? extends AbsObjSet> getAllAbsObjSets() {
		ArrayList<DataFlowSetVariable> abss = new ArrayList<>();
		abss.add(x);
		abss.add(y);
		return abss;
	}
	
	@Override
	public boolean contains(AbsObjSet aos) {
		if (x.equals(aos)) {
			return true;
		}
		if (y.equals(aos)) {
			return true;
		}
		return false;
	}
	
	@Override
	public String getKind() {
		return this.getClass().getSimpleName();
	}
	
	
	/**
	 * Form:	C{X} <: D{Y}
	 */
	@Override
	public String toString() {
		return getX() + " <: " + getY();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
		return result;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		XSubseteqY other = (XSubseteqY) obj;
		if (x == null) {
			if (other.x != null) {
				return false;
			}
		} else if (!x.equals(other.x)) {
			return false;
		}
		if (y == null) {
			if (other.y != null) {
				return false;
			}
		} else if (!y.equals(other.y)) {
			return false;
		}
		return true;
	}
}