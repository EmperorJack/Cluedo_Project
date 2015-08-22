package cluedo.tokens;

import cluedo.board.Location;
import cluedo.board.Room;

public class CharacterToken extends Token {

	char characterLetter;
	Location location;
	Room room;
	boolean inRoom;

	public CharacterToken(String name, char characterLetter, int x, int y) {
		super(name);
		this.characterLetter = characterLetter;
		this.location = new Location(x, y);
		this.room = null;
		inRoom = false;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location loc) {
		this.location = loc;
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

	public Room getRoom() {
		return room;
	}

	public boolean inRoom() {
		return inRoom;
	}
}
