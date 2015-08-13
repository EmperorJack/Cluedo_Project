package cluedo.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import cluedo.board.Board;

@SuppressWarnings("serial")
public class Frame extends JFrame {

	private Canvas canvas;

	public Frame(Board board) {
		super("Cluedo Game");
		
		// create canvas with center border layout
		canvas = new Canvas(board);
		setLayout(new BorderLayout());
		add(canvas, BorderLayout.CENTER);

		// default close operation
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// pack components and make visible
		pack();
		setResizable(true);
		setVisible(true);
	}
	
	public void update() {
		canvas.repaint();
	}
}
