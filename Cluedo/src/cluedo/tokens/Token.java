package cluedo.tokens;

import java.awt.Graphics2D;
import java.awt.Image;

import static cluedo.view.Canvas.loadImage;

public abstract class Token {

	private String name;
	private Image image;

	public Token(String name) {
		this.name = name;
		this.image = loadImage(name + ".jpg");
	}

	public String getName() {
		return name;
	}

	public void draw(Graphics2D g2d) {
		g2d.drawImage(image, 0, 0, null);
	}
	
	public Image getImage() {
		return image;
	}
}
