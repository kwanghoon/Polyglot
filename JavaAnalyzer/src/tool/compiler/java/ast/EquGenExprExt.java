package tool.compiler.java.ast;

import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.visit.EquGenerator;

/**
 * Expr <: Term <: Node					<br>
 * Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenExprExt extends EquGenExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Expression";
	
	private MetaSetVariable metaSetVar = null;
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
//		ReportUtil.enterReport(this);
//		Expr expr = (Expr)this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
//		ReportUtil.leaveReport(this);
//		Expr expr = (Expr)this.node();
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
	
	
	/**
	 * @return the MetaSetVariable
	 */
	public final MetaSetVariable metaSetVar() {
		return metaSetVar;
	}
	
	/**
	 * @param n node
	 * @return the MetaSetVariable of node n
	 */
	public static final MetaSetVariable metaSetVar(Expr n) {
		return ((EquGenExprExt)EquGenExt.ext(n)).metaSetVar();
	}
	
	/**
	 * @param metaSetVar the MetaSetVariable to set
	 */
	protected final void setMetaSetVar(MetaSetVariable metaSetVar) {
		this.metaSetVar = metaSetVar;
	}
}