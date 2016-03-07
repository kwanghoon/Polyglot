package tool.compiler.java.visit;

import polyglot.ext.jl5.types.JL5FieldInstance;
import polyglot.types.MemberInstance;
import polyglot.types.Type;
import tool.compiler.java.util.CollUtil;

public class FieldInfo extends TypingInfo implements FieldOps {
	
	private JL5FieldInstance fldIns;
	
	/**
	 * @param fieldInstance
	 */
	public FieldInfo(JL5FieldInstance fieldInstance) {
		super(fieldInstance);
	}
	
	@Override
	public String getName() {
		return fldIns.name();
	}
	
	@Override
	public Type getType() {
		return fldIns.type();
	}
	
	/**
	 * @return the FieldInstance
	 */
	@Override
	public JL5FieldInstance getTypeInstance() {
		return fldIns;
	}
	
	/**
	 * @param FieldInstance the FieldInstance to set
	 */
	@Override
	protected void setTypeInstance(MemberInstance fieldInstance) {
		fldIns = (JL5FieldInstance) fieldInstance;		// ClassCastingException이 발생하는 경우가 없음.
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
		
		return "FI("
				+ CollUtil.getStringOf(getBoundVariables(), '{', '}') + ", "
				+ containerStr + ", "
				+ getName() + ") = "
				+ getType();
	}
}