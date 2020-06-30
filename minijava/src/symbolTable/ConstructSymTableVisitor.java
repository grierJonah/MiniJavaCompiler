package symbolTable;

import java.util.Hashtable;

import AST.*;
import AST.Visitor.Visitor;
import symbolTable.components.Class;
import symbolTable.components.Method;
import symbolTable.components.VariableObj;

public class ConstructSymTableVisitor implements Visitor {

	private SymbolTable globalTable;

	private Class currClass;
	private Method currMethod;

	public ConstructSymTableVisitor() {
		this.globalTable = new SymbolTable();
		this.currClass = null;
		this.currMethod = null;
	}


	// Returns SymbolTable
	public SymbolTable getSymbolTable() {
		return this.globalTable;
	}


	// Display added for toy example language.  Not used in regular MiniJava
	public void visit(Display n) {
		n.e.accept(this);
	}


	// MainClass m;
	// ClassDeclList cl;
	public void visit(Program n) {
		n.m.accept(this);
		for (int i = 0; i < n.cl.size(); i++) {
			n.cl.get(i).accept(this);
		}
	}


	// Identifier i1,i2;
	// Statement s;
	public void visit(MainClass n) {
		String name = n.i1.s;
		n.i1.accept(this);
		n.i2.accept(this);
		
		if (!globalTable.addClass(name, null, n.line_number)) {
			System.err.println("main class: " + name + " has already been defined!");
		}
		
		currClass = globalTable.getClass(name);
		
		n.s.accept(this);
	}


	// Identifier i;
	// VarDeclList vl;
	// MethodDeclList ml;
	public void visit(ClassDeclSimple n) {	
		n.i.accept(this);
		String name = n.i.s;

		if (!globalTable.addClass(name, null, n.line_number)) {
			System.err.println("Simple class: " + name + " is already created!");
		}
		
		currClass = globalTable.getClass(name);

		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.get(i).accept(this);
		}

		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.get(i).accept(this);
		}
		
	}


	// Identifier i;
	// Identifier j;
	// VarDeclList vl;
	// MethodDeclList ml;
	public void visit(ClassDeclExtends n) {
		n.i.accept(this);
		n.j.accept(this);

		String name = n.i.s;
		String parent = globalTable.getClass(name).getParent();

		if(!globalTable.addClass(name, parent, n.line_number)) {
			System.err.println("Extended class: " + name + " is already defined!");
		}

		currClass = globalTable.getClass(name);

		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.get(i).accept(this);
		}

		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.get(i).accept(this);
		}
	}


	// Type t;
	// Identifier i;
	public void visit(VarDecl n) {
		n.t.accept(this);
		n.i.accept(this);
		String name = n.i.s;
		Type type = n.t;
		
		if (currMethod == null) {
			currClass.addVariableObj(name, type);
		} else {
			currMethod.addVariableObj(name, type);
		}
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
		
		if (!currClass.addMethod(n.i.s, n.t)) {
			System.out.println("Method already found in current class");
		}
		currMethod = currClass.getMethod(n.i.s);

		for ( int i = 0; i < n.fl.size(); i++ ) {
			n.fl.get(i).accept(this); 
		}
		for ( int i = 0; i < n.vl.size(); i++ ) {
			n.vl.get(i).accept(this);
		}
		for ( int i = 0; i < n.sl.size(); i++ ) {
			n.sl.get(i).accept(this);
		}
		n.e.accept(this);
	}


	// Type t;
	// Identifier i;
	public void visit(Formal n) {
		n.t.accept(this);
		n.i.accept(this);
		currMethod.addParam(n.i.s, n.t);
	}


	public void visit(IntArrayType n) {
	}


	public void visit(BooleanType n) {
	}


	public void visit(IntegerType n) {
	}


	// String s;
	public void visit(IdentifierType n) {
	}


	// StatementList sl;
	public void visit(Block n) {
		for (int i = 0; i < n.sl.size(); i++) {
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
		n.e1.accept(this);
		n.e2.accept(this);
	}


	// Exp e1,e2;
	public void visit(Minus n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}


	// Exp e1,e2;
	public void visit(Times n) {
		n.e1.accept(this);
		n.e2.accept(this);
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
		for (int i = 0; i < n.el.size(); i++) {
			n.el.get(i).accept(this);
		}
	}

	// int i;
	public void visit(IntegerLiteral n) {
	}


	public void visit(True n) {
	}


	public void visit(False n) {
	}


	// String s;
	public void visit(IdentifierExp n) {
	}


	public void visit(This n) {
	}


	// String s;
	public void visit(Identifier n) {
	}

	// Exp e;
	public void visit(NewArray n) {
		n.e.accept(this);
	}


	// Identifier i;
	public void visit(NewObject n) {
		n.i.accept(this);
	}


	// Exp e;
	public void visit(Not n) {
		n.e.accept(this);
	}


	@Override
	public void visit(DoubleType doubleType) {
		// TODO Auto-generated method stub
	}


	@Override
	public void visit(DoubleLiteral doubleLiteral) {
		// TODO Auto-generated method stub
	}
}