package tool.compiler.java.env;

import polyglot.types.InitializerInstance;
import tool.compiler.java.aos.AbsObjSet;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.aos.TypedSetVariable;
import tool.compiler.java.constraint.Constraint;

import java.util.ArrayList;
import java.util.HashMap;

public class InitializerConstraint extends CodeConstraint {
	
	/**
	 * @param initializer
	 */
	public InitializerConstraint(InitializerInstance initializer) {
		super(initializer);
	}
	
	/**
	 * @return the initializer
	 */
	@Override
	public InitializerInstance getInstance() {
		return (InitializerInstance) super.getInstance();
	}
	
	public ArrayList<Constraint> apply() {
		
		// Initializer를 수행할 때 생기는 자료흐름 CS를 만든다.
		ArrayList<Constraint> cs = new ArrayList<>();
		HashMap<MetaSetVariable, TypedSetVariable> substLocals = new HashMap<>();
		for(Constraint metaCon : getMetaConstraints()) {	// 가지고 있는 전체 제약식에 대해
			ArrayList<TypedSetVariable> substs = new ArrayList<>();	// subst한 aos
			// MetaSetVariable을 TypedSetVariable로 대치
			for(AbsObjSet aos : metaCon.getAllAbsObjSets()) {
				if (aos instanceof MetaSetVariable) {
					// Local의 Chi만 존재
					TypedSetVariable tsvLocal = substLocals.get(aos);
					if(tsvLocal != null) {
						substs.add(tsvLocal);
					} else {
						tsvLocal = new TypedSetVariable(aos.getType());
						substLocals.put((MetaSetVariable) aos, tsvLocal);
						substs.add(tsvLocal);
					}
				} else if (aos instanceof TypedSetVariable) {
					substs.add((TypedSetVariable) aos);
				}
			}
			cs.add(metaCon.substitute(substs));
		}
		
		return cs;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MC " + getInstance() + ": λ()";
	}
}