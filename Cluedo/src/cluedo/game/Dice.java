package cluedo.game;

import java.awt.Graphics2D;

public class Dice {

	private int value1;
	private int value2;

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

	}
}
