package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ast.Stmt;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.env.LocalEnvironment;
import tool.compiler.java.visit.EquGenerator;

/**
 * Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenStmtExt extends EquGenExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Statement";
	
	private LocalEnvironment localEnv = null;
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
//		ReportUtil.enterReport(this);
//		Stmt stmt = (Stmt)this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
//		ReportUtil.leaveReport(this);
//		Stmt stmt = (Stmt)this.node();
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
	
	
	/**
	 * @return the LocalEnvironment
	 */
	public final LocalEnvironment localEnv() {
		return localEnv;
	}
	
	/**
	 * @param n node
	 * @return the LocalEnvironment of node n
	 */
	public static final LocalEnvironment localEnv(Stmt n) {
		return ((EquGenStmtExt)EquGenExt.ext(n)).localEnv();
	}
	
	/**
	 * @param localEnv the LocalEnvironment to set
	 */
	protected final void setLocalEnv(LocalEnvironment localEnv) {
		this.localEnv = localEnv;
	}
}