package tool.compiler.java.ast;

import polyglot.ast.ArrayInit;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ext.jl5.types.JL5ArrayType;
import polyglot.main.Report;
import polyglot.util.SerialVersionUID;
import tool.compiler.java.util.EquGenUtil;
import tool.compiler.java.visit.AbstractObject;
import tool.compiler.java.visit.AbstractObject.Info;
import tool.compiler.java.visit.ArrayMetaSetVariable;
import tool.compiler.java.visit.EquGenerator;
import tool.compiler.java.visit.MetaSetVariable;
import tool.compiler.java.visit.ObjsSubseteqX;
import tool.compiler.java.visit.XSubseteqY;

import java.util.Collection;

/**
 * ArrayInit <: Expr <: Term <: Node				<br>
 * ArrayInit <: Expr <: Receiver <: Prefix <: Node
 * @author LHJ
 */
public class EquGenArrayInitExt extends EquGenExprExt {
	private static final long serialVersionUID = SerialVersionUID.generate();
	
	private AbstractObject absObj;
	private AbstractObject length;
	
	@Override
	public EquGenerator equGenEnter(EquGenerator v) {
		ArrayInit arrInit = (ArrayInit) this.node();
		Report.report(2, "[Enter] Array Initialization: " + arrInit + " (type: " + arrInit.type() + ")");
		
		absObj = new AbstractObject(arrInit);
		v.addToSet(absObj);
		Report.report(3, "\t[AbstractObject] "  + absObj + " (Object " + absObj.getType() + ")");
		
		length = new AbstractObject(arrInit, Info.ArrayInitLength);
		v.addToSet(length);
		Report.report(3, "\t[AbstractObject] "  + length + " (Object " + length.getType() + " (length Field))");
		
		return super.equGenEnter(v);
	}
	
	@Override
	public Node equGenLeave(EquGenerator v) {
		ArrayInit arrInit = (ArrayInit) this.node();
		Report.report(2, "[Leave] Array Initialization: " + arrInit);
		
		// {e1, ... , en}
		//   1. C[]{Chi} 변수 생성
		ArrayMetaSetVariable cchi = new ArrayMetaSetVariable((JL5ArrayType) arrInit.type());
		Report.report(3, "\t[MetaSetVariable] " + cchi + " (For return: New)");
		
		//   2-1. C[]{o} <: C[]{Chi} 제약식을 추가
		ObjsSubseteqX ox = new ObjsSubseteqX(absObj, cchi);
		v.getCurrMC().addMetaConstraint(ox);
		Report.report(3, "\t[ObjsSubseteqX] " + ox);
		
		//   2-2. C{Chi}.length에 대한 제약식 생성
		ox = new ObjsSubseteqX(length, cchi.length());
		v.getCurrMC().addMetaConstraint(ox);
		Report.report(3, "\t[ObjsSubseteqX] " + ox);
		
		//   2-3. elements에 대한 데이터 플로우
		for(Expr ei : arrInit.elements()) {
			//   2-3a.C[]{Chi}의 base의 타입 C[]{Chi}.base와 ei의 타입 Ci{Chii}를 가져온 다음
			//         (element의 타입에 대한 MSV)
			MetaSetVariable cchi_base = cchi.base();
			MetaSetVariable cichii = metaSetVar(ei);
			
			//   2-3b. Ci{Chii} <: C[]{Chi}.base 제약식을 추가 (element에 대한 Top Level)
			XSubseteqY xy = new XSubseteqY(cichii, cchi_base);
			v.getCurrMC().addMetaConstraint(xy);
			Report.report(3, "\t[XSubseteqY] " + xy);
			
			//   2-3c. ei가 배열인 경우, Ci{Chii} <: C[]{Chi}.base의 하위 레벨 제약식을 집합에 추가
			//         (Top Level 아래의 MetaSetVariable의 데이터 플로우)
			if(EquGenUtil.isArray(ei.type())) {
				Collection<XSubseteqY> xys = EquGenUtil.constrain(
						(ArrayMetaSetVariable) cichii, 
						(ArrayMetaSetVariable) cchi_base);
				v.getCurrMC().addMetaConstraints(xys);
			}
		}
		
		//   3. return C[]{Chi}
		setMetaSetVar(cchi);
		
		return super.equGenLeave(v);
	}
}