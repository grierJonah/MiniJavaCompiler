package AST;
import AST.Visitor.PrettyPrintVisitor;
import codegen.CodeGeneration;
import symbolTable.ConstructSymTableVisitor;
import symbolTable.TypeCheckVisitor;

public abstract class Statement extends ASTNode {	
	public Statement(int ln) {
		super(ln);
	}
	public abstract void accept(PrettyPrintVisitor v);
	public abstract void accept(ConstructSymTableVisitor v);
	public abstract void accept(TypeCheckVisitor v);
	public abstract void accept(CodeGeneration v);
}
