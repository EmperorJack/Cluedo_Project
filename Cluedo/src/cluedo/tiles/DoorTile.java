package cluedo.tiles;
import java.awt.Color;
import java.awt.Graphics2D;

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

	@Override
	public void draw(Graphics2D g, int gridXoffset, int gridYoffset,
			int squareSize, Color color) {
		// TODO Auto-generated method stub
		
	}

}
