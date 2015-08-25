package cluedo.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import cluedo.board.Board;
import cluedo.control.Controller;

/**
 * A java canvas that allows drawing of the visual elements of the cluedo board.
 */
@SuppressWarnings("serial")
public class Canvas extends JPanel {

	private Board board;
	private static final String IMAGE_PATH = "/images/";

	/**
	 * Setup a new canvas.
	 * 
	 * @param board
	 *            The game board.
	 * @param controller
	 *            The mouse controller.
	 */
	public Canvas(Board board, Controller controller) {
		this.board = board;

		// setup mouse listeners for the controller
		addMouseMotionListener(controller);
		addMouseListener(controller);
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		// draw the background colour
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, getWidth(), getHeight());

		// draw the current game board state
		board.draw(g2d, getWidth(), getHeight());
	}

	/**
	 * Load an image from the file system using a given filename.
	 * 
	 * @param filename
	 *            The filename of the image file.
	 * @return The image if it was successfully loaded.
	 */
	public static Image loadImage(String filename) {
		// in a jar or expanded into individual files.
		java.net.URL imageURL = Canvas.class.getResource(IMAGE_PATH + filename);

		try {
			// attempt to load the image from the given URL
			Image img = ImageIO.read(imageURL);
			return img;
		} catch (IOException e) {
			// failed to load the given image from filename
			throw new RuntimeException("Unable to load image: " + filename);
		}
	}

}
