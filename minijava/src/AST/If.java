package AST;
import AST.Visitor.PrettyPrintVisitor;
import AST.Visitor.Visitor;
import codegen.CodeGeneration;
import symbolTable.ConstructSymTableVisitor;
import symbolTable.TypeCheckVisitor;

public class If extends Statement {
	public Exp e;
	public Statement s1,s2;

	public If(Exp ae, Statement as1, Statement as2, int ln) {
		super(ln);
		e=ae; s1=as1; s2=as2;
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

