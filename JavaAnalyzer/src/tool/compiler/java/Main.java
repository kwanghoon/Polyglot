package tool.compiler.java;

import java.io.File;
import java.util.ArrayList;

/**
 * Main is the main program of the compiler extension. It simply invokes
 * Polyglot's main, passing in the extension's ExtensionInfo.
 */
public class Main {
	
	// 컴파일할 파일 타입 지정
	private static final String[] compilableFileTypes = {".java"};
	
	public static void main(String[] args) {
		polyglot.main.Main polyglotMain = new polyglot.main.Main();
		
		try {
			// For Java 7
			// polyglotMain.start(args, new polyglot.ext.jl7.JL7ExtensionInfo());
			
			// For Java 7 plus an extra compilation pass
			polyglotMain.start(getPolyglotArgs(args), new tool.compiler.java.ExtensionInfo());
			
		} catch (polyglot.main.Main.TerminationException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
	
	/**
	 * 컴파일할 파일이 있는 디렉터리 명령행 인자를 반영하기 위한 명령행 인자 가공 메서드.
	 * 입력된 인자가 컴파일할 디렉터리인 경우, 그 하위에 있는 컴파일할 파일을 찾아서 추가함.
	 * 그 이외의 경우, 가공하지 않음.
	 * 
	 * @param clArgs	입력 받은 명령행 인자
	 * @return	가공된 명령행 인자
	 */
	private static String[] getPolyglotArgs(String[] clArgs) {
		
		// 이전 인자가 output directory (-D 또는 -d) 옵션이었는지를 저장.
		// 첫 번째 인자의 이전 인자는 없으므로 false.
		boolean prevArgOutputDirOption = false;
		
		// 결과를 저장할 ArrayList<String> 객체 생성.
		ArrayList<String> result = new ArrayList<>();
		
		// 전체 명령행 인자에 대해...
		for(String arg : clArgs) {
			File argFile = new File(arg);
			
			// 하위 파일을 컴파일할 디렉터리가 아닌 인자: 가공하지 않음.
			if(!argFile.isDirectory() || prevArgOutputDirOption) {
				result.add(arg);
			}
			
			// output 옵션이 아닌 디렉터리: 하위의 컴파일할 파일을 찾아서 추가함.
			else {
				result.addAll(getSubCompilableFilePaths(argFile));
			}
			
			// 현재 다루는 인자가 output directory (-D 또는 -d) 옵션인지 확인.
			// 다음번 루프에 하위 파일을 검색할 디렉터리를 결정하는 데 영향을 미침.
			if(arg.toLowerCase().equals("-d")) {
				prevArgOutputDirOption = true;
			} else {
				prevArgOutputDirOption = false;
			}
		}
		
		return result.toArray(new String[result.size()]);
	}
	
	private static ArrayList<String> getSubCompilableFilePaths(File file) {
		
		File[] packedFile = {file};
		return getSubCompilableFilePaths(packedFile, new ArrayList<String>());
	}
	
	private static ArrayList<String> getSubCompilableFilePaths(File[] files, ArrayList<String> fileList) {
		
		if (files != null) {
			for(File file : files) {
				if(file.isDirectory()) {	// 디렉터리인 경우
					getSubCompilableFilePaths(file.listFiles(), fileList);
				} else if (file.isFile()) {	// 파일인 경우
					String fileName = file.getName().toLowerCase();
					for(String type : compilableFileTypes) {
						if(fileName.endsWith(type)) {
							fileList.add(file.getPath());
						}
					}
				}
			}
		}
		
		return fileList;
	}
}