package tool.compiler.java.visit;

import java.util.ArrayList;
import java.util.Collection;

/**
 * base constraint interface
 */
public interface Constraint {
	public Constraint substitute(Collection<TypedSetVariable> tsvs);
	public int absObjSetSize();
	public int substitutableSize();
	public ArrayList<? extends AbsObjSet> getAllAbsObjSets();
	public boolean contains(AbsObjSet aos);
}