package tool.compiler.java.ast;

import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5LocalInstance;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.TypedSetVariable;
import tool.compiler.java.visit.XSubseteqY;

/**
 * LocalDecl <: ForInit <: Stmt <: Term <: Node	<br>
 * LocalDecl <: VarDecl <: Term <: Node
 * @author LHJ
 */
public class EquGenLocalDeclExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		LocalDecl lclDecl = (LocalDecl) this.node();
		Report.report(0, "Local Declaration: " + lclDecl/*.name()*/);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		LocalDecl lclDecl = (LocalDecl) this.node();
		// C x = e;
		//   1. x : C{X1}을 현재 env에 추가 (X1은 새로운 변수)
		TypedSetVariable cx1 = new TypedSetVariable(lclDecl.type().type());
		getLocalEnv().add((JL5LocalInstance) lclDecl.localInstance(), cx1);
		
		//   2. e가 있는지 확인 후 e의 타입 D{X2}를 가져오고 D{X2} <: C{X1}을 제약식 집합에 추가 
		//      e가 없으면 e의 타입은 C{X2} (X2는 새로운 변수)를 만들고 C{X2}<: C{X1}을 제약식 집합에 추가
		TypedSetVariable cdx2;
		if(lclDecl.init() != null) {
			cdx2 = EquGenExt.typedSetVar(lclDecl.init());
		} else {
			cdx2 = new TypedSetVariable(lclDecl.type().type());
		}
		
		XSubseteqY xy = new XSubseteqY(cdx2, cx1);
		v.addToSet(xy);
		
		//   3. 리턴할 typedSetVar는 없음
		
		return super.equGenLeave(v);
	}
}