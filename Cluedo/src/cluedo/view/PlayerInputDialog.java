package cluedo.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * A custom window that acts as a dialog box with multiple inputs. Allows the
 * current player to enter a name and select a character from a list of
 * available characters.
 */
@SuppressWarnings("serial")
public class PlayerInputDialog extends JFrame {

	// dialog box fields
	private JTextField nameField;
	private ButtonGroup radioGroup;
	private String selectedCharacter;
	private boolean complete;

	/**
	 * Setup a new player setup dialog box.
	 * 
	 * @param characters
	 *            The available characters the player may choose from
	 * @param playerNumber
	 *            The ID number of the player
	 */
	public PlayerInputDialog(List<String> characters, int playerNumber) {
		super("Player " + playerNumber + " Setup");
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		complete = false;

		// player name input area setup
		panel.add(new JLabel("Player Name:"));
		nameField = new JTextField("", 10);
		panel.add(nameField);

		// player character select area setup
		panel.add(new JLabel("Available Characters:"));
		radioGroup = new ButtonGroup();

		// setup a radio button for each selectable character
		for (int i = 0; i < characters.size(); i++) {
			JRadioButton button;
			if (i == 0) {
				// set the first radio button to be the default selection
				button = new JRadioButton(characters.get(i), true);
				selectedCharacter = characters.get(i);
			} else {
				button = new JRadioButton(characters.get(i), false);
			}

			// add the new radio button to the container and radio button group
			panel.add(button);
			radioGroup.add(button);

			// create a listener for the new radio button
			button.addItemListener(new RadioButtonHandler(characters.get(i)));
		}

		// setup the OK button to confirm player selection
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// the dialog box has finished getting player input
				complete = true;
			}
		});
		panel.add(okButton);

		// finish setting up the dialog box attributes
		panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		add(panel);
		getRootPane().setDefaultButton(okButton);
		pack();
		setSize(300, 350);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * An innter class that handles the events that occur when the user changes
	 * the selected radio button.
	 */
	private class RadioButtonHandler implements ItemListener {

		// character name associated with the radio button
		private String name;

		public RadioButtonHandler(String name) {
			this.name = name;
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			// if the radio button was selected
			if (e.getStateChange() == ItemEvent.SELECTED) {
				// set the dialog box selected character to this character name
				selectedCharacter = name;
			}
		}
	}
	
	public void requestInput() {
		// while the dialog box input has not been completed
		while (!inputChosen()) {
			try {
				// wait a bit
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// a thread interrupted exception occurred
				e.printStackTrace();
			}
		}
	}

	// get methods below to return dialog box fields

	public boolean inputChosen() {
		return complete;
	}

	public String getNameInput() {
		return nameField.getText();
	}

	public String getSelectedCharacter() {
		return selectedCharacter;
	}
}
