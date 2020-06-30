import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import symbolTable.TypeCheckVisitor;

import Parser.*;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import AST.Program;
import AST.Visitor.PrettyPrintVisitor;
import Scanner.scanner;
import codegen.CodeGeneration;
import java_cup.runtime.Symbol;
import symbolTable.ConstructSymTableVisitor;

public class MiniJava {

	private final static int fail_status = 1;  // Global failure status variable
	private final static int successful_status = 0; // Global success status variable

	public static void MiniJava(String[] args) {
		File infile = null;
		if (args.length <= 2 || args.length > 4) {
			System.out.println("Error: Arguments should be "
					+ "java MiniJava [-A, -S, -P, or -T] [filename.java]");
			System.exit(fail_status);
		} else {
			if (args[0] == "java" && args[1] == "MiniJava") {
				switch(args[2]) {
				case "-A":
					lexicalAnalysis();
					break;
				case "-S":
					parser();
					break;
				case "-P":
					parser();
					break;
				case "-T":
					constructSymbolTables();
					break;
				default:
					codegeneration();
				}
			}
		}
	}

	/* Helper function for lexically analyzing the words. This function takes in through the build file the specific 
	 * file to be tokenized. It takes each character and figures out if the group of characters match any of the 
	 * specified characters in the SYM.java file used minijava.cup. The chunk of the process is performed by the 
	 * function t = s.next_token() on line 47. This function uses more-or-less magic to process and match characters
	 * to tokens. It uses a combination of regex, LALR(1) parsing, and ... magic. To see the guts of the function 
	 * check out the scanner.java class (line ~763).
	 * 
	 * After matching a terminal, the scanner class uses function "symbolToString" to print the matched character
	 * stream into a recognizable keyword (delimiter, operator, identifier, keyword).
	 */
	private static void lexicalAnalysis() {
		try {
			scanner s = new scanner(new BufferedReader(new InputStreamReader(System.in)));
			Symbol t = s.next_token();
			while (t.sym != sym.EOF) {
				System.out.print(s.symbolToString(t));
				t = s.next_token();
			}
		} catch (Exception e) {
			System.err.println("Unexpected internal compiler error: " + e.toString());
			e.printStackTrace();
			System.exit(fail_status);
		}
		System.out.println("Lexical Analysis Completed");
		System.exit(successful_status);
	}

	private static void parser() {
		try {
			scanner s = new scanner(new BufferedReader(new InputStreamReader(System.in)));
			parser p = new parser(s);
			Symbol root;

			root = p.parse();

			Program program = (Program)root.value;

			program.accept(new PrettyPrintVisitor());

			System.out.println("Parsing completed");
			System.exit(successful_status);
		} catch (Exception e) {
			System.err.println("Unexpected internal compile error: " + e.toString());
			e.printStackTrace();
			System.exit(fail_status);
		}
	}

	private static void constructSymbolTables() {
		try {
			scanner s = new scanner(new BufferedReader(new InputStreamReader(System.in)));
			parser p = new parser(s);
			Symbol root;

			root = p.parse();

			Program program = (Program)root.value;
			ConstructSymTableVisitor symTable = new ConstructSymTableVisitor();
			program.accept(symTable);

			List<String> errorList = new ArrayList<String>();			
			TypeCheckVisitor typeChecker = new TypeCheckVisitor(symTable.getSymbolTable(), errorList);
			program.accept(typeChecker);

			for (String error : errorList) {
				System.out.println(error);
			}

			System.out.println("Types checked!");
			System.exit(successful_status);
		} catch(Exception e) {
			System.err.println("Unexpected internal compiler error: " + e.toString());
			e.printStackTrace();
			System.exit(fail_status);
		}
	}

	private static void codegeneration() {
		try {
			scanner s = new scanner(new BufferedReader(new InputStreamReader(System.in)));
			parser p = new parser(s);

			Symbol root;

			root = p.parse();

			Program program = (Program) root.value;

			ConstructSymTableVisitor symTable = new ConstructSymTableVisitor();
			program.accept(symTable);

			List<String> errorList = new ArrayList<String>();

			TypeCheckVisitor typeChecker = new TypeCheckVisitor(symTable.getSymbolTable(), errorList);

			program.accept(typeChecker);

			if (errorList.size() > 0) {
				System.out.println("There are: " + errorList.size() + " errors required to fix!");
				for (String errors : errorList) {
					System.out.println(errors);
				}
			} else {
				System.out.println("Types Checked!");
				System.out.println("Beginning code generation...");
			}

			CodeGeneration cg = new CodeGeneration(symTable.getSymbolTable());
			program.accept(cg);
			System.out.println("Code generation completed");


		} catch (Exception e) {
			System.err.println("Unexpected compiler error: " + e.toString());
			e.printStackTrace();
		}
	}
}

