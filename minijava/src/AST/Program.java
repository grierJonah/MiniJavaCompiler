package AST;
import AST.Visitor.Visitor;
import codegen.CodeGeneration;
import symbolTable.ConstructSymTableVisitor;
import symbolTable.SymbolTable;
import symbolTable.TypeCheckVisitor;

public class Program extends ASTNode {
	public MainClass m;
	public ClassDeclList cl;

	public Program(MainClass am, ClassDeclList acl, int ln) {
		super(ln);
		m=am; cl=acl; 
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
	
	public void accept(CodeGeneration v) {
		v.visit(this);
	}
}
