package tool.compiler.java.aos;

import polyglot.ext.jl5.types.JL5SubstClassType;
import polyglot.types.Type;
import tool.compiler.java.info.Info;

public abstract class AbsObjSet extends SetVariable implements Info {
	
	private Type type;
	
	protected AbsObjSet(Type type) {
		super();
		setType(type);
	}
	
	/**
	 * @return the type
	 */
	@Override
	public Type getType() {
		return type;
	}
	
	/**
	 * 
	 */
	@Override
	public Type getBaseType() {
		if(type instanceof JL5SubstClassType) {
			return ((JL5SubstClassType)type).base();
		}
		return getType();
	}
	
	/**
	 * @param type the type to set
	 */
	private void setType(Type type) {
		this.type = type;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	
	/**
	 * 변수의 종류와 ID 및 타입이 일치하는지 비교하여 같은지 판단한다.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AbsObjSet other = (AbsObjSet) obj;
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 두 AOS의 타입이 서로 같은지 확인한다.
	 */
	public boolean equalsForType(AbsObjSet aos) {
		return this.getType().equals(aos.getType());
	}
}