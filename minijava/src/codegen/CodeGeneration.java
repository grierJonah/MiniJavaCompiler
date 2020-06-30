package codegen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import AST.*;
import symbolTable.SymbolTable;

/*
 * Class CodeGeneration performs the translation of source code into assembly through the use
 * of visitor methods. The output is then directly written to "output.s" located in the full file
 * path: "/Users/jonahgrier/Desktop/Student_repo_Jonah_Grier/minijava/src/files/output.s"
 * 
 * It can then be compiled using "gcc -o [name] output.s boot.c" 
 * Run the new file by using ./[name]
 */
public class CodeGeneration implements CGVisitor {

	private SymbolTable globalTable;
	private File cgText;
	private FileOutputStream fileOutputStream;
	private OutputStreamWriter outputStreamWriter;
	private BufferedWriter writer;
	private int count;


	public CodeGeneration(SymbolTable globalTable) throws FileNotFoundException, IOException {
		cgText = new File("/Users/jonahgrier/Desktop/Student_repo_Jonah_Grier/minijava/src/files/output.s");
		fileOutputStream = new FileOutputStream(cgText);
		outputStreamWriter = new OutputStreamWriter(fileOutputStream);
		writer = new BufferedWriter(outputStreamWriter);
		this.globalTable = globalTable;
		count = 0;
	}


	// Display added for toy example language.  Not used in regular MiniJava
	public void visit(Display n) {
		n.e.accept(this);
	}


	// MainClass m;
	// ClassDeclList cl;
	public void visit(Program n) {
		try {
			writer.write("\t" + ".text");
			writer.write("\n\t" + ".globl" + "\t" + "_asm_main");
		} catch (IOException e) {
			System.out.println("Unexpected file error: " + e.toString());
			e.printStackTrace();
		}
		n.m.accept(this);
		for ( int i = 0; i < n.cl.size(); i++ ) {
			n.cl.get(i).accept(this);
		}
	}


	// Identifier i1,i2;
	// Statement s;
	public void visit(MainClass n) {
		try {
			writer.write("\n" + "_asm_main:");
		} catch (IOException e) {
			System.out.println("Unexpected file error: " + e.toString());
			e.printStackTrace();
		}
		prologue();
		n.i1.accept(this);
		n.i2.accept(this);
		n.s.accept(this);
	}


	// Identifier i;
	// VarDeclList vl;
	// MethodDeclList ml;
	public void visit(ClassDeclSimple n) {
		n.i.accept(this);
		for ( int i = 0; i < n.vl.size(); i++ ) {
			n.vl.get(i).accept(this);
			if ( i+1 < n.vl.size() ) { System.out.println(); }
		}
		for ( int i = 0; i < n.ml.size(); i++ ) {
			n.ml.get(i).accept(this);
		}
		epilogue();
	}


	// Identifier i;
	// Identifier j;
	// VarDeclList vl;
	// MethodDeclList ml;
	public void visit(ClassDeclExtends n) {
		//prologue();
		n.i.accept(this);
		n.j.accept(this);
		for ( int i = 0; i < n.vl.size(); i++ ) {
			n.vl.get(i).accept(this);
			if ( i+1 < n.vl.size() ) { System.out.println(); }
		}
		for ( int i = 0; i < n.ml.size(); i++ ) {
			n.ml.get(i).accept(this);
		}
	}


	// Type t;
	// Identifier i;
	public void visit(VarDecl n) {
		n.t.accept(this);
		n.i.accept(this);
	}


	// Type t;
	// Identifier i;
	// FormalList fl;
	// VarDeclList vl;
	// StatementList sl;
	// Exp e;
	public void visit(MethodDecl n) {
		n.t.accept(this);
		n.i.accept(this);

		for ( int i = 0; i < n.fl.size(); i++ ) {
			n.fl.get(i).accept(this);
			if (i+1 < n.fl.size()) { System.out.print(", "); }
		}
		for ( int i = 0; i < n.vl.size(); i++ ) {
			n.vl.get(i).accept(this);
		}
		for ( int i = 0; i < n.sl.size(); i++ ) {
			n.sl.get(i).accept(this);
			if ( i < n.sl.size() ) { System.out.println(""); }
		}
		n.e.accept(this);
	}


