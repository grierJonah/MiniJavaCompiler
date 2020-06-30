import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import AST.Program;
import Parser.parser;
import Scanner.scanner;
import codegen.CodeGeneration;
import java_cup.runtime.Symbol;
import symbolTable.ConstructSymTableVisitor;
import symbolTable.TypeCheckVisitor;

/*
 * Class TestCodegen builds and solves the final end problem for the compiler. It goes through all processes of 
 * parsing, the construction of symbol tables, typechecking, and then translating the source code into Assembly
 * in the specified directory: "/Users/jonahgrier/Desktop/Student_repo_Jonah_Grier/minijava/src/files/output.s"
 */
public class TestCodegen {
	public static void main(String[] args) {
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
