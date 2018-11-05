package it.enhancer.enhancer;

public class LogCat {
	private String operation;
	private String property;
	public String action;
	
	public LogCat(String operation, String identifier, String action) {
		super();
		this.operation = operation;
		this.property = identifier;
		this.action = action;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	
	
}
