package cluedo.view;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cluedo.board.Room;
import cluedo.cards.*;
import cluedo.game.Player;

/**
 * Dialog box that allows the current player to select three cards for a
 * suggestion or accusation action.
 */
@SuppressWarnings("serial")
public class CardInputDialog extends InputDialog {

	// dialog box fields
	private JComboBox characterSelector;
	private JComboBox roomSelector;
	private JComboBox weaponSelector;
	private JLabel characterImageLabel;
	private JLabel roomImageLabel;
	private JLabel weaponImageLabel;
	private CharacterCard character;
	private RoomCard room;
	private WeaponCard weapon;
	private Object[] characters;
	private Object[] rooms;
	private Object[] weapons;

	/**
	 * Setup a new suggestion / accusation input dialog.
	 * 
	 * @param player
	 * @param playerRoom
	 */
	public CardInputDialog(Player player, Room playerRoom, String type) {
		super(type + " Input");
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// setup the option panel to hold input fields
		JPanel optionPanel = new JPanel();
		optionPanel.setLayout(new GridLayout(3, 3));

		// get lists of selectable cards
		characters = player.getNonRefutedCharacters().toArray();
		rooms = player.getNonRefutedRooms().toArray();
		weapons = player.getNonRefutedWeapons().toArray();

		// set default selected cards
		character = (CharacterCard) characters[0];
		room = (RoomCard) rooms[0];
		weapon = (WeaponCard) weapons[0];

		// setup the option panel with the combo boxes
		optionPanel.add(new JLabel("Character Card Select"));
		optionPanel.add(new JLabel("Room Card Select"));
		optionPanel.add(new JLabel("Weapon Card Select"));

		// setup the character card selector
		JPanel characterSelectorPanel = new JPanel(new FlowLayout());
		characterSelector = new JComboBox(characters);
		characterSelector.setSelectedIndex(0);
		characterSelector.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// set the dialog currently selected character to the new card
				character = (CharacterCard) characterSelector.getSelectedItem();

				// set the dialog character image label to the new card image
				characterImageLabel.setIcon(new ImageIcon(character.getImage()));
			}
		});
		characterSelectorPanel.add(characterSelector);
		optionPanel.add(characterSelectorPanel);

		// add the currently selected character card image
		characterImageLabel = new JLabel(new ImageIcon(character.getImage()));
		optionPanel.add(characterImageLabel);

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

		// finish setting up the dialog box attributes
		panel.add(optionPanel);
		panel.add(buttonPanel);
		panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		add(panel);
		getRootPane().setDefaultButton(okButton);
		pack();
		setSize(300, 350);
		setResizable(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	// get methods below to return dialog box fields

	public CharacterCard getCharacter() {
		return character;
	}

	public RoomCard getRoom() {
		return room;
	}

	public WeaponCard getWeapon() {
		return weapon;
	}
}
