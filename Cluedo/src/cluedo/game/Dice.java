package cluedo.game;

import java.awt.Graphics2D;
import java.awt.Image;

import static cluedo.view.Canvas.loadImage;


public class Dice {

	private int value1;
	private int value2;
	private Image face1;
	private Image face2;

	/**
	 * Setup the class
	 */
	public Dice() {
		value1 = 0;
		value2 = 0;
	}

	/**
	 * Roll each dice separately for correct distribution.
	 */
	public void roll() {
		value1 = (int) (Math.random() * 6 + 1);
		value2 = (int) (Math.random() * 6 + 1);
		face1 = loadImage("dice/"+value1+".jpg");
		face2 = loadImage("dice/"+value2+".jpg");
	}

	/**
	 * Returns the current value of the dice added together.
	 * 
	 * @return The result from rolling two separate dice and adding them
	 *         together.
	 */
	public int getResult() {
		return value1 + value2;
	}

	/**
	 * Draw the dice with the given graphics object.
	 * 
	 * @param g2d
	 *            The graphics canvas to draw on.
	 */
	public void draw(Graphics2D g2d) {
		if (face1 != null && face2 != null){
			g2d.drawImage(face1,0,0,100,100,null);
			g2d.drawImage(face2,0,100,100,100, null);
		}

	}
	
	/**
	 * Reset the dice to 0 values.
	 */
	public void resetValues() {
		value1 = 0;
		value2 = 0;
	}
}
