import Scanner.*;
import Parser.*;
import AST.*;
import AST.Visitor.*;
import java_cup.runtime.Symbol;
import symbolTable.ConstructSymTableVisitor;
import symbolTable.SymbolTable;
import symbolTable.TypeCheckVisitor;

import java.util.*;

import java.io.*;

/*
 * Class Test Parser tests that the written source code is in good format and contains no insufficiencies based on the
 * MiniJava grammar. Additionally, it builds a symbol table and type checks, however, it has no effect to the end outcome.
 */
public class TestParser {
	public static void main(String [] args) {
		try {
			// create a scanner on the input file
			scanner s = new scanner(new BufferedReader(new InputStreamReader(System.in)));
			parser p = new parser(s);
			Symbol root;
			// replace p.parse() with p.debug_parse() in next line to see trace of
			// parser shift/reduce actions during parse
			root = p.parse();

			Program program = (Program)root.value;
			
			// Prints written program to console
			program.accept(new PrettyPrintVisitor());

			ConstructSymTableVisitor symTable = new ConstructSymTableVisitor();
			program.accept(symTable);

			// Used to catch any errors when type checking
			List<String> errorList = new ArrayList<String>();

			TypeCheckVisitor typeChecker = new TypeCheckVisitor(symTable.getSymbolTable(), errorList);
			program.accept(typeChecker);

			if (errorList.size() > 0) {
				System.out.println("There are: " + errorList.size() + " errors required to fix!");
				for (String errors : errorList) {
					System.out.println(errors);
				}
			} else
				System.out.println("Types checked successfully!");
				System.out.print("\nParsing completed"); 
		} catch (Exception e) {
			// yuck: some kind of error in the compiler implementation
			// that we're not expecting (a bug!)
			System.err.println("Unexpected internal compiler error: " + 
					e.toString());
			// print out a stack dump
			e.printStackTrace();
		}
	}



	/*private void printTree(Node root) {
    	// AST NODE & all Children
    	//			-> accept method that uses visit(this)

    	if (this.children == null) {
    		System.out.println(this);
    	} else {
    		for (int i = 1; i<=length(root.children); i++) {  			
    			Visitor ppVisitor = new PrettyPrintVisitor();
    			root.accept(ppVisitor);
    			prettyPrintVisitor(root.children);
    			print(root.children(i));
    		}

    		//public accept(Visitor v) {
    		// 	for(all the children) {
    				// this.children(i);
    				//accept(v)
    		//}
    		//	v.visit(this);
    		//}

    	}
    } */
}
