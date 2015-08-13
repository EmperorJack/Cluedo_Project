package cluedo.tokens;

public abstract class Token {
	private String name;
	
	public Token (String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
