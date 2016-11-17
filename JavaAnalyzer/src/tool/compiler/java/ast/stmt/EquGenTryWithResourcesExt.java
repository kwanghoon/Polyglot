package tool.compiler.java.ast.stmt;

import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ext.jl7.ast.TryWithResources;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;

import java.util.List;

/**
 * TryWithResources <: Try <: CompoundStmt <: Stmt <: Term <: Node
 * @author LHJ
 */
public class EquGenTryWithResourcesExt extends EquGenTryExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Try with Resources";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		EquGenerator superEnter = super.equGenEnter(v);
//		TryWithResources tryRes = (TryWithResources) this.node();
		
		// 로컬 환경 구성 (Resources를 위해)
		v.peekTypeEnv().push();
		
		return superEnter;
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		TryWithResources tryRes = (TryWithResources) super.equGenLeave(v);
		
		// 로컬 환경 해제
		// TODO: Resources는 try block에서만 접근 가능하므로, 아래와 같이
		// Try Node의 탈출부에서 로컬 환경을 해제하는 것은 좋지 못한 것으로 판단된다. 
		// 따라서, try block의 탈출부에서 환경을 해제하는 방법을 강구해야한다.
		// 다만, 이미 컴파일러에서 catch/finally block가 Resources에 접근하는 것을 막으므로,
		// Resources에 대한 MetaSetVariable에 접근하는 경우가 없음은 보장된다.
		v.peekTypeEnv().pop();	// super에서 설정(setLocalEnv())했던 LocalEnv임
		
		return tryRes;
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
	
	
	/**
	 * @return Resources
	 */
	@Override
	protected final List<LocalDecl> resources() {
		TryWithResources tryRes = (TryWithResources) this.node();
		return tryRes.resources();
	}
}