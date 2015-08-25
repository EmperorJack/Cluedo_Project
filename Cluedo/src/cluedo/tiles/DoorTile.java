package cluedo.tiles;
import static cluedo.board.Board.GRID_X_OFFSET;
import static cluedo.board.Board.GRID_Y_OFFSET;
import static cluedo.board.Board.SQUARE_SIZE;

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
		g.fillRect(location.getX() * SQUARE_SIZE + GRID_X_OFFSET, location.getY()
				* SQUARE_SIZE + GRID_Y_OFFSET, SQUARE_SIZE,SQUARE_SIZE);	
	}

}
