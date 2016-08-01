package tool.compiler.java.table;

import tool.compiler.java.aos.AbstractObject;
import tool.compiler.java.info.FieldInfo;
import tool.compiler.java.util.CollUtil;

public class FieldTableRow extends AbstractTableRow implements FieldOps {
	
	public FieldTableRow(AbstractObject abstractObjectInfo, FieldInfo fieldInfo) {
		super(abstractObjectInfo, fieldInfo);
	}
	
	public FieldTableRow(FieldInfo fieldInfo) {
		super(fieldInfo);
	}
	
	/**
	 * @see tool.compiler.java.table.AbstractTableRow#getInfo()
	 */
	@Override
	public FieldInfo getInfo() {
		return (FieldInfo) super.getInfo();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String containerStr = getContainerType().toString();
		if(getTypeVariables().isEmpty()) {
				containerStr += "<>";
		}
		
		return "F("
				+ CollUtil.getStringOf(getBoundVariables(), '{', '}') + ", "
				+ containerStr + ", "
				+ getAbstractObjectInfo() + (!isStatic() ? CollUtil.getStringOf(getContainerSubstitutionTypes(), '<', '>') : "") + ", "
				+ getName() + ") = "
				+ getSetVariable();
	}
}