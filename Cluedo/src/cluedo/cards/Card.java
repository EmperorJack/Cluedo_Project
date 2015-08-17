package cluedo.cards;

import java.awt.Graphics;
import java.awt.Image;

import static cluedo.view.Canvas.loadImage;

/**
 * Represents a cluedo game card with a name.
 */
public abstract class Card {

	private String name;
	private Image image;

	/**
	 * Setup a new card with given name.
	 * 
	 * @param name
	 *            The given card name.
	 */
	public Card(String name, String fileName) {
		this.name = name;
		this.image = loadImage(fileName);
	}

	public Card(String name) {
		this.name = name;
		// TODO Remove this constructor once image files have been made
	}

	@Override
	public String toString() {
		return name;
	}


	public void draw(Graphics g) {
		g.drawImage(image, 0, 0, null);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
