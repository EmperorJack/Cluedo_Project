package cluedo.tiles;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import cluedo.board.*;

public class DoorTile extends Tile {
	Room room;
	List<RoomTile> roomTiles;
	public DoorTile(Location location, Room room) {
		super(location);
		this.room = room;
		roomTiles = new ArrayList<RoomTile>();
	}
	
	public Room getRoom(){
		return room;
	}
	
	public void addRoomTile(RoomTile roomTile){
		roomTiles.add(roomTile);
	}

	@Override
	public void draw(Graphics2D g, Color color) {
		// TODO Auto-generated method stub
		
	}

}
