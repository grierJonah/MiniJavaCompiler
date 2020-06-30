package symbolTable;

import java.util.Hashtable;

import AST.BooleanType;
import AST.DoubleType;
import AST.IdentifierType;
import AST.IntArrayType;
import AST.IntegerType;
import AST.MainClass;
import AST.Program;
import AST.Type;
import symbolTable.components.Class;
import symbolTable.components.Method;

/**
 * A class for a Symbol Table. Symbol Tables are created to help keep track of 
 * semantics of variables. It stores information about scope, binding information
 * about names, information about instances of various entities such as function names
 * classes, and objects. Storing information like this in a table makes it easier for 
 * the compiler to check object instances to each other for type checking and semantic
 * analysis purposes. 
 * 
 * ************* Implemented some of this code from an educational website: *************
 * ********* http://alumni.cs.ucr.edu/~vladimir/cs152/A5/SymbolTable.java *********
 * 
 * @author jonahgrier
 *
 */
public class SymbolTable {
	
	public Hashtable<String, Object> symTable;
	
	/**
	 * Constructor for SymbolTable.
	 */
	public SymbolTable() {
		symTable = new Hashtable<String, Object>();
	}
	
	/**
	 * Adds a regular class that has no parent to the symbol table given an
	 * ID, and line number.
	 *	  
	 * @param id the name of the class
	 * @param parent the name of the parent class
	 * @param ln the line number of the class
	 * @return a true or false depending on status
	 */
	public boolean addClass(String id, String parent, int ln) {
		if (!symTable.contains(id)) {
			Class newClass = new Class(id, parent, ln);
			symTable.put(id, newClass);
			return true;
		} else {
			System.out.println("Unable to add Class to SymbolTable." +
					" Class already found in symbol table");
			return false;
		}
	}
	
	/**
	 * Gets a specific class by ID.
	 * 
	 * @param id the name of the class
	 * @return a Class object or null, depending on the status
	 */
	public Class getClass(String id) {
		if (symTable.containsKey(id)) {
			return (Class) symTable.get(id);
		}
		return null;
	}
	
	/**
	 * GetMethodFromSymbolTable does a couple things here. First, it checks to make sure that the class 
	 * being searched is available. If it is not, then it returns a system error and prints 
	 * to the console. If the className is found, and there is a matching class found in the 
	 * hashtable, then perform 3 steps to try to find available methods matching the ID 
	 * passed in. While the class is not equal to null, we iterate through all the class methods
	 * trying to match our ID to an ID stored. If one is found, it returns the method. 
	 * If there are no methods inside the class that matches the ID we are searching for we 
	 * check what the current class parent is. If the parent is null, then we set our 
	 * class to null and break from the while loop. If this is not the case, we set our 
	 * class to be the parent class and re-iterate through the parent classes methods repeating 
	 * the process over again.
	 * 
	 * @param id the name of the method to search for
	 * @param className the name of the class containing the searched for method
	 * @return null or the found method
	 */
	public Method getMethodFromSymbolTable(String id, String className) {
		if (getClass(className) == null) {
			System.out.println("Class " + className + " is not found");
			System.exit(1);
		}
		Class cls = getClass(className);
		while (cls != null) {
			if(cls.getMethod(id) != null) {
				return cls.getMethod(id);
			} else {
				if (cls.getParent() == "") {
					cls = null;
				} else {
					cls = getClass(cls.getParent());
				}
			}
		}
		System.out.println("Method " + id + " is not found in class " + className);
		return null;
	}
	
	
	/**
	 * There are four checks required for type checking. First we check to see 
	 * if the two types are of type Boolean, Integer, or IntArray type.
	 * If they are, then we return true. If the two types are of IdentifierType
	 * there is a bit more work involved due to parent classes. First, we cast two
	 * new variables called id1 and id2 to be IdentifierType variables used for checking.
	 * We then check where these new identifiers originally came from. By checking parent
	 * classes, we can figure out if they are of the same type (object type). We go through
	 * a recursive like structured while loop checking parent classes. If the parent class
	 * is none, then we return false.
	 * 
	 * @param t1 first type object to check
	 * @param t2 second type object to check
	 * @return true or false
	 */
	public boolean checkTypes(Type t1, Type t2) {
		if (t1 == null || t2 == null)
			return false;
		
		if (t1 instanceof BooleanType && t2 instanceof BooleanType)
			return true;
		if (t1 instanceof IntegerType && t2 instanceof IntegerType)
			return true;
		if (t1 instanceof DoubleType && t2 instanceof DoubleType)
			return true;
		if (t1 instanceof IntArrayType && t2 instanceof IntArrayType)
			return true;
		if (t1 instanceof IdentifierType && t2 instanceof IntegerType) {
			return true;
		}
		if (t1 instanceof IdentifierType && t2 instanceof IdentifierType) {
			IdentifierType id1 = (IdentifierType)t1;
			IdentifierType id2 = (IdentifierType)t2;
			
			Class cls = getClass(id1.s);
			while (cls != null) {
				if (id1.s.equals(id2.s)) {
					return true;
				} else {
					if (cls.getParent() == "") {
						return false;
					} else {
						cls = getClass(cls.getParent());
					}
				}
			}
		}
		return false;
	}
}
