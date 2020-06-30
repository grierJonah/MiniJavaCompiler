import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Scanner.*;
import Parser.*;
import AST.*;
import AST.Visitor.*;
import java_cup.runtime.Symbol;
import symbolTable.ConstructSymTableVisitor;
import symbolTable.SymbolTable;
import symbolTable.TypeCheckVisitor;

import Parser.parser;
import Scanner.scanner;
import codegen.CodeGeneration;

/*
 * Class TestTypeCheck performs a type check on the given grammar.
 */
public class TestTypeCheck {
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
				System.out.println("Types Checked Successfully");
			}
		} catch (Exception e) {
			System.err.println("Unexpected compiler error: " + e.toString());
			e.printStackTrace();
		}
	}
}
