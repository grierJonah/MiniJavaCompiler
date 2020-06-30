package AST;
import AST.Visitor.PrettyPrintVisitor;
import AST.Visitor.Visitor;
import codegen.CodeGeneration;
import symbolTable.ConstructSymTableVisitor;
import symbolTable.TypeCheckVisitor;

public class Identifier extends ASTNode {
	public String s;
	public Type type;

	public Identifier(String as, int ln) { 
		super(ln);
		s=as;
		this.type = new IdentifierType(as, ln);
	}
	
	public String toString(){
		return s;
	}
	
	public void accept(PrettyPrintVisitor v) {
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
