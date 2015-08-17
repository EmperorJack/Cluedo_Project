package cluedo.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import cluedo.board.Board;

@SuppressWarnings("serial")
public class Canvas extends JPanel {

	private Board board;
	private static final String IMAGE_PATH = "images/";

	public Canvas(Board board) {
		this.board = board;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(600, 600);
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		// draw the background colour
		g2d.setColor(Color.BLACK);
		g2d.setColor(new Color((float) Math.random(), (float) Math.random(),
				(float) Math.random()));
		g2d.fillRect(0, 0, getWidth(), getHeight());

		// TODO game visible components here
		g.translate(0, 0); // YO KELLY WE CAN TRANSLATE THE CANVAS
		// YAAAAAAAS
		board.draw(g2d);
	}

	/**
	 * Load an image from the file system using a given filename.
	 * 
	 * @param filename
	 *            Name of the image file to load.
	 * @return The image if it was successfully loaded.
	 */
	public static Image loadImage(String filename) {
		// using the URL means the image loads when stored
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
