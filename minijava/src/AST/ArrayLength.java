package AST;
import AST.Visitor.Visitor;
import codegen.CodeGeneration;
import symbolTable.ConstructSymTableVisitor;
import symbolTable.TypeCheckVisitor;

public class ArrayLength extends Exp {
	public Exp e;

	public ArrayLength(Exp ae, int ln) {
		super(ln);
		e=ae; 
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
