package cluedo.tiles;

import java.awt.Color;
import java.awt.Graphics2D;

import static cluedo.board.Board.squareSize;
import static cluedo.board.Board.gridXoffset;
import static cluedo.board.Board.gridYoffset;

import cluedo.board.Location;

public class PathTile extends Tile {

	public PathTile(Location location){
		super(location);
	}

	public void draw(Graphics2D g, Color col) {
		g.setColor(col);
		g.fillRect(location.getX() * squareSize + gridXoffset, location.getY()
				* squareSize + gridYoffset, squareSize, squareSize);
	}

}
