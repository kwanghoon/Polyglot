package tool.compiler.java.constraint;

import java.util.List;

import tool.compiler.java.aos.AbsObjSet;
import tool.compiler.java.aos.TypedSetVariable;

/**
 * base constraint interface
 */
public interface Constraint {
	public Constraint substitute(List<TypedSetVariable> tsvs);
	public int absObjSetSize();
	public int substitutableSize();
	public List<? extends AbsObjSet> getAllAbsObjSets();
	public boolean contains(AbsObjSet aos);
	public String getKind();
}