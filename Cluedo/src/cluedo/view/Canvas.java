package cluedo.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import cluedo.board.Board;
import cluedo.control.Controller;
import cluedo.game.Game;

@SuppressWarnings("serial")
public class Canvas extends JPanel{

	private Board board;
	private Game game;
	private static final String IMAGE_PATH = "/images/";

	public Canvas(Board board, Controller controller) {
		this.board = board;
		addMouseMotionListener(controller);
		addMouseListener(controller);
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
		g2d.fillRect(0, 0, getWidth(), getHeight());

		// TODO draw game visible components here
		board.draw(g2d, getWidth(), getHeight());
	}

	/**
	 * Load an image from the file system using a given filename.
	 * 
	 * @param filename
	 * @return The image if it was successfully loaded.
	 */
		// using the URL means the image loads when stored
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
