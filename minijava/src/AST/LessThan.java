package AST;
import AST.Visitor.Visitor;
import codegen.CodeGeneration;
import symbolTable.ConstructSymTableVisitor;
import symbolTable.TypeCheckVisitor;

public class LessThan extends Exp {
	public Exp e1,e2;
	public Type type;

	public LessThan(Exp ae1, Exp ae2, int ln) {
		super(ln);
		e1=ae1; e2=ae2;
		this.type = new BooleanType(ln);
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
