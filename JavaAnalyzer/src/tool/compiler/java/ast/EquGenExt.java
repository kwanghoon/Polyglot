package tool.compiler.java.ast;

import polyglot.ast.Ext;
import polyglot.ast.Ext_c;
import polyglot.ast.Node;
import polyglot.util.InternalCompilerError;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.MetaSetVariable;

public class EquGenExt extends Ext_c implements EquGenOps {	// TODO: Not JL7Ext, but Ext_c to override lang()!!!
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	private MetaSetVariable metaSetVar = null;
//	private Effect effect = null;
	
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
	
	
	
	/**
	 * @return the MetaSetVariable
	 */
	@Override
	public final MetaSetVariable MetaSetVar() {
		return metaSetVar;
	}
	
	/**
	 * @param n node
	 * @return the MetaSetVariable of node n
	 */
	public static final MetaSetVariable MetaSetVar(Node n) {
		return EquGenExt.ext(n).MetaSetVar();
	}
	
	/**
	 * @param metaSetVar the MetaSetVariable to set
	 */
	protected final void setMetaSetVar(MetaSetVariable metaSetVar) {
		this.metaSetVar = metaSetVar;
	}
}