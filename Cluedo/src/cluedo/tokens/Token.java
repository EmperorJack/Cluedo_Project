package cluedo.tokens;

import java.awt.Graphics2D;
import java.awt.Image;

import cluedo.board.Location;
import cluedo.board.Room;
import static cluedo.view.Canvas.loadImage;

public abstract class Token {

	private String name;
	private Image portrait;
	private Image image;
	Location location;
	Room room;
	int xPos;
	int yPos;

	public Token(String name) {
		this.name = name;

		// load the associated portrait of this token (for GUI display)
		this.portrait = loadImage("cards/" + name + ".jpg");

		// load the associated image of this token (for board display)
		this.image = loadImage("tokens/" + name + ".png");
	}
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location loc) {
		this.location = loc;
	}
	

	public void setX(int x) {
		xPos = x;
	}
	
	public void setY(int y) {
		yPos = y;
	}

	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}

	public String getName() {
		return name;
	}

	public void draw(Graphics2D g2d) {
		g2d.drawImage(image, 0, 0, null);
	}

	public Image getPortrait() {
		return portrait;
	}
}
