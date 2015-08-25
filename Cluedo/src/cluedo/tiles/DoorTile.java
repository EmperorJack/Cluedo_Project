package cluedo.tiles;
import static cluedo.board.Board.gridXoffset;
import static cluedo.board.Board.gridYoffset;
import static cluedo.board.Board.squareSize;

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
	public void draw(Graphics2D g, Color color) {
		g.setColor(color);
		g.fillRect(location.getX() * squareSize + gridXoffset, location.getY()
				* squareSize + gridYoffset, squareSize, squareSize);	
	}

}
