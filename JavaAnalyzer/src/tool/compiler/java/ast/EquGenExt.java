package tool.compiler.java.ast;

import java.util.Collection;
import java.util.HashMap;

import polyglot.ast.Ext;
import polyglot.ast.Ext_c;
import polyglot.ast.Node;
import polyglot.util.InternalCompilerError;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.effect.Effect;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.visit.EquGenerator;

public class EquGenExt extends Ext_c implements EquGenOps {	// TODO: Not JL7Ext, but Ext_c to override lang()!!!
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Node";
	
	private HashMap<EffectName, Effect> effects = null;
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public static EquGenExt ext(Node n) {
		Ext e = n.ext();
		while ( e != null && !(e instanceof EquGenExt) ) {
			e = e.ext();
		}
		if ( e == null ) {
			throw new InternalCompilerError("No EquGen extension object for node"
					+ n + " (" + n.getClass() + ")", n.position());
		}
		
		return (EquGenExt)e;
	}
		
	@Override	// TODO: Must extend Ext_c, not JL7Ext!!!
	public final EquGenLang lang() {
		return EquGenLang_c.instance;
	}
	
	// TODO: Override operation methods for overridden AST operations.
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		// By default, return the given visitor.
		return v;
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		// By default, do nothing.
		return node();
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
	
	
	/**
	 * @return the Effect
	 */
	public final Effect effect(EffectName type) {
		try {
			return effects.get(type);
		} catch(NullPointerException e) {
			return null;
		}
	}
	
	public final Collection<Effect> effects() {
		try {
			return effects.values();
		} catch(NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @param n node
	 * @param type Effect Name
	 * @return the Effect of node n
	 */
	public static final Effect effect(Node n, EffectName type) {
		return EquGenExt.ext(n).effect(type);
	}
	
	/**
	 * @param n node
	 * @return Effects of node n
	 */
	public static final Collection<Effect> effects(Node n) {
		return EquGenExt.ext(n).effects();
	}
	
	/**
	 * @param effect the Effect to add
	 */
	protected final void addEffect(Effect effect) {
		if(effects == null) {
			effects = new HashMap<>();
		}
		effects.put(effect.getType(), effect);
	}
}