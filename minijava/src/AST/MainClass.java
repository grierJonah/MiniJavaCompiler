package AST;

import AST.Visitor.Visitor;
import codegen.CodeGeneration;
import symbolTable.ConstructSymTableVisitor;
import symbolTable.TypeCheckVisitor;

public class MainClass extends ASTNode{
	public Identifier i1,i2;
	public Statement s;

	public MainClass(Identifier ai1, Identifier ai2, Statement as, int ln) {
		super(ln);
		i1=ai1; i2=ai2; s=as;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public void accept(ConstructSymTableVisitor v) {
		v.visit(this);
	}

	public Type accept(TypeCheckVisitor v) {
		return v.visit(this);
	}

	public void accept(CodeGeneration v) {
		v.visit(this);
	}
}

