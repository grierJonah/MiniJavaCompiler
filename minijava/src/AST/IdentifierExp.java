package AST;
import AST.Visitor.Visitor;
import codegen.CodeGeneration;
import symbolTable.ConstructSymTableVisitor;
import symbolTable.TypeCheckVisitor;

public class IdentifierExp extends Exp {
	public String s;
	public IdentifierExp(String as, int ln) { 
		super(ln);
		s=as;
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
