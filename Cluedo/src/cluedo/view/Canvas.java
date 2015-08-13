package cluedo.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import cluedo.board.Board;

@SuppressWarnings("serial")
public class Canvas extends JPanel {
	
	private Board board;
	
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
		// draw the background colour
		g.setColor(Color.BLACK);
		g.setColor(new Color((float) Math.random(), (float) Math.random(), (float) Math.random())); 
		g.fillRect(0, 0, getWidth(), getHeight());
		
		// TODO draw everything else
	}
}
