package tool.compiler.java.ast;

import polyglot.ast.Instanceof;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.AbstractObject;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.MetaSetVariable;
import tool.compiler.java.visit.ObjsSubseteqX;

/**
 * Instanceof <: Expr <: Term <: Node				<br>
 * Instanceof <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenInstanceofExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	private AbstractObject absObj;
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Instanceof insof = (Instanceof) this.node();
		Report.report(2, "[Enter] Instanceof: " + insof);
		
		absObj = new AbstractObject(insof);
		v.addToSet(absObj);
		Report.report(3, "\t[AbstractObject] "  + absObj + " (Object " + absObj.getType() + ")");
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Instanceof insof = (Instanceof) this.node();
		Report.report(2, "[Leave] Instanceof: " + insof);
		
		// instanceof
		//   1. boolean{Chi} 변수 생성
		MetaSetVariable tchi = new MetaSetVariable(insof.type());
		Report.report(3, "\t[MetaSetVariable] " + tchi + " (For return: New)");
		
		//   2. boolean{o} <: boolean{Chi} 제약식을 추가
		ObjsSubseteqX ox = new ObjsSubseteqX(absObj, tchi);
		v.getCurrMC().addMetaConstraint(ox);
		Report.report(3, "\t[ObjsSubseteqX] " + ox);
		
		//   3. return boolean{Chi}
		setMetaSetVar(tchi);
		
		
		return super.equGenLeave(v);
	}
}