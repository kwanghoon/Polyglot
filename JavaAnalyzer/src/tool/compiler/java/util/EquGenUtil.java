package tool.compiler.java.util;

import java.util.List;
import java.util.Map;

import polyglot.ext.jl5.types.JL5ProcedureInstance;
import polyglot.ext.jl5.types.JL5Subst;
import polyglot.ext.jl5.types.JL5TypeSystem;
import polyglot.ext.jl5.types.TypeVariable;
import polyglot.ext.jl5.types.inference.InferenceSolver;
import polyglot.ext.jl5.types.inference.InferenceSolver_c;
import polyglot.types.ReferenceType;
import polyglot.types.Type;

public class EquGenUtil {

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
		if (m == null)
			return null;
		JL5Subst subst = (JL5Subst) ts.subst(m);
		return subst;
	}

	public static final InferenceSolver inferenceSolver(JL5ProcedureInstance pi, List<? extends Type> argTypes,
			JL5TypeSystem ts) {
		return new InferenceSolver_c(pi, argTypes, ts);
	}
}