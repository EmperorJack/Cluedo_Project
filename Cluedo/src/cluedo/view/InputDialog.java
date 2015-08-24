package cluedo.view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * A custom window that acts as a dialog box. It should be created with fields
 * that need to be filled in by the user. Once input is confirmed the boolean
 * complete should change and then the requestInput() method should return to
 * the caller. The caller can then access the inputed fields from this via
 * getter methods.
 */
@SuppressWarnings("serial")
public abstract class InputDialog extends JFrame {

	// once input is complete the dialog box will return to the caller
	boolean complete;

	// if the dialog was closed before input was confirmed
	boolean closed;

	public InputDialog(String name) {
		super(name);
		complete = false;

		// setup close operation
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				closed = true;
				complete = true;
			}
		});
	}

	/**
	 * Wait for the player to complete dialog input. When they hit the OK button
	 * the input will be confirmed and this will return to the caller.
	 */
	public void requestInput() {
		// while the dialog box input has not been completed
		while (!complete) {
			try {
				// wait a bit
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// a thread interrupted exception occurred
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns true / false depending on if the user closed the dialog before
	 * confirming their input.
	 * 
	 * @return If the dialog was closed.
	 */
	public boolean wasClosed() {
		return closed;
	}
}
