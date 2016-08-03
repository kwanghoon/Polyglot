package tool.compiler.java.ast;

import polyglot.ast.Expr;
import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5LocalInstance;
import polyglot.types.Type;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.ArrayMetaSetVariable;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.constraint.XSubseteqY;
import tool.compiler.java.util.EquGenUtil;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;
import tool.compiler.java.visit.EquGenerator;

import java.util.Collection;

/**
 * LocalDecl <: ForInit <: Stmt <: Term <: Node	<br>
 * LocalDecl <: VarDecl <: Term <: Node
 * @author LHJ
 */
public class EquGenLocalDeclExt extends EquGenStmtExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "Local Declaration";
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
//		LocalDecl localDecl = (LocalDecl) this.node();
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		LocalDecl localDecl = (LocalDecl) this.node();
		Type localDeclType = localDecl.type().type();
		JL5LocalInstance localIns = (JL5LocalInstance) localDecl.localInstance();
		
		// C x = e / C[] x = e;
		//   1. x : C{Chi1} / C[]{Chi1(base, elem)}을 현재 env에 추가 (Chi1은 새로운 변수)
		MetaSetVariable cchi1 = MetaSetVariable.create(localDeclType);
		v.peekTypeEnv().add(localIns, cchi1);
		ReportUtil.report(cchi1, MetaSetVarSource.New, MetaSetVarGoal.Environment);
		
		//   2. e가 있는지 확인 후
		Expr e = localDecl.init();
		if(e != null) {
			//   2-1. e의 타입 D{Chi2}를 가져오고
			MetaSetVariable dchi2 = EquGenExprExt.metaSetVar(e);
			ReportUtil.report(dchi2, MetaSetVarSource.Rvalue, MetaSetVarGoal.Flow);
			
			//   2-2. D{Chi2} <: C{Chi1}을 제약식 집합에 추가
			XSubseteqY xy = new XSubseteqY(dchi2, cchi1);
			v.getCurrCF().addMetaConstraint(xy);
			ReportUtil.report(xy);
			
			//   2-3. 배열 변수인 경우, Top Level 아래의 MetaSetVariable의 데이터 플로우
			if(EquGenUtil.isArray(localDeclType)) {
				Collection<XSubseteqY> xys = EquGenUtil.constrain(
						(ArrayMetaSetVariable) dchi2, 
						(ArrayMetaSetVariable) cchi1);
				v.getCurrCF().addMetaConstraints(xys);
			}
		}
		
		//   3. 리턴할 MataSetVar는 없음
		setLocalEnv(v.peekTypeEnv().getCurrEnv());
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}