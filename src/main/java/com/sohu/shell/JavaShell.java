/**
 * 
 */
package com.sohu.shell;

import java.io.File;
import java.util.Scanner;

/**
 * @author hongliuliao
 *
 * createTime:2012-12-28 上午11:52:37
 */
public class JavaShell {
	
	CodeContainer codeContainer = new CodeContainer();
	
	CodeGenerator codeGenerator = new CodeGenerator();
	
	CodeRunner codeRunner = new CodeRunner();
	
	String tempFileName = "Temp";
	
	int buildTimes = 0;
	
	public void handleInput(String line) {
		if(line.trim().equals("")) {
			return;
		}
		if(line.trim().equals("exit")) {
			System.exit(0);
		}
		
		if(line.trim().startsWith("import")) {
			codeContainer.addImport(line);
		}else {
			codeContainer.addCode(line);
		}
		buildTimes ++;
		String basePath = JavaShell.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/../generate/";
		this.buildGenerateFolder(basePath);
		String className = tempFileName + buildTimes;
		codeContainer.setClassName(className);
		
		String javaCode = codeGenerator.generator(codeContainer);
		boolean result = codeRunner.compile(basePath + className + ".java", javaCode);
		if(!result) {
			this.codeContainer.removeCode(line);
			return;
		}
		codeRunner.loadClassAndRun(basePath + className + ".class", className);
		
	}
	
	public void buildGenerateFolder(String path) {
		File file = new File(path);
		if(!file.exists()) {
			file.mkdir();
		}
	}
	
	public static void printlnSign() {
		System.out.print(">");
	}

	public static void main(String[] args) {
		System.out.println("Welcome to use jshell!");
		printlnSign();
		
		JavaShell shell = new JavaShell();
		Scanner scanner = new Scanner(System.in);
		
		while(scanner.hasNext()) {
			String line = scanner.nextLine();
			shell.handleInput(line);
			printlnSign();
		}
	}
}
