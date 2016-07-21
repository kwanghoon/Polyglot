package tool.compiler.java.ast;

import polyglot.ast.Formal;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5LocalInstance;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.MetaSetVariable;

/**
 * Formal <: VarDecl <: Term <: Node
 * @author LHJ
 */
public class EquGenFormalExt extends EquGenExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		Formal fm = (Formal) this.node();
		Report.report(2, "[Enter] Formal: " + fm);
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		Formal fm = (Formal) this.node();
		Report.report(2, "[Leave] Formal: " + fm);
		
		// TODO: CC의 Field처럼, 구별을 위해 타입인스턴스도 Map으로 함께 저장할 필요가 있을 것도 같음
		MetaSetVariable msv = new MetaSetVariable(fm.type().type());
		v.getCurrMC().addFormal(msv);
		v.getLocalEnv().add((JL5LocalInstance) fm.localInstance(), msv);
		Report.report(3, "\t[MetaSetVariable] " + msv + " (For Env.: New)");
		
		return super.equGenLeave(v);
	}
}