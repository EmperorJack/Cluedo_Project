package cluedo.view;

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

	public InputDialog(String name) {
		super(name);
		complete = false;
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
}
