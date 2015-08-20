package cluedo.tokens;

import java.awt.Image;

import cluedo.board.Room;

public class WeaponToken extends Token {
	Room room;

	public WeaponToken(String name, Room room, Image image) {
		super(name, image);
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
