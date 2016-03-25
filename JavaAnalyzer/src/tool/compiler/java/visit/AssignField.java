package tool.compiler.java.visit;

import polyglot.ext.jl5.types.JL5FieldInstance;
import polyglot.types.Type;

/**
 * C{X} <: D{Y}.f<br>
 * ... assign to an instance field
 */
public class AssignField extends Constraint {
	
	// fields
	
	/* ### Abstract Fields ###
	 * Type c;				// C
	 * SetVariable x;		// X
	 * Type d;				// D
	 * SetVariable y;		// Y
	 * FieldInstance f;		// f
	 */
	
	/* ### Actual Fields ### */
	private TypedSetVariable cx;	// C, X
	private TypedSetVariable dy;	// D, Y
	private JL5FieldInstance f;		// f
	
	
	// constructor
	
	/**
	 * C{X} <: D{Y}
	 * @param cx	set C, X	( C{X} )
	 * @param dy	set D, Y	( D{Y} )
	 * @param f		set f
	 */
	public AssignField(TypedSetVariable cx, TypedSetVariable dy, JL5FieldInstance f) {
		super();
		this.cx = cx;
		this.dy = dy;
		this.f = f;
	}
	
	
	// getter methods
	
	/**
	 * @return the C{X}
	 */
	public TypedSetVariable getCX() {
		return cx;
	}
	
	/**
	 * @return the C
	 */
	public Type getC() {
		return cx.getType();
	}
	
	/**
	 * @return the X
	 */
	public String getX() {
		return cx.getID();
	}
	
	/**
	 * @return the D{Y}
	 */
	public TypedSetVariable getDY() {
		return dy;
	}
	
	/**
	 * @return the D
	 */
	public Type getD() {
		return dy.getType();
	}
	
	/**
	 * @return the Y
	 */
	public String getY() {
		return dy.getID();
	}
	
	/**
	 * @return the f
	 */
	public JL5FieldInstance getF() {
		return f;
	}
	
	
	/**
	 * Form:	C{X} <: D{Y}
	 */
	@Override
	public String toString() {
		return getCX() + " <: " + getDY() + "." + getF().name();
	}
}