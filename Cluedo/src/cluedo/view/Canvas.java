package cluedo.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import cluedo.board.Board;

@SuppressWarnings("serial")
public class Canvas extends JPanel{

	private Board board;
	private static final String IMAGE_PATH = "images/";
	private Image boardImage;

	public Canvas(Board board) {
		this.board = board;
		this.boardImage = loadImage("board.jpg");
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
		int lockAr = Math.min(getWidth(), getHeight());
		double proportion = boardImage.getWidth(this)/boardImage.getHeight(this);

		if (getWidth() < getHeight()){
		g.drawImage(boardImage, 0, (getHeight() - lockAr)/2, (int)(lockAr*proportion), lockAr, this);
		} else {
		g.drawImage(boardImage, (getWidth() - lockAr)/2, 0, (int)(lockAr*proportion), lockAr, this);

		}
		board.draw(g);
		// TODO game visible components here
		g.translate(0, 0); // YO KELLY WE CAN TRANSLATE THE CANVAS
		//YAAAAAAAS
	}

	/**
	 * Load an image from the file system using a given filename.
	 * 
	 * @param filename
	 * @return The image if it was successfully loaded.
	 */
	public static Image loadImage(String filename) {
		// using the URL means the image loads when stored
		// in a jar or expanded into individual files.
		java.net.URL imageURL = Canvas.class.getResource("/" + IMAGE_PATH + filename);

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
