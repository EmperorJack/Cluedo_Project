package cluedo.view;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import cluedo.board.Board;
import cluedo.tokens.CharacterToken;

/**
 * Dialog box that allows the current player to enter a name and select a
 * character from a list of available characters.
 */
@SuppressWarnings("serial")
public class PlayerInputDialog extends InputDialog {

	// dialog box fields
	private JTextField nameField;
	private ButtonGroup radioGroup;
	private String selectedCharacter;
	private CharacterToken currentToken;
	private JLabel imageLabel;

	/**
	 * Setup a new player setup dialog box.
	 * 
	 * @param characters
	 *            The available characters the player may choose from
	 * @param playerNumber
	 *            The ID number of the player
	 */
	public PlayerInputDialog(String[] characters,
			List<String> remainingCharacters, Board board, int playerNumber) {
		super("Player " + playerNumber + " Setup");
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// setup the option panel to hold input fields
		JPanel optionPanel = new JPanel();
		optionPanel.setLayout(new GridLayout(0, 1));

		// player name input area setup
		optionPanel.add(new JLabel("Player Name:"));
		nameField = new JTextField("", 10);
		optionPanel.add(nameField);

		// player character select area setup
		optionPanel.add(new JLabel("Available Characters:"));
		radioGroup = new ButtonGroup();
		boolean defaultSelected = false;

		// setup a radio button for each available character
		for (int i = 0; i < characters.length; i++) {
			JRadioButton button;
			button = new JRadioButton(characters[i], false);

			// grey out the character buttons that have already been selected
			if (!remainingCharacters.contains(characters[i])) {
				button.setEnabled(false);
			} else if (!defaultSelected) {
				// set the default selected button if it has not been chosen yet
				button.setSelected(true);
				selectedCharacter = characters[i];
				currentToken = board.getCharacterToken(characters[i]);
				defaultSelected = true;
			}

			// add the new radio button to the container and radio button group
			optionPanel.add(button);
			radioGroup.add(button);

			// create a listener for the new radio button
			button.addItemListener(new RadioButtonHandler(characters[i], board
					.getCharacterToken(characters[i])));
		}
		panel.add(optionPanel);

		// setup the portrait of the currently selected character
		JPanel portraitPanel = new JPanel(new FlowLayout());
		imageLabel = new JLabel(new ImageIcon(currentToken.getImage()));
		portraitPanel.add(imageLabel);
		panel.add(portraitPanel);

		// setup the OK button to confirm player selection
		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// the dialog box has finished getting player input
				complete = true;
			}
		});
		buttonPanel.add(okButton);
		panel.add(buttonPanel);

		// finish setting up the dialog box attributes
		panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		add(panel);
		getRootPane().setDefaultButton(okButton);
		pack();
		setSize(300, 520);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * An inner class that handles the events that occur when the user changes
	 * the selected radio button.
	 */
	private class RadioButtonHandler implements ItemListener {

		// character name and token associated with the radio button
		private String name;
		private CharacterToken token;

		public RadioButtonHandler(String name, CharacterToken token) {
			this.name = name;
			this.token = token;
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			// if the radio button was selected
			if (e.getStateChange() == ItemEvent.SELECTED) {
				// set the dialog box selected character to this character name
				selectedCharacter = name;

				// set the dialog box character portrait to this token image
				imageLabel.setIcon(new ImageIcon(token.getImage()));
			}
		}
	}

	// get methods below to return dialog box fields

	public String getNameInput() {
		return nameField.getText();
	}

	public String getSelectedCharacter() {
		return selectedCharacter;
	}
}
