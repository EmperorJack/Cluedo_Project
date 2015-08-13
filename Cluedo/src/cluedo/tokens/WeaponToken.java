package cluedo.tokens;

import cluedo.board.Room;

public class WeaponToken extends Token {
	Room room;

	public WeaponToken(String name, Room room) {
		super(name);

	}

	public void leaveRoom() {
		if (room != null) {
			room.removeToken(this);
			this.room = null;
		}
	}

	public void setRoom(Room room) {
		room.addToken(this);
		this.room = room;
	}
	
	public Room getRoom(){
		return room;
	}

}
