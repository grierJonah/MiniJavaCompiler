package AST;
import AST.Visitor.PrettyPrintVisitor;
import AST.Visitor.Visitor;
import codegen.CodeGeneration;
import symbolTable.ConstructSymTableVisitor;
import symbolTable.TypeCheckVisitor;

public class Block extends Statement {
	public StatementList sl;

	public Block(StatementList asl, int ln) {
		super(ln);
		sl=asl;
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

