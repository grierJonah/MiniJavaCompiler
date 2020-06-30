package symbolTable.components;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import AST.Type;

/*
 * A basic Method class that represents a Method within a block of code. Each Method contains
 * its own specific ID (name of the method), a type (in MiniJava this is int, boolean, or int[]),
 * a List of parameters and a hashtable of variables. Parameters and Variables differ in that the 
 * Variables are a simple List of method signature 'VariableObjects', whereas the variables are 
 * variables that are created and used within the method itself. Variables are stored using a hashtable
 * to record their type and name. 
 */
public class Method {
	
	private String id;
	private Type type;
	private List<VariableObj> parameters;			   // method signature variables
	private Hashtable<Object, VariableObj> variables;  // variables inside the method
	
	public Method(String id, Type type) {  // method signature
		this.id = id;
		this.type = type;
		variables = new Hashtable<Object, VariableObj>();
		parameters = new ArrayList<VariableObj>();
	}
	
	/**
	 * Checks and adds a parameter to the list of parameters contained
	 * from the method signature. If the parameter ID is already found in
	 * the list, the method will return false, and will not add to the list.
	 * 
	 * @param id the name of the parameter
	 * @param type the type of the parameter
	 * @return true if parameter was submitted successfully 
	 * 		   or false if otherwises
	 */
	public boolean addParam(String id, Type type) {
		if (!parameters.contains(id)) {
			VariableObj newVar = new VariableObj(id, type);
			parameters.add(newVar);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Adds a variable to the hashtable of variable objects stored within a Method
	 * These variables are the ones found within the body of a method.
	 * 
	 * @param id the name of the method variable
	 * @param type the type of the method variable
	 * @return true or false depending on whether it was added or not
	 */
	public boolean addVariableObj(String id, Type type) {
		if (!variables.containsKey(id)) {
			VariableObj newVar = new VariableObj(id, type);
			variables.put(id, newVar);
			return true;
		} else  {
			return false;
		}
	}
	
	/**
	 * Gets a specific parameter by ID. If the ID is found in the List, then 
	 * the method will return a VariableObj. Otherwise, it will return null.
	 * These parameters are found in the method signature.
	 * 
	 * @param id the name of the parameter, represented as a String
	 * @return a VariableObj or null
	 */
	public VariableObj getParamByID(String id) {
		for (int i = 0; i < parameters.size(); i++) {
			if (parameters.contains(id)) {
				return parameters.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Gets a specific variable by ID. If the ID is found in the hashtable, then
	 * the method will return a VariableObj. Otherwise it will return null.
	 * These variables are found within the method body. 
	 * 
	 * @param id the name of the method variable
	 * @return a VariableObj or null
	 */
	public VariableObj getVariable(String id) {
		for (int i = 0; i < variables.size(); i++) {
			if (variables.containsKey(id)) {
				return (VariableObj) variables.get(id);
			}
		}
		return null;
	}
	
	/**
	 * Gets the list of parameters.
	 * 
	 * @return the list of parameters from the method signature
	 */
	public List<VariableObj> getParameters() {
		return this.parameters;
	}
	
	/**
	 * A getter for the name of the method.
	 * 
	 * @return the name of the method
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * A getter for the type.
	 * 
	 * @return the type of the method (int, boolean)
	 */
	public Type getType() {
		return this.type;
	}
}
