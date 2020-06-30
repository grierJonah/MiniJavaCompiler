package AST;
import AST.Visitor.PrettyPrintVisitor;
import AST.Visitor.Visitor;
import codegen.CodeGeneration;
import symbolTable.ConstructSymTableVisitor;
import symbolTable.TypeCheckVisitor;

public class Assign extends Statement {
	public Identifier i;
	public Exp e;

	public Assign(Identifier ai, Exp ae, int ln) {
		super(ln);
		i=ai; e=ae; 
	}

	public void accept(Visitor v) {
		v.visit(this);
	}
	
	public void accept(PrettyPrintVisitor v) {
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

