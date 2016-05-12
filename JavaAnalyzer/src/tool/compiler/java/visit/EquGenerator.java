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
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;

public class EquGenerator extends ContextVisitor {
	
	// TODO: 인포와 테이블을 저장하는 자료구조 정하기
	// 후보1 LinkedHashSet/LinkedHashMap: hashCode()메서드와 equals() 메서드 오버라이딩으로 중복 제거 가능.
	// 후보2 별도의 중복이 제거된 자료 구조: 고려 가능한 후보.
	private static LinkedHashSet<MethodInfo> methodDeclInfoSet;
	private static LinkedHashSet<AbstractObjectInfo> abstractObjectInfoSet;
	
	private static LinkedHashMap<MethodCallInfo, LinkedHashSet<MethodTableRow>> methodTableMap;
	private static LinkedHashMap<FieldInfo, LinkedHashSet<FieldTableRow>> fieldTableMap;
	
	private static LinkedHashSet<Constraint> constraintSet;
	
	private static LocalEnv localEnv;
	
	private static final String OutputFileName = "tables.txt";
	
	static {
		methodDeclInfoSet = new LinkedHashSet<>();
		abstractObjectInfoSet = new LinkedHashSet<>();
		
		methodTableMap = new LinkedHashMap<>();
		fieldTableMap = new LinkedHashMap<>();
		
		constraintSet = new LinkedHashSet<>();
		
		localEnv = new LocalEnv();
	}
	
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
		printTablesToConsole();
		printConstraintsToConsole();
		writeTablesToFile();
		
//		FieldEquation fe = new FieldEquation(fieldTableMap.keySet().iterator().next(), fieldTableMap.values().iterator().next());
		
//		System.out.println(fe);
		
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
		
		return lang().equGen(n, this);
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
	 * @param methodDeclInfo
	 */
	public void addToSet(MethodInfo methodDeclInfo) {
		methodDeclInfoSet.add(methodDeclInfo);
	}
	
	/**
	 * 메서드 호출 인포 집합에 추가
	 * @param methodCallInfo
	 */
	public void addToSet(MethodCallInfo methodCallInfo) {
		methodTableMap.put(methodCallInfo, null);
	}
	
	/**
	 * 필드 인포 집합에 추가
	 * @param fieldDeclInfo
	 */
	public void addToSet(FieldInfo fieldDeclInfo) {
		fieldTableMap.put(fieldDeclInfo, null);
	}
	
	/**
	 * 추상 객체 인포 집합에 추가
	 * @param abstractObjectInfo
	 */
	public void addToSet(AbstractObjectInfo abstractObjectInfo) {
		abstractObjectInfoSet.add(abstractObjectInfo);
	}
	
	/**
	 * 제약식 집합에 추가
	 * @param constraint
	 */
	public void addToSet(Constraint constraint) {
		constraintSet.add(constraint);
	}
	
	/**
	 * 테이블 생성 및 테이블 집합에 추가
	 */
	public void generateTables() {
		for(AbstractObjectInfo absObjInfo: abstractObjectInfoSet) {
			for(Entry<MethodCallInfo, LinkedHashSet<MethodTableRow>> miEntry: methodTableMap.entrySet()) {
				try {
//					MethodTable.checkArguments(absObjInfo, miEntry.getKey());	// IllegalArgumentException 발생
					MethodTableRow mt = new MethodTableRow(absObjInfo, miEntry.getKey());	// IllegalArgumentException 발생
					
					if(miEntry.getValue() == null) {
						miEntry.setValue(new LinkedHashSet<MethodTableRow>());
					}
					
					miEntry.getValue().add(mt);
				} catch (IllegalArgumentException ignored) {}	// 무시
			}
			
			for(Entry<FieldInfo, LinkedHashSet<FieldTableRow>> fiEntry: fieldTableMap.entrySet()) {
				try {
//					FieldTable.checkArguments(absObjInfo, fiEntry.getKey());	// IllegalArgumentException 발생
					FieldTableRow ft = new FieldTableRow(absObjInfo, fiEntry.getKey());	// IllegalArgumentException 발생
					
					if(fiEntry.getValue() == null) {
						fiEntry.setValue(new LinkedHashSet<FieldTableRow>());
					}
					
					fiEntry.getValue().add(ft);
				} catch (IllegalArgumentException ignored) {}	// 무시
			}
		}
	}
	
	public void generateEquations() {
		
	}
	
	/**
	 * 테이블을 콘솔에 출력
	 */
	 private void printTablesToConsole() {
		Report.report(1, "\n----- Tables -----");
		for(Entry<FieldInfo, LinkedHashSet<FieldTableRow>> fiEntry : fieldTableMap.entrySet()) {
			try {
				Report.report(1, CollUtil.getNLStringOf(fiEntry.getValue()));
			} catch (NullPointerException ignored) {}
		}
		
		for(Entry<MethodCallInfo, LinkedHashSet<MethodTableRow>> miEntry : methodTableMap.entrySet()) {
			try {
				Report.report(1, CollUtil.getNLStringOf(miEntry.getValue()));
			} catch (NullPointerException ignored) {}
		}
	 }
	 
	/**
	 * 테이블을 콘솔에 출력
	 */
	private void printConstraintsToConsole() {
		Report.report(1, "\n----- Constraints -----");
		Report.report(1, CollUtil.getNLStringOf(constraintSet));
	}
	
	/**
	 * 테이블을 파일에 출력
	 */
	private void writeTablesToFile() {
		try {
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(OutputFileName));
			
			dos.write("----- Tables -----\n\n".getBytes());
			for(Entry<FieldInfo, LinkedHashSet<FieldTableRow>> fiEntry : fieldTableMap.entrySet()) {
				try {
					CollUtil.writeToOutputStream(dos, fiEntry.getValue());
				} catch (NullPointerException ignored) {}
			}
			
			for(Entry<MethodCallInfo, LinkedHashSet<MethodTableRow>> miEntry : methodTableMap.entrySet()) {
				try {
					CollUtil.writeToOutputStream(dos, miEntry.getValue());
				} catch (NullPointerException ignored) {}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}