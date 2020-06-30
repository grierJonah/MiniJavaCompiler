package AST;
import AST.Visitor.Visitor;
import codegen.CodeGeneration;
import symbolTable.ConstructSymTableVisitor;
import symbolTable.TypeCheckVisitor;

public abstract class Exp extends ASTNode {
    public Exp(int ln) {
        super(ln);
    }
    public abstract void accept(Visitor v);
    public abstract void accept(ConstructSymTableVisitor v);
    public abstract Type accept(TypeCheckVisitor v);
	public abstract void accept(CodeGeneration v);
}
