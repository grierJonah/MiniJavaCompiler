package AST;
import AST.Visitor.PrettyPrintVisitor;
import AST.Visitor.Visitor;
import codegen.CodeGeneration;
import symbolTable.ConstructSymTableVisitor;
import symbolTable.TypeCheckVisitor;

public class Display extends Statement {
	public Exp e;

	public Display(Exp re, int ln) {
		super(ln);
		e=re; 
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

