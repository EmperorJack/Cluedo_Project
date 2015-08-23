package cluedo.tiles;

import java.awt.Color;
import java.awt.Graphics2D;

import cluedo.board.Location;

public class PathTile extends Tile {

	public PathTile(Location location) {
		super(location);
	}
	
	public void draw(Graphics2D g, int gridXoffset, int gridYoffset, int squareSize){
		g.setColor(new Color(0,0,0,125));
		g.fillRect(location.getX() * squareSize + gridXoffset,location.getY() * squareSize + gridYoffset, squareSize, squareSize);
	}

}
