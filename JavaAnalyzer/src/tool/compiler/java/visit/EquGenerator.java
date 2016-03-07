package tool.compiler.java.visit;

import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;
import tool.compiler.java.ast.EquGenLang;
import tool.compiler.java.util.CollUtil;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashSet;

public class EquGenerator extends ContextVisitor {
	
	// TODO: 인포와 테이블을 저장하는 자료구조 정하기
	// 후보1 LinkedList: 중복 발생가능하므로 좋지는 않은 듯.
	// 후보2 LinkedHashSet: 이 프로그램 논리 상의 중복이 제거되지 않으므로 대안 필요.
	// 후보3 별도의 중복이 제거된 리스트: LinkedList를 확장하여 중복 제거 리스트 클래스를 별도로 만듦.
	private static LinkedHashSet<MethodInfo> methodDeclInfoSet;
	private static LinkedHashSet<MethodCallInfo> methodCallInfoSet;
	private static LinkedHashSet<FieldInfo> fieldDeclInfoSet;
	private static LinkedHashSet<AbstractObjectInfo> abstractObjectInfoSet;
	
	private static LinkedHashSet<MethodTable> methodTableSet;
	private static LinkedHashSet<FieldTable> fieldTableSet;
	
	static {
		methodDeclInfoSet = new LinkedHashSet<>();
		methodCallInfoSet = new LinkedHashSet<>();
		fieldDeclInfoSet = new LinkedHashSet<>();
		abstractObjectInfoSet = new LinkedHashSet<>();
		
		methodTableSet = new LinkedHashSet<>();
		fieldTableSet = new LinkedHashSet<>();
	}
	
	private static final String OutputFileName = "tables.txt";
	
	public EquGenerator(Job job, TypeSystem ts, NodeFactory nf) {
		super(job, ts, nf);
	}
	
	@Override
	public EquGenLang lang() {
		return (EquGenLang) super.lang();
	}

	@Override
	public NodeVisitor begin() {
		Report.report(1, "EquGenerator: begin()");
		NodeVisitor nv = super.begin();
		return nv;
	}

	@Override
	public void finish() {
		Report.report(1, "EquGenerator: finish()");
		generateTables();
		
		Report.report(1, "\n----- Tables -----");
		Report.report(1, CollUtil.getNLStringOf(fieldTableSet));
		Report.report(1, CollUtil.getNLStringOf(methodTableSet));
		
		writeTablesToFile();
		
		super.finish();
	}
	
//	@Override
//	protected NodeVisitor enterCall(Node parent, Node n)
//			throws SemanticException {
//
////		reportPackage(n);
//		
//		return super.enterCall(parent, n);
//	}

	@Override
	protected NodeVisitor enterCall(Node n) throws SemanticException {

		return lang().equGenEnter(n, this);
	}

//	@Override
//	protected Node leaveCall(Node parent, Node old, Node n, NodeVisitor v)
//			throws SemanticException {
//
//		return super.leaveCall(parent, old, n, v);
//	}

	@Override
	protected Node leaveCall(Node old, Node n, NodeVisitor v)
			throws SemanticException {

		return lang().equGen(n,  this);
	}

//	@Override
//	protected Node leaveCall(Node n) throws SemanticException {
//
//		return super.leaveCall(n);
//	}

	@Override
	public TypeSystem typeSystem() {
		return super.typeSystem();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	/**
	 * 메서드 선언 인포 집합에 추가
	 * @param methodInfo
	 */
	public void addToSet(MethodInfo methodInfo) {
		methodDeclInfoSet.add(methodInfo);
	}
	
	/**
	 * 메서드 호출 인포 집합에 추가
	 * @param methodInfo
	 */
	public void addToSet(MethodCallInfo methodInfo) {
		methodCallInfoSet.add(methodInfo);
	}
	
	/**
	 * 필드 인포 집합에 추가
	 * @param fieldInfo
	 */
	public void addToDeclSet(FieldInfo fieldInfo) {
		fieldDeclInfoSet.add(fieldInfo);
	}
	
	/**
	 * 추상 객체 인포 집합에 추가
	 * @param abstractObjectInfo
	 */
	public void addToAbsObjSet(AbstractObjectInfo abstractObjectInfo) {
		abstractObjectInfoSet.add(abstractObjectInfo);
	}
	
	/**
	 * 테이블 생성 및 테이블 집합에 추가
	 */
	public void generateTables() {
		for(AbstractObjectInfo absObjInfo: abstractObjectInfoSet) {
			for(MethodCallInfo methodInfo: methodCallInfoSet) {
				try {
//					MethodTable.checkArguments(absObjInfo, methodInfo);	// IllegalArgumentException 발생
					MethodTable mt = new MethodTable(absObjInfo, methodInfo);	// IllegalArgumentException 발생
					boolean contain = false;
					
					for(MethodTable currMt : methodTableSet) {		// 같은 메서드를 여러 번 호출할 수 있으므로 중복 제거
						if(currMt.equals(mt)) {
							contain = true;
							break;
						}
					}
					
					if(!contain) {
						methodTableSet.add(mt);
					}
				} catch (IllegalArgumentException ignored) {}	// 무시
			}
			
			for(FieldInfo fieldInfo: fieldDeclInfoSet) {
				try {
//					FieldTable.checkArguments(absObjInfo, fieldInfo);	// IllegalArgumentException 발생
					fieldTableSet.add(new FieldTable(absObjInfo, fieldInfo));	// IllegalArgumentException 발생
				} catch (IllegalArgumentException ignored) {}	// 무시
			}
		}
	}
	
	/**
	 * 테이블을 파일에 출력
	 */
	private void writeTablesToFile() {
		try {
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(OutputFileName));
			
			dos.write("----- Tables -----\n\n".getBytes());
			CollUtil.writeToOutputStream(dos, fieldTableSet);
			CollUtil.writeToOutputStream(dos, methodTableSet);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}