	// Type t;
	// Identifier i;
	public void visit(Formal n) {
		n.t.accept(this);
		System.out.print(" ");
		n.i.accept(this);
	}


	public void visit(IntArrayType n) {
	}


	public void visit(BooleanType n) {
	}


	public void visit(IntegerType n) {
	}

	public void visit(DoubleType doubleType) {
	}


	// String s;
	public void visit(IdentifierType n) {
	}


	// StatementList sl;
	public void visit(Block n) {
		for ( int i = 0; i < n.sl.size(); i++ ) {
			n.sl.get(i).accept(this);
		}
	}


	// Exp e;
	// Statement s1,s2;
	public void visit(If n) {
		n.e.accept(this);
		n.s1.accept(this);
		n.s2.accept(this);
	}


	// Exp e;
	// Statement s;
	public void visit(While n) {
		n.e.accept(this);
		n.s.accept(this);
	}


	// Exp e;
	public void visit(Print n) {
		n.e.accept(this);
		callq("_put");
	}


	// Identifier i;
	// Exp e;
	public void visit(Assign n) {
		n.i.accept(this);
		n.e.accept(this);
	}


	// Identifier i;
	// Exp e1,e2;
	public void visit(ArrayAssign n) {
		n.i.accept(this);
		n.e1.accept(this);
		n.e2.accept(this);
	}


	// Exp e1,e2;
	public void visit(And n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}


	// Exp e1,e2;
	public void visit(LessThan n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}


	// Exp e1,e2;
	public void visit(Plus n) {
		//int num1 = Integer.parseInt(n.e1.toString());
		//int num2 = Integer.parseInt(n.e2.toString());

		n.e1.accept(this);

		n.e2.accept(this);

	}


	// Exp e1,e2;
	public void visit(Minus n) {
		//pushq("%rdi");
		int num1 = Integer.parseInt(n.e1.toString());
		int num2 = Integer.parseInt(n.e2.toString());

		n.e1.accept(this);

		//movq(num1, "%rdi");
		n.e2.accept(this);
		//movq(num2, "%rdi");
	}


	// Exp e1,e2;
	public void visit(Times n) {
		pushq("%rdi");

		n.e1.accept(this);								// Move to %rdi using IntegerLiteral					
		//movq(num1, "%rax");
		n.e2.accept(this);								// Move to %rdi using IntegerLiteral
		//movq(num2, "%rdi");

		imul("%rax", "%rdi");

		//callq("_put");

		//popq("%rax");
	}


	// Exp e1,e2;
	public void visit(ArrayLookup n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}


	// Exp e;
	public void visit(ArrayLength n) {
		n.e.accept(this);
	}


	// Exp e;
	// Identifier i;
	// ExpList el;
	public void visit(Call n) {
		n.e.accept(this);
		n.i.accept(this);
		for ( int i = 0; i < n.el.size(); i++ ) {
			n.el.get(i).accept(this);
			if ( i+1 < n.el.size() ) { System.out.print(", "); }
		}
	}


	// int i;
	public void visit(IntegerLiteral n) {
		String register = "%rdi";
		if (count % 2 == 0) {
			movq(n.i, register);
		} else {
			addq(n.i, register);
		}
		count++;
	}

	// double i
	public void visit(DoubleLiteral doubleLiteral) {
		/* in progress */
	}


	public void visit(True n) {
		String functionCall = "movq";
		String register = "%rdi";
		int True = 0;
		assemblyBuilder(functionCall, True, register);
	}


	public void visit(False n) {
		String functionCall = "movq";
		String register = "%rdi";
		int False = -1;
		assemblyBuilder(functionCall, False, register);
	}

	// String s;
	public void visit(IdentifierExp n) {
	}

	public void visit(This n) {
	}

	// Exp e;
	public void visit(NewArray n) {
		n.e.accept(this);
	}

	// Identifier i;
	public void visit(NewObject n) {
		System.out.print(n.i.s);
	}

	// Exp e;
	public void visit(Not n) {
		n.e.accept(this);
	}

	// String s;
	public void visit(Identifier n) {
	}



	/* * * * * * * * * * * * * * * * * * * * * * *  
	 * Generating Assembly Code Helper Functions *
	 * * * * * * * * * * * * * * * * * * * * * * */


	// Pushing a register		<-- pushq	%rbp
	private void pushq(String register) {
		String functionCall = "pushq";
		assemblyBuilder(functionCall, register);
	}


	// Moving a number into a register		<-- movq	$100,%rax
	private void movq(int num, String register) {
		String functionCall = "movq";
		assemblyBuilder(functionCall, num, register);
	}


	// Moving two registers into each other		<-- movq	%rbp,%rsp
	private void movq(String register1, String register2) {
		String functionCall = "movq";
		assemblyBuilder(functionCall, register1, register2);
	}

	// Adding a number to a register
	private void addq(int num, String register) {
		String functionCall = "addq";
		assemblyBuilder(functionCall, num, register);
	}

	// Adding two registers
	private void addq(String register1, String register2) {
		String functionCall = "addq";
		assemblyBuilder(functionCall, register1, register2);
	}


	// Multiply a number against a register		<-- imul	$3,%rdi
	private void imul(int num, String register) {
		String functionCall = "imul";
		assemblyBuilder(functionCall, num, register);
	}


	// Multiply two registers		<-- imul	%rax,%rdi
	private void imul(String register1, String register2) {
		String functionCall = "imul";
		assemblyBuilder(functionCall, register1, register2);
	}


	// Popping a register		<-- popq	%rdi
	private void popq(String register) {
		String functionCall = "popq";
		assemblyBuilder(functionCall, register);
	}


	// Calling a function		<--	callq	_put 
	private void callq(String method) {
		String functionCall = "callq";
		assemblyBuilder(functionCall, method);
	}


	// Moving ints into registers		<--	movq	$10,%rdi
	private void assemblyBuilder(String functionCall, int i, String register) {
		String call = "\n\n\t" + functionCall + "\t" +"$" + i + "," + register;
		fileWrite(call);
	}


	// Builds and prints a function call with one register
	private void assemblyBuilder(String functionCall, String register) {
		String call = "\n\t" + functionCall + "\t" + register;
		fileWrite(call);
	}


	// Builds and prints a function call with two registers
	private void assemblyBuilder(String functionCall, String register1, String register2) {
		String call = "\n\t" + functionCall + "\t" + register1 + "," + register2;
		fileWrite(call);
	}


	/* Used to reduce number of sys.out.prints, takes a pre-built string from one of the
	 * 'assemblyBuilder' methods, and passes it into the System.out.println here
	 */ 
	private void fileWrite(String s) {
		try {
			writer.write(s);
		} catch (IOException e) {
			System.out.println("Unexpected error with file: " + e.toString());
			e.printStackTrace();
		}
	}


	// Prologue - Used to reduce code lines
	private void prologue() {
		String pushq = "\n\t" + "pushq"+ "\t" + "%rbp";
		String movq = "\n\t" + "movq" + "\t" + "%rsp,%rbp";
		try {
			writer.write(pushq);
			writer.write(movq);
		} catch (IOException e) {
			System.out.println("Unexpected file error: " + e.toString());
			e.printStackTrace();
		}
	}


	// Epilogue - Used to reduce code lines
	private void epilogue() {
		String movq = "\n\n\t" + "movq" + "\t" + "%rsp,%rbp";
		String popq = "\n\t" + "popq" + "\t" + "%rbp";
		String ret = "\n\t" + "ret";
		try {
			writer.write(movq);
			writer.write(popq);
			writer.write(ret);
			writer.close();
		} catch (IOException e) {
			System.out.println("Unexpected file error: " + e.toString());
			e.printStackTrace();
		}
	}
}
