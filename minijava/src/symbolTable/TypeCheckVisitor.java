package symbolTable;

import java.util.ArrayList;
import java.util.List;

import AST.*;
import symbolTable.components.Method;
import symbolTable.components.VariableObj;
import symbolTable.components.Class;

public class TypeCheckVisitor implements TypeVisitor {
	
	private SymbolTable globalTable;
	private Class currClass;
	private Method currMethod;
	private VariableObj currVariable;
	private List<String> errorList;
	
	public TypeCheckVisitor(SymbolTable symTable, List<String> errorList) {
		this.globalTable = symTable;
		this.currClass = null;
		this.errorList = errorList;
	}

	// Display added for toy example language.  Not used in regular MiniJava
	public Type visit(Display n) {
		n.e.accept(this);
		return n.type;
	}

	// MainClass m;
	// ClassDeclList cl;
	public Type visit(Program n) {
		n.m.accept(this);
		for(int i = 0; i < n.cl.size(); i++) {
			n.cl.get(i).accept(this);
		}
		return n.type;
	}

	
	// Identifier i1,i2;
	// Statement s;
	public Type visit(MainClass n) {
		String name = n.i1.s;
		n.s.accept(this);
		if (globalTable.getClass(name) != null) {
			currClass = globalTable.getClass(n.i1.s);
			return currClass.getType();
		} else {
			return null;
		}
	}

	
	// Identifier i;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Type visit(ClassDeclSimple n) {
		n.i.accept(this);
		String name = n.i.s;
		
