package symbolTable.components;

import AST.Type;

public class VariableObj {

	private String id;
	private Type type;
	
	/**
	 * Constructs a new Variable object with an id and type.
	 * 
	 * @param id the name of the variable
	 * @param type the type of the variable
	 */
	public VariableObj(String id, Type type) {
		this.id = id;
		this.type = type;
	}

	/**
	 * Gets the ID of the variable object.
	 * 
	 * @return the ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the type of the variable object.
	 *  
	 * @return the type
	 */
	public Type getType() {
		return type;
	}	
}
