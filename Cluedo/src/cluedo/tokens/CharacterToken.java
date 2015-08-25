package cluedo.tokens;

import cluedo.board.Location;
import cluedo.board.Room;

public class CharacterToken extends Token {

	char characterLetter;
	boolean inRoom;
	boolean moving;
	

	public CharacterToken(String name, char characterLetter, int x, int y) {
		super(name);
		this.characterLetter = characterLetter;
		this.location = new Location(x, y);
		this.room = null;
		inRoom = false;
	}


	public char getChar() {
		return characterLetter;
	}
	public void setRoom(Room room) {
		room.addToken(this);
		this.room = room;
		inRoom = true;
	}

	public void leaveRoom() {
		if (room != null) {
			room.removeToken(this);
			this.room = null;
			inRoom = false;
		}
	}


	public boolean inRoom() {
		return inRoom;
	}
	
	public void updateX(int deltaX){
		xPos += deltaX;
	}
	
	public void updateY(int deltaY){
		yPos += deltaY;
	}
}
