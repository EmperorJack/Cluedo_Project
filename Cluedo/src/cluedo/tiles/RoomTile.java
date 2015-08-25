package cluedo.tiles;

import java.awt.Color;
import java.awt.Graphics2D;

import cluedo.board.Location;
import cluedo.board.Room;

public class RoomTile extends Tile{
	Room room;
	public RoomTile(Location location, Room room) {
		super(location);
		this.room = room;
	}

	@Override
	public void draw(Graphics2D g, Color color) {
		
	}

}
