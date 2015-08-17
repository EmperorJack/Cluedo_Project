package cluedo.tokens;

import java.awt.Graphics2D;
import java.awt.Image;

import static cluedo.view.Canvas.loadImage;

public abstract class Token {

	private String name;
	private Image image;

	public Token(String name, String fileName) {
		this.name = name;
		this.image = loadImage(fileName);
	}
	
	public Token(String name) {
		this.name = name;
		// TODO Remove this constructor once image files have been made
	}

	public String getName() {
		return name;
	}

	public void draw(Graphics2D g2d) {
		g2d.drawImage(image, 0, 0, null);
	}
}
