package tool.compiler.java.visit;

import java.util.List;

import polyglot.ext.jl5.types.TypeVariable;
import polyglot.types.ReferenceType;
import polyglot.types.Type;

public interface Ops {
	/**
	 * get Delta
	 * @return
	 */
	public List<TypeVariable> getBoundVariables();
	public List<TypeVariable> getTypeVariables();
	
	/**
	 * get Class
	 * @return
	 */
	public ReferenceType getContainerType();
	
	/**
	 * get Name
	 * @return
	 */
	public String getName();
	
	/**
	 * get Type
	 * @return
	 */
	public Type getType();
	
	/**
	 * get Base Type of Class if the class is generic.
	 * @return
	 */
	public ReferenceType getContainerBaseType();
	
	/**
	 * static declaration
	 * @return
	 */
	public boolean isStatic();
}