		currClass = globalTable.getClass(name);

		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.get(i).accept(this);
		}

		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.get(i).accept(this);
		}
		return null;
	}	

	
	// Identifier i;
	// Identifier j;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Type visit(ClassDeclExtends n) {
		n.i.accept(this);
		String name = n.i.s;
		
		String parent = globalTable.getClass(name).getParent();
		
		// Class is present, and cannot extend itself
		if (globalTable.getClass(parent) != null &&
				globalTable.getClass(name).getParent() != name) {
			for (int i = 0; i < n.vl.size(); i++) {
				n.vl.get(i).accept(this);
			}
			for (int i = 0; i < n.ml.size(); i++) {
				n.ml.get(i).accept(this);
			}
			return null;
			//return globalTable.getClass(name).getType();
		}
		if (globalTable.getClass(parent).getParent() == name) {
			errorList.add("Error @" + n.j.line_number + ": Class cannot extend itself!");
		}
		errorList.add("Error @"+ n.line_number + ": Parent class doesn't exist!");
		return null;
	}

	
	// Type t;
	// Identifier i;
	public Type visit(VarDecl n) {
		n.t.accept(this);
		n.i.accept(this);
		return null;
	}

	
	// Type t;
	// Identifier i;
	// FormalList fl;
	// VarDeclList vl;
	// StatementList sl;
	// Exp e;
	public Type visit(MethodDecl n) {
		
		Method methodName = currClass.getMethod(n.i.s); 
		n.t.accept(this);
		n.i.accept(this);
		for ( int i = 0; i < n.fl.size(); i++ ) {
			n.fl.get(i).accept(this);
		}
		for ( int i = 0; i < n.vl.size(); i++ ) {
			n.vl.get(i).accept(this);
		}
		for ( int i = 0; i < n.sl.size(); i++ ) {
			n.sl.get(i).accept(this);
		}
		n.e.accept(this);
		return n.e.type;
	}

	// Type t;
	// Identifier i;
	public Type visit(Formal n) {
		n.t.accept(this);
		n.i.accept(this);
		return n.t;
	}


	public Type visit(IntArrayType n) {
		return new IntArrayType(n.line_number);
	}


	public Type visit(BooleanType n) {
		return new BooleanType(n.line_number);
	}


	public Type visit(IntegerType n) {
		return new IntegerType(n.line_number);
	}
	
	public Type visit(DoubleType n) {
		return new DoubleType(n.line_number);
	}

	
	// String s;
	public Type visit(IdentifierType n) {
		return new IdentifierType(n.s, n.line_number);
	}

	
	// StatementList sl;
	public Type visit(Block n) {
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.get(i).accept(this);
		}
		return null;
	}

	
	// Exp e;
	// Statement s1,s2;
	public Type visit(If n) {
		n.e.accept(this);
		n.s1.accept(this);
		n.s2.accept(this);
		return n.type;
	}

	
	// Exp e;
	// Statement s;
	public Type visit(While n) {
		n.e.accept(this);
		n.s.accept(this);
		return n.type;
	}

	
	// Exp e;
	public Type visit(Print n) {
		n.e.accept(this);
		return n.type;
	}

	
	// Identifier i;
	// Exp e;
	public Type visit(Assign n) {
		Type t1 = n.i.accept(this);
		Type t2 = n.e.accept(this);
		
		if (globalTable.checkTypes(t1, t2)) {
			return n.type;
		} else {
			errorList.add("Error @" + n.line_number + ": Cannot assign the types: "
					+ t1
					+ " and "
					+ t2);
			return null;
		}
	}

	
	// Identifier i;
	// Exp e1,e2;
	public Type visit(ArrayAssign n) {
		Type t1 = n.i.accept(this);
		Type t2 = n.e1.accept(this);
		Type t3 = n.e2.accept(this);
		
		if (t1 instanceof IntArrayType
				&& t2 instanceof IntegerType
				&& t3 instanceof IntegerType) {
			return n.type;
		} else {
			errorList.add("Error @" + n.i.line_number + ": Incompatible array assign types!");
			return null;
		}
	}

	
	// Exp e1,e2;
	public Type visit(And n) {
		Type t1 = n.e1.accept(this);
		Type t2 = n.e2.accept(this);
		if (globalTable.checkTypes(t1, t2)) {
			return t1;
		} else {
			errorList.add("Error @" + n.line_number + ": Types: "
					+ t1
					+ " and "
					+ t2
					+ " do not match.");
			return null;
		}
	}

	
	// Exp e1,e2;
	public Type visit(LessThan n) {
		Type t1 = n.e1.accept(this);
		Type t2 = n.e2.accept(this);
		if (globalTable.checkTypes(t1, t2)) {
			return t1;
		} else {
			errorList.add("Error @" + n.line_number + ": Types: "
					+ t1
					+ " and "
					+ t2
					+ " do not match");
			return null;
		}
	}

	
	// Exp e1,e2;
	public Type visit(Plus n) {
		Type t1 = n.e1.accept(this);
		Type t2 = n.e2.accept(this);
		
		if (!globalTable.checkTypes(t1, t2)) {
			errorList.add("Error @" + n.line_number + ": Cannot add "
					+ t1
					+ " and "
					+ t2);	
		} else if (!(t1 instanceof IntegerType) && !(t1 instanceof DoubleType)) {
			errorList.add("Error @" + n.line_number + ": Cannot add "
					+ t1
					+ " and "
					+ t2);
		}
		return n.e1.type;
	}

	// Exp e1,e2;
	public Type visit(Minus n) {
		Type t1 = n.e1.accept(this);
		Type t2 = n.e2.accept(this);
		
		if (!globalTable.checkTypes(t1, t2)) {
			errorList.add("Error @" + n.line_number + ": Cannot add "
					+ t1
					+ " and "
					+ t2);	
		}
		if (t1 instanceof BooleanType) {
			errorList.add("Error @" + n.line_number + ": Cannot add "
					+ t1
					+ " and "
					+ t2);
		}
		if (!(t1 instanceof IntegerType) && !(t1 instanceof DoubleType)) {
			errorList.add("Error @" + n.line_number + ": Cannot add "
					+ t1
					+ " and "
					+ t2);
		}
		return n.e1.type;
	}


	// Exp e1,e2;
	public Type visit(Times n) {
		Type t1 = n.e1.accept(this);
		Type t2 = n.e2.accept(this);
		
		if (!globalTable.checkTypes(t1, t2)) {
			errorList.add("Error @" + n.line_number + ": Cannot add "
					+ t1
					+ " and "
					+ t2);	
		}
		if (t1 instanceof BooleanType) {
			errorList.add("Error @" + n.line_number + ": Cannot add "
					+ t1
					+ " and "
					+ t2);
		}
		if (!(t1 instanceof IntegerType) && !(t1 instanceof DoubleType)) {
			errorList.add("Error @" + n.line_number + ": Cannot add "
					+ t1
					+ " and "
					+ t2);
		}
		return n.e1.type;
	}


	// Exp e1,e2;
	public Type visit(ArrayLookup n) {
		Type t1 = n.e1.accept(this);
		Type t2 = n.e2.accept(this);
		if (globalTable.checkTypes(t1, t2)) {
			return n.e1.type;
		} else {
			errorList.add("Error @" + n.line_number + " incompatible types. Must be of IntArrayType.");
			return null;
		}
	}


	// Exp e;
	public Type visit(ArrayLength n) {
		Type t1 = n.e.type;
		if (t1 instanceof IntArrayType) {
			return t1;
		} else {
			errorList.add("Error @" + n.e.line_number + ": Length can only be called on int[] type!");
			return null;
		}	
	}


	// Exp e;
	// Identifier i;
	// ExpList el;
	/**
	 * Function "call" gets a bit tricky since their are a couple extra moving parts to consider.
	 * 1) Check if the class even exists in the symbol table: B.call()  <-- Check B exists. (If it doesn't return null, error)
	 * 2) Check if the method exists in current class: B.call()  <-- Check call() function exists (If it doesn't check parent class)
	 * 
	 * Two helper functions are used to help for testing: checkCurrentClassForMethod & checkParentClassForMethod
	 */
	public Type visit(Call n) {			// object.method(type param_1, type param_2, type param_n)     <-- Expression . identifier ( parameters )
		n.e.accept(this);
		n.i.accept(this);
		
		// Check if class exists in global table
		currClass = globalTable.getClass(n.i.s);
		
		if (currClass != null) {
			
			currMethod = currClass.getMethod(n.i.s);
			
			if (currMethod != null) {
				
				// CurrClass exists! Call helper function
				checkCurrentClassForMethod(currMethod, n);
				
			} else {
				
				// Method was not found in original class, search through parent class instead
				String parentClass = globalTable.getClass(n.i.s).getParent();
				currClass = globalTable.getClass(parentClass);
				
				if (globalTable.getClass(parentClass).getMethod(n.i.s) != null) {
					currMethod = currClass.getMethod(n.i.s);
					if (checkParentClassForMethod(currMethod, n)) {
						return n.type;
					}
				}
			}
		}
		return null;
	}
	
	
	// int i;
	public Type visit(IntegerLiteral n) {
		return new IntegerType(n.line_number);
	}
	
	public Type visit(DoubleLiteral n) {
		return new DoubleType(n.line_number);
	}
	

	public Type visit(True n) {
		return new BooleanType(n.line_number);
	}

	
	@Override
	public Type visit(False n) {
		return new BooleanType(n.line_number);
	}

	
	// String s;
	public Type visit(IdentifierExp n) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Type visit(This n) {
		// TODO Auto-generated method stub
		return null;
	}

	
	//Exp e;
	public Type visit(NewArray n) {
		n.e.accept(this);
		return n.type;
	}

	
	// Identifier i;
	public Type visit(NewObject n) {
		n.i.accept(this);
		return n.type;
	}

	
	// Exp e;
	public Type visit(Not n) {
		Type t1 = n.e.accept(this);
		if (!globalTable.checkTypes(t1, n.type)) {
			errorList.add("Error @" + n.line_number + ": Cannot perform "
					+ n.type
					+ " on "
					+ t1);
		}
		return n.type;
	}
	

	//String s;
	public Type visit(Identifier n) {
		return n.type;
	}
	
	
	/**
	 * Large helper method for Call. checkCurrentClassMethod basically does 3 things:
	 * 1) It checks the current object N's parameter size (what the user passes into their call) and the currentMethods parameter size in the symbol table
	 * 2) It iterates through both the users and stored method's inputs, checking that the ordering and types are the same
	 * 3) It checks that the return type the user specified matches the return type stored in the symbol table
	 * 
	 * @param currentMethod the method the user types out
	 * @param n the method that is stored in the symbol table
	 * @return the final type of the function call or null
	 */
	private Boolean checkCurrentClassForMethod(Method currentMethod, Call n) {
		int inputParameterSize = n.el.size();
		int currMethodParameterSize = currentMethod.getParameters().size();

		// Check parameter input size match (user function call against stored method parameter size)
		if (inputParameterSize == currMethodParameterSize) {

			// Check ordering of input parameter and their matching types
			for (int i = 0; i < inputParameterSize; i++) {
				if (currentMethod.getParameters().get(i).getType() == n.el.get(i).type) {

					// Check method return type and return type of call
					if (currentMethod.getType() == n.type) {
						return true;
					} else {
						errorList.add("Error @"
								+ n.line_number
								+ ": Check return type argument. Types do not match!");
					}
				} else {
					errorList.add("Error @" 
							+ n.el.line_number 
							+ ": Check input parameters type and/or ordering!");
				}
			} 
		} else {
			errorList.add("Error @"
					+ n.el.line_number
					+ ": "
					+ currMethodParameterSize
					+ " arguments required for function call!");
		}
		return false;
	}
	
	/**
	 * Helper method for call.
	 * 
	 * @param currentMethod the method the user types out
	 * @param n the method that is stored in the symbol table
	 * @return the final type of the function call or null
	 */
	private Boolean checkParentClassForMethod(Method currentMethod, Call n) {
		return checkCurrentClassForMethod(currentMethod, n);		
	}
}
