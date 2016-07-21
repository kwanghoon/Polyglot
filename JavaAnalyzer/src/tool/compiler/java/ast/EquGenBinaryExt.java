package tool.compiler.java.ast;

import polyglot.ast.Binary;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.AbstractObject;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.MetaSetVariable;
import tool.compiler.java.visit.ObjsSubseteqX;

/**
 * Binary <: Expr <: Term <: Node				<br>
 * Binary <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenBinaryExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	private AbstractObject absObj;
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Binary binary = (Binary) this.node();
		Report.report(2, "[Enter] Binary: " + binary);
		
		absObj = new AbstractObject(binary);
		v.addToSet(absObj);
		Report.report(3, "\t[AbstractObject] " + absObj + " (" + absObj.getType() + ")");
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Binary binary = (Binary) this.node();
		Report.report(2, "[Leave] Binary: " + binary);
		
		// e1 > e2 / e1 < e2 / e1 == e2 / e1 <= e2 / e1 >= e2 / e1 != e2 /
		// e1 || e2 / e1 && e2 / e1 + e2 / e1 - e2 / e1 * e2 / e1 / e2 / e1 % e2 /
		// e1 | e2 / e1 & e2 / e1 ^ e2 / e1 << e2 / e1 >> e2 / e1 >>> e2
		//   1. T{Chi} 변수 생성
		MetaSetVariable tchi = new MetaSetVariable(binary.type());
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