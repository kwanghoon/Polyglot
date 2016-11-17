package tool.compiler.java.aos;

public abstract class SetVariable {
	
	private long idNum;
	
	protected SetVariable() {
		generateID();
	}
	
	/**
	 * @return the ID number
	 */
	protected final long idNum() {
		return idNum;
	}
	
	/**
	 * @return the kind() of program point
	 */
	public abstract String kind();
	
	/**
	 * @return the ID number to set
	 */
	protected abstract long generateIDNum();
	
	/**
	 * generate the ID
	 */
	private final void generateID() {
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
		result = prime * result + (int) (idNum ^ (idNum >>> 32));
		return result;
	}
	
	/**
	 * 변수의 종류와 ID가 일치하는지 비교하여 같은지 판단한다.
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
		SetVariable other = (SetVariable) obj;
		if (kind() == null) {
			if (other.kind() != null) {
				return false;
			}
		} else if (!kind().equals(other.kind())) {
			return false;
		}
		if (idNum != other.idNum) {
			return false;
		}
		return true;
	}
}