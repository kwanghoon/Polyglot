package tool.compiler.java.visit;

import polyglot.ext.jl5.types.JL5SubstClassType;
import polyglot.types.Type;

public abstract class AbsObjSet implements Info {
	
	private Type type;
	private long idNum;
	
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
	public void setType(Type type) {
		this.type = type;
	}
	
	/**
	 * @return the ID number
	 */
	protected final long idNum() {
		return idNum;
	}
	
	/**
	 * @return the kind of program point
	 */
	public abstract String kind();
	
	/**
	 * @return the ID number to set
	 */
	protected abstract long generateIDNum();
	
	/**
	 * generate the ID
	 */
	protected final void generateID() {
		idNum = generateIDNum();
	}
	
	/**
	 * @return the ID
	 */
	public String getID() {
		return kind() + idNum();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getID();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kind() == null) ? 0 : kind().hashCode());
		result = prime * result + (int) (idNum() ^ (idNum() >>> 32));
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
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AbsObjSet other = (AbsObjSet) obj;
		if (kind() == null) {
			if (other.kind() != null) {
				return false;
			}
		} else if (!kind().equals(other.kind())) {
			return false;
		}
		if (idNum() != other.idNum()) {
			return false;
		}
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