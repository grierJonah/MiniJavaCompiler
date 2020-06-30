package AST;
import AST.Visitor.Visitor;
import codegen.CodeGeneration;
import symbolTable.ConstructSymTableVisitor;
import symbolTable.TypeCheckVisitor;

public class Formal extends ASTNode{
	public Type t;
	public Identifier i;

	public Formal(Type at, Identifier ai, int ln) {
		super(ln);
		t=at; i=ai;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public void accept(ConstructSymTableVisitor v) {
		v.visit(this);
	}

	public void accept(TypeCheckVisitor v) {
		v.visit(this);
	}
	
	public void accept(CodeGeneration v) {
		v.visit(this);
	}
}
