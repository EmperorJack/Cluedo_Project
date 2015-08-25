package cluedo.tiles;

import java.awt.Color;
import java.awt.Graphics2D;

import static cluedo.board.Board.GRID_X_OFFSET;
import static cluedo.board.Board.GRID_Y_OFFSET;
import static cluedo.board.Board.SQUARE_SIZE;
import cluedo.board.Location;

public class PathTile extends Tile {

	public PathTile(Location location){
		super(location);
	}

	public void draw(Graphics2D g, Color col) {
		g.setColor(col);
		g.fillRect(location.getX() * SQUARE_SIZE + GRID_X_OFFSET, location.getY()
				* SQUARE_SIZE + GRID_Y_OFFSET, SQUARE_SIZE,SQUARE_SIZE);	
	}

}
