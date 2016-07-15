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
		
		// TODO: Decl할 때 Formals의 MetaSetVariabls를 생성하고 MC에서 가지고 있다가 FormalExt로 가져올 것인지,
		// FormalExt에서 생성하여 MC에 저장할 것인지 결정
		// TODO: CC의 Field처럼, 구별을 위해 타입인스턴스도 Map으로 함께 저장할 필요가 있을 것도 같음
		MetaSetVariable msv = new MetaSetVariable(fm.type().type());
		v.getCurrMC().addFormal(msv);
		v.getLocalEnv().add((JL5LocalInstance) fm.localInstance(), msv);
		
		Report.report(2, "[Leave] Formal: " + fm);
		return super.equGenLeave(v);
	}
}