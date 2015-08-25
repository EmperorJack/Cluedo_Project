package cluedo.game;

import cluedo.board.Board;
import cluedo.view.Frame;


/**
 * The Clock Thread is responsible for producing a consistent "pulse" which is
 * used to update the game state, and refresh the display. Setting the pulse
 * rate too high may cause problems, when the point is reached at which the work
 * done to service a given pulse exceeds the time between pulses.
 * 
 * @author djp
 * 
 */
public class ClockThread extends Thread {
	private final int delay; // delay between pulses in us
	private final Board board;
	private final Frame display;
	
	public ClockThread(int delay, Board game, Frame display) {
		this.delay = delay;
		this.board = game;
		this.display = display;
	}
	
	public void run() {
		while(true) {
			// Loop forever			
			try {
				Thread.sleep(delay);
				board.tick();
				if(display != null) {
					// update the frame each tick to allow animations
					display.update();
				}
			} catch(InterruptedException e) {
				// should never happen
			}			
		}
	}
}
