package AST;
import AST.Visitor.PrettyPrintVisitor;
import AST.Visitor.Visitor;
import codegen.CodeGeneration;
import symbolTable.ConstructSymTableVisitor;
import symbolTable.TypeCheckVisitor;

public class ArrayAssign extends Statement {
	public Identifier i;
	public Exp e1,e2;
	public Type type;

	public ArrayAssign(Identifier ai, Exp ae1, Exp ae2, int ln) {
		super(ln);
		i=ai; e1=ae1; e2=ae2;
		this.type = new IntArrayType(ln);
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

	public void accept(PrettyPrintVisitor v) {
		v.visit(this);
		
	}

	public void accept(CodeGeneration v) {
		v.visit(this);
	}
}

