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

@SuppressWarnings("serial")
public class Canvas extends JPanel implements MouseMotionListener, MouseListener {

	private Board board;
	private static final String IMAGE_PATH = "/images/";

	public Canvas(Board board) {
		this.board = board;
		addMouseMotionListener(this);
		addMouseListener(this);
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

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		board.updateMousePos(e.getX(), e.getY());
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		board.triggerMove(e.getX(), e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
