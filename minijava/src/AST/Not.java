package AST;
import AST.Visitor.Visitor;
import codegen.CodeGeneration;
import symbolTable.ConstructSymTableVisitor;
import symbolTable.TypeCheckVisitor;

public class Not extends Exp {
	public Exp e;
	public Type type;

	public Not(Exp ae, int ln) {
		super(ln);
		e=ae; 
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
