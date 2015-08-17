package cluedo.view;

import java.awt.Container;
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
import javax.swing.JRadioButton;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class CharacterInputDialog extends JFrame {

	private JTextField nameField;
	private ButtonGroup radioGroup;
	private String selectedCharacter;
	private boolean complete;

	public CharacterInputDialog(List<String> characters, int playerNumber) {
		super("Player " + playerNumber + " Setup");
		Container c = getContentPane();
		c.setLayout(new GridLayout(0, 1));
		complete = false;

		// player name input area setup
		c.add(new JLabel("Player Name:"));
		nameField = new JTextField("", 10);
		c.add(nameField);

		// player character select area setup
		c.add(new JLabel("Available Characters:"));
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
			c.add(button);
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
		c.add(okButton);

		// setup the dialog box size
		setSize(300, 350);
		setResizable(false);
		getRootPane().setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private class RadioButtonHandler implements ItemListener {
		private String name;

		public RadioButtonHandler(String name) {
			this.name = name;
		}

		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				selectedCharacter = name;
			}
		}
	}

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
