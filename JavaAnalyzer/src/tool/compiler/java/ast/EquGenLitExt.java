package tool.compiler.java.ast;

import polyglot.ast.Lit;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.AbstractObject;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.MetaSetVariable;
import tool.compiler.java.visit.ObjsSubseteqX;

/**
 * Lit <: Expr <: Term <: Node					<br>
 * Lit <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenLitExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	private AbstractObject absObj;
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Lit lit = (Lit) this.node();
		Report.report(2, "[Enter] Literal: " + lit);
		
		absObj = new AbstractObject(lit);
		v.addToSet(absObj);
		Report.report(3, "\t[AbstractObject] " + absObj + " (" + lit.getClass().getInterfaces()[0].getSimpleName() + ")");
//		Report.report(3, "\t[AbstractObject] "  + absObj + " (Object " + absObj.getType() + ")");
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Lit lit = (Lit) this.node();
		Report.report(2, "[Leave] Literal: " + lit);
		
		// int / char / float / boolean / string / class / null
		//   1. T{Chi} 변수 생성
		MetaSetVariable tchi = new MetaSetVariable(lit.type());
		Report.report(3, "\t[MetaSetVariable] " + tchi + " (For return: New)");
		
		//   2. T{o} <: T{Chi} 제약식을 추가
		ObjsSubseteqX ox = new ObjsSubseteqX(absObj, tchi);
		v.getCurrMC().addMetaConstraint(ox);
		Report.report(3, "\t[ObjsSubseteqX] " + ox);
		
		//   3. return T{Chi}
		setMetaSetVar(tchi);
		
		return super.equGenLeave(v);
	}
}