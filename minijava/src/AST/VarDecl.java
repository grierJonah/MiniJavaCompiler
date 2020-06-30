package AST;
import AST.Visitor.PrettyPrintVisitor;
import codegen.CodeGeneration;
import symbolTable.ConstructSymTableVisitor;
import symbolTable.TypeCheckVisitor;

public class VarDecl extends ASTNode {
	public Type t;
	public Identifier i;

	public VarDecl(Type at, Identifier ai, int ln) {
		super(ln);
		t=at; i=ai;
		this.type = new IdentifierType(i.s, ln);
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
