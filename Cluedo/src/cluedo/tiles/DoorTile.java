package cluedo.tiles;
import cluedo.board.*;

public class DoorTile extends Tile {
	Room room;
	public DoorTile(Location location, Room room) {
		super(location);
		this.room = room;
	}
	
	public Room getRoom(){
		return room;
	}

}
