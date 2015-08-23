package cluedo.tokens;

import java.awt.Graphics2D;
import java.awt.Image;

import static cluedo.view.Canvas.loadImage;

public abstract class Token {

	private String name;
	private Image portrait;
	private Image image;

	public Token(String name) {
		this.name = name;
		this.portrait = loadImage("cards/" + name + ".jpg");
		this.image = loadImage("tokens/" + name + ".png");
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
