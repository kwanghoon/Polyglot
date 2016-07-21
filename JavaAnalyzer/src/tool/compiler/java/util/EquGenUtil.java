package tool.compiler.java.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import polyglot.ext.jl5.types.JL5ArrayType;
import polyglot.ext.jl5.types.JL5ProcedureInstance;
import polyglot.ext.jl5.types.JL5Subst;
import polyglot.ext.jl5.types.JL5TypeSystem;
import polyglot.ext.jl5.types.TypeVariable;
import polyglot.ext.jl5.types.inference.InferenceSolver;
import polyglot.ext.jl5.types.inference.InferenceSolver_c;
import polyglot.main.Report;
import polyglot.types.ReferenceType;
import polyglot.types.Type;
import tool.compiler.java.visit.ArrayMetaSetVariable;
import tool.compiler.java.visit.MetaSetVariable;
import tool.compiler.java.visit.XSubseteqY;

public final class EquGenUtil {
	
	/**
	 * Infer type arguments for pi, when it is called with arguments of type argTypes
	 * @param pi
	 * @param argTypes
	 * @return
	 */
	public static final JL5Subst inferTypeArgs(JL5ProcedureInstance pi, List<? extends Type> argTypes,
			Type expectedReturnType, JL5TypeSystem ts) {
		InferenceSolver s = inferenceSolver(pi, argTypes, ts);
		Map<TypeVariable, ReferenceType> m = s.solve(expectedReturnType);
		if (m == null) {
			return null;
		}
		JL5Subst subst = (JL5Subst) ts.subst(m);
		return subst;
	}
	
	private static final InferenceSolver inferenceSolver(JL5ProcedureInstance pi, List<? extends Type> argTypes,
			JL5TypeSystem ts) {
		return new InferenceSolver_c(pi, argTypes, ts);
	}
	
	/**
	 * X[] <: Y[]		<br>
	 * 주의! Top Level의 배열 타입에 대한 제약식은 생성하지 않음!
	 *      Top Level의 length에 대한 제약식은 생성함!
	 * @param x	set X[]
	 * @param y	set Y[]
	 * @return	Generated Constraints for Array (XSubseteqY)
	 */
	public static final ArrayList<XSubseteqY> constrain(ArrayMetaSetVariable x, ArrayMetaSetVariable y) {
		ArrayList<XSubseteqY> constraints = new ArrayList<>();
		XSubseteqY xy;
		ArrayMetaSetVariable cichi;			// C{Chi1}의 하위 레벨 base Ci{Chi}
		ArrayMetaSetVariable dichi;			// D{Chi2}의 하위 레벨 base Di{Chi}
		MetaSetVariable cichi_base = y;
		MetaSetVariable dichi_base = x;
		
		do {
			// 현재 레벨 갱신
			cichi = (ArrayMetaSetVariable) cichi_base;	// 현재 Ci{Chi} 갱신
			dichi = (ArrayMetaSetVariable) dichi_base;	// 현재 Di{Chi} 갱신
			
			// Di{Chi}.length <: Ci{Chi}.length 제약식 추가 (각각 int 타입)
			xy = new XSubseteqY(dichi.length(), cichi.length());
			constraints.add(xy);
			Report.report(3, "\t[XSubseteqY] " + xy);
			
			// 각각의 base를 가져옴
			cichi_base = cichi.base();	// Ci{Chi}의 base
			dichi_base = dichi.base();	// Di{Chi}의 base
			
			// Di{Chi}.base <: Ci{Chi}.base 제약식 추가
			xy = new XSubseteqY(dichi_base, cichi_base);
			constraints.add(xy);
			Report.report(3, "\t[XSubseteqY] " + xy);
		} while(cichi_base instanceof ArrayMetaSetVariable && 
				dichi_base instanceof ArrayMetaSetVariable);
		// TODO: cichi_base와 dichi_base 둘 중 하나만 
		// ArrayMetaSetVariable가 되는 경우가 발생하는가
		
		return constraints;
	}
	
	public static final boolean isArray(Type type) {
		return type instanceof JL5ArrayType;
	}
	
	public static final boolean isArray(MetaSetVariable metaSetVar) {
		return metaSetVar instanceof ArrayMetaSetVariable;
	}
}