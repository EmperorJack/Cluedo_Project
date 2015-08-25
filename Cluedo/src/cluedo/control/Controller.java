package cluedo.control;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import cluedo.board.Board;
import cluedo.game.Game;

/**
 * The controller class acts as an overlying class that listens for mouse input
 * and simply delegates the mouse events to the board and game classes.
 */
public class Controller implements MouseListener, MouseMotionListener {
	private Board board;
	private Game game;

	/**
	 * Setup a new controller that knows about the game and board.
	 * 
	 * @param board
	 *            The cluedo board.
	 * @param game
	 *            The cluedo game.
	 */
	public Controller(Board board, Game game) {
		this.game = game;
		this.board = board;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// make the board aware of the new mouse position
		board.updateMousePos(e.getX(), e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// make the game aware of a mouse release action
		game.triggerMove(e.getX(), e.getY());
	}

	// following methods are unused but required by the mouse interfaces

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
