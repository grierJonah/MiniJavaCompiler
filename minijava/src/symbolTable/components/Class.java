package symbolTable.components;

import java.util.Hashtable;

import AST.IdentifierType;
import AST.Type;

public class Class {
	
	private String id;
	private String parent;
	private Type type;
	private int line_number;
	private Hashtable<String, Method> methods;
	private Hashtable<Object, VariableObj> variables;
	
	/**
	 * Constructing a Class object with an ID, Parent, and line number.
	 * 
	 * @param id the string name of the class
	 * @param p the string parent of the class
	 * @param ln the linenumber of the class
	 */
	public Class(String id, String p, int ln) {
		this.id = id;
		this.parent = p;
		this.line_number = ln;
		type = new IdentifierType(id, ln);
		methods = new Hashtable<String, Method>();
		variables = new Hashtable<Object, VariableObj>();
	}
	
	/**
	 * Constructs an empty class.
	 */
	public Class() {	}
	
	
	/**
	 * Adds a variable to the class hashtable. Keeps track of the name of the variable and
	 * the type of variable object it is (i.e. int, boolean, int[]).
	 * 
	 * @param id the name of the variable object
	 * @param type the type of variable object
	 * @return true or false
	 */
	public boolean addVariableObj(String id, Type type) {
		if (!variables.containsKey(id)) {
			VariableObj newVar = new VariableObj(id, type);
			variables.put(id, newVar);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Gets a specific variable object if the variable is contained within the 
	 * global hashmap. If it is not contained in the hashmap, returns null.
	 * 
	 * @param id the name of the variable
	 * @return the variable object or null
	 */
	public VariableObj getVariable(String id) {
		if (variables.containsKey(id)) {
			return (VariableObj) variables.get(id);
		} else {
			return null;
		}
	}
	
	/**
	 * Adds a method object to the Class hashtable. ID is the name of the method
	 * and method is the 'method' object. 
	 * 
	 * @param id the name of the method, represented as a String
	 * @param method the object, represented as a Method object
	 * @return true or false
	 */
	public boolean addMethod(String id, Type type) {
		if (methods.containsKey(id)) {
			System.out.println("Unable to add method " + id + ". ID already exists");
			return false;
		}
		Method newMethod = new Method(id, type);
		methods.put(id, newMethod);
		return true;
	}
	
	/**
	 * Gets a single method from the class if the methods hashtable contains 
	 * a match of the ID inputted.
	 *  
	 * @param id the name of the method
	 * @return a Method object
	 */
	public Method getMethod(String id) {
		if (methods.containsKey(id)) {
			return methods.get(id);
		}
		return null;
	}
	
	/**
	 * Gets the methods of a class.
	 * 
	 * @return a hashtable of methods
	 */
	public Hashtable<String, Method> getMethods() {
		return this.methods;
	}
	
	/**
	 * Gets the variables for the class.
	 * 
	 * @return a hashtable of variables
	 */
	public Hashtable<Object, VariableObj> getVariables() {
		return this.variables;
	}
	
	/**
	 * Gets the ID (name) of the class.
	 * 
	 * @return the ID
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * Gets the type of the class.
	 * 
	 * @return the type, represented as a Type object
	 */
	public Type getType() {
		return this.type;
	}
	
	/**
	 * Gets the parent of the class.
	 * 
	 * @return the parent of the class, represented as a String
	 */
	public String getParent() {
		return this.parent;
	}
	
	/**
	 * Gets the line number of the class.
	 * 
	 * @return the line number, represented as an int
	 */
	public int getLineNumber() {
		return this.line_number;
	}
}