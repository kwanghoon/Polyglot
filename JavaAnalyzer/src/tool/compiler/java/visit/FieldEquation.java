package tool.compiler.java.visit;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import tool.compiler.java.util.CollUtil;

// TODO: 나중에 refactoring 필요
@Deprecated
public class FieldEquation extends AbstractEquation {
	
	private LinkedHashSet<FieldTable> fieldTableSet;
	private LinkedList<AbstractObjectInfo> absObjInfoList;
	private LinkedList<TypedSetVariable> setVarList;
	
	private static final char envId = 'x';
	
	public FieldEquation(FieldInfo fieldInfo, Collection<FieldTable> fieldTables) {
		this(fieldInfo, fieldTables, true);
	}
	
	public FieldEquation(FieldInfo fieldInfo, Collection<FieldTable> fieldTables, boolean argCheck) {
		super();
		if(argCheck) {
			checkArguments(fieldInfo, fieldTables);
		}
		setInfo(fieldInfo);
		this.fieldTableSet = new LinkedHashSet<>(fieldTables);
		System.out.println(fieldTables);
	}
	
	public FieldEquation(Collection<FieldTable> fieldTables) {
		this(fieldTables, true);
	}
	
	public FieldEquation(Collection<FieldTable> fieldTables, boolean argCheck) {
		this(!fieldTables.isEmpty() ? fieldTables.iterator().next().getInfo() : null, fieldTables, argCheck);
	}
	
	/**
	 * @see tool.compiler.java.visit.AbstractEquation#getInfo()
	 */
	@Override
	public FieldInfo getInfo() {
		return (FieldInfo) super.getInfo();
	}
	
	/**
	 * @return the absObjInfoList
	 */
	public final List<AbstractObjectInfo> getAbsObjInfoList() {
		return absObjInfoList;
	}
	
	/**
	 * @return the setVarList
	 */
	public final List<TypedSetVariable> getSetVarList() {
		return setVarList;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// 형식: Γ{x:C{R}} ▷ x.f:S, ∅
		return "Γ{" + envId + ":" + getContainerType() + CollUtil.getStringOf(absObjInfoList, '{', '}') +"} ▷ " + envId + "." + getName() + ":";
	}
	
	public static final void checkArguments(FieldInfo fieldInfo, Collection<FieldTable> fieldTables) {
		if(!fieldTables.isEmpty()) {					// fieldTables가 null이면 NullPointerException 발생
			for(FieldTable ft: fieldTables) {
				if(!fieldInfo.equals(ft.getInfo())) {	// fieldInfo가 null이면 NullPointerException 발생
					throw new IllegalArgumentException("Missmatch among Info objects.");
				}
			}
		} else {	// fieldTables가 비어있는 경우
			throw new IllegalArgumentException("'fieldTables' is empty.");
		}
	}
	
	public static final void checkArguments(Collection<FieldTable> fieldTables) {
		checkArguments(!fieldTables.isEmpty() ? fieldTables.iterator().next().getInfo() : null, fieldTables);
				// fieldTables가 null이면 NullPointerException 발생
	}
}