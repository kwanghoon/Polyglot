package tool.compiler.java.aos;

import polyglot.types.Type;

public abstract class DataFlowSetVariable extends AbsObjSet {
	
	protected static final long NOID = -1;
	private static boolean IdGen;
	
	/**
	 * @param type
	 */
	protected DataFlowSetVariable(Type type) {
		super(check(type));
	}
	
	private static final Type check(Type type) {
		// Type이 void이거나 null type인 경우 ID를 생성하지 않음
		if(!type.isVoid() && !type.isNull()) {
			IdGen = true;
		} else {
			IdGen = false;
		}
		return type;
	}
	
	/**
	 * @return the ID
	 */
	@Override
	public final String getID() {
		if(idNum() != NOID) {
			return super.getID();
		} else {
			return null;
		}
	}
	
	/**
	 * generate ID Number
	 */
	@Override
	protected final long generateIDNum() {
		// Type이 void이거나 null type인 경우 ID를 생성하지 않음
		if(IdGen) {
			return idFoctor();
		} else {
			return NOID;
		}
	}
	
	/**
	 * ID 번호는 하위 클래스에서 static 변수로 관리하며, 이 메서드를 Overriding하여 반환 받음.
	 * @return 생성된 ID 번호
	 */
	protected abstract long idFoctor();
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		try {
			if(getType().isNull()) {
				return "null";
			}
			
			String result = getType() + "{";
			if(!getType().isVoid()) {
				result += super.toString();
			} 
			return result + "}";
		} catch (NullPointerException e) {
			return "The type field of TypedSetVariable is null.";
		}
	}
}