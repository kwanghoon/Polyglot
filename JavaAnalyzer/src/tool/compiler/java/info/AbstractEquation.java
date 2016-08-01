package tool.compiler.java.info;

import java.util.List;

import polyglot.ext.jl5.types.TypeVariable;
import polyglot.types.ReferenceType;
import polyglot.types.Type;
import tool.compiler.java.table.TypingOps;

@Deprecated
public abstract class AbstractEquation implements TypingOps {
	
	private AbstractTypingInfo info;
	
	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return info.getName();
	}
	
	@Override
	public List<TypeVariable> getBoundVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TypeVariable> getTypeVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReferenceType getContainerType() {
		return info.getContainerType();
	}

	@Override
	public Type getType() {
		return info.getType();
	}

	@Override
	public ReferenceType getContainerBaseType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @return the info
	 */
	public AbstractTypingInfo getInfo() {
		return info;
	}

	/**
	 * @param info the info to set
	 */
	protected final void setInfo(AbstractTypingInfo info) {
		this.info = info;
	}
}