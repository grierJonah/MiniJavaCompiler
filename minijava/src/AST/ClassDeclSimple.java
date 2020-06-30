package AST;
import AST.Visitor.Visitor;
import codegen.CodeGeneration;
import symbolTable.ConstructSymTableVisitor;
import symbolTable.TypeCheckVisitor;

public class ClassDeclSimple extends ClassDecl {
	public Identifier i;
	public VarDeclList vl;  
	public MethodDeclList ml;

	public ClassDeclSimple(Identifier ai, VarDeclList avl, MethodDeclList aml, int ln) {
		super(ln);
		i=ai; vl=avl; ml=aml;
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
