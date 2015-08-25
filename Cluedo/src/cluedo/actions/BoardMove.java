package cluedo.actions;

import cluedo.tokens.Token;

public interface BoardMove {
	public boolean isFinished();
	public void tick(Token t);
}
