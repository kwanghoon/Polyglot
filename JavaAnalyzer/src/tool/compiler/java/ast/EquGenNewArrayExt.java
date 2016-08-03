package tool.compiler.java.ast;

import polyglot.ast.ArrayInit;
import polyglot.ast.Expr;
import polyglot.ast.NewArray;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5ArrayType;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.aos.AbstractObject;
import tool.compiler.java.aos.ArrayMetaSetVariable;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.constraint.ObjsSubseteqX;
import tool.compiler.java.constraint.XSubseteqY;
import tool.compiler.java.util.ReportUtil;
import tool.compiler.java.util.ReportUtil.MetaSetVarGoal;
import tool.compiler.java.util.ReportUtil.MetaSetVarSource;
import tool.compiler.java.visit.EquGenerator;

/**
 * NewArray <: Expr <: Term <: Node					<br>
 * NewArray <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenNewArrayExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	public static final String KIND = "New Array";
	
	private AbstractObject absObj;
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ReportUtil.enterReport(this);
		NewArray nwArr = (NewArray) this.node();
		
		if(nwArr.init() == null) {
			absObj = new AbstractObject(nwArr);
			v.addToSet(absObj);
			ReportUtil.report(absObj);
		}
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ReportUtil.leaveReport(this);
		NewArray nwArr = (NewArray) this.node();
		
		// new C[n] / new C[]{e1, ... , en}
		ArrayInit es = nwArr.init();
		ArrayMetaSetVariable cchi;
		
		//   1A. new C[n]
		//       새로운 집합변수 Chi를 생성해서 C[]{Chi}를 만들고, C[]{o} <: C[]{Chi}를 제약식 집합에 추가
		if(es == null) {
			//   1A-1. C[]{Chi} 변수 생성 (NewArray의 타입에 대한 MSV)
			cchi = new ArrayMetaSetVariable((JL5ArrayType) nwArr.type());
			ReportUtil.report(cchi, MetaSetVarSource.New, MetaSetVarGoal.Return);
			
			//   1A-2. C[]{o} <: C[]{Chi} 제약식을 추가
			ObjsSubseteqX ox = new ObjsSubseteqX(absObj, cchi);
			v.getCurrCF().addMetaConstraint(ox);
			ReportUtil.report(ox);
			
			//   1A-3. n의 타입 int{Chij}을 가져와 int{Chij} <: Ci{Chii}.length 제약식을 추가
			//         그 하위 차원에 대하여서도 같은 형태의 제약식을 추가
			XSubseteqY xy;
			ArrayMetaSetVariable cichii;	// C{Chi}의 하위 레벨 base Ci{Chii}
			MetaSetVariable tjchij;			// 선언에서 지정한 길이에 대한 타입 int{Chij}
			MetaSetVariable cichii_length;
			MetaSetVariable cichii_base = cchi;
			
			for(Expr ns : nwArr.dims()) {
				//   1A-3a. 현재 레벨 갱신
				cichii = (ArrayMetaSetVariable) cichii_base;	// 현재 Ci{Chii} 갱신
				cichii_length = cichii.length();
				ReportUtil.report(cichii_length, MetaSetVarSource.ArrayLength, MetaSetVarGoal.ArraySubFlow);
				tjchij = metaSetVar(ns);						// 현재 int{Chij} 갱신
				ReportUtil.report(tjchij, MetaSetVarSource.ArrayDimension, MetaSetVarGoal.ArraySubFlow);
				
				//   1A-3b. int{Chij} <: Ci{Chii}.length 제약식을 추가 (각각 int 타입)
				xy = new XSubseteqY(tjchij, cichii_length);
				v.getCurrCF().addMetaConstraint(xy);
				ReportUtil.report(xy);
				
				//   1A-3c. Ci{Chii}의 base를 가져옴
				cichii_base = cichii.base();	// Ci{Chii}의 base
				
				//   1A-3d. base가 Array가 아니면 종료
				// TODO: dims 리스트의 크기와 cchi의 차원이 다른 경우가 발생하는가
				// 발생하면 안되며, 확실히 발생하지 않는다면 아래의 if 블록은 지울 수 있다.
//				if(!(cichi_base instanceof ArrayMetaSetVariable)) {
//					break;
//				}
			}
		}
		
		//   1B. new C[]{e1, ... , en}
		//       {e1, ... , en}의 타입 C[]{Chi}을 가져오기
		else {
			cchi = (ArrayMetaSetVariable) metaSetVar(es);
			ReportUtil.report(cchi, MetaSetVarSource.ArrayInit, MetaSetVarGoal.Return);
		}
		
		//   2. return C[]{Chi}
		setMetaSetVar(cchi);
		
		return super.equGenLeave(v);
	}
	
	@Override
	public String getKind() {
		return KIND;
	}
}