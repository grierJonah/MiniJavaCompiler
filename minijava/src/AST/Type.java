package AST;
import AST.Visitor.Visitor;
import codegen.CodeGeneration;
import symbolTable.ConstructSymTableVisitor;
import symbolTable.TypeCheckVisitor;

public abstract class Type extends ASTNode {
    public Type(int ln) {
        super(ln);
    }
    public abstract void accept(Visitor v);
    public abstract void accept(ConstructSymTableVisitor v);
	public abstract void accept(TypeCheckVisitor v);
	public abstract void accept(CodeGeneration v);
}
