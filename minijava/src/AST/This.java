package AST;
import AST.Visitor.Visitor;
import codegen.CodeGeneration;
import symbolTable.ConstructSymTableVisitor;
import symbolTable.TypeCheckVisitor;

public class This extends Exp {
	public This(int ln) {
		super(ln);
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
