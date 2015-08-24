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

import cluedo.board.Room;
import cluedo.cards.*;
import cluedo.game.Deck;
import cluedo.game.Player;

/**
 * Dialog box that allows the current player to select three cards for a
 * suggestion or accusation action.
 */
@SuppressWarnings("serial")
public class CardInputDialog extends InputDialog {

	// dialog box fields
	private ButtonGroup characterRadioGroup;
	private ButtonGroup roomRadioGroup;
	private ButtonGroup weaponRadioGroup;
	private JLabel characterImageLabel;
	private JLabel roomImageLabel;
	private JLabel weaponImageLabel;
	private CharacterCard character;
	private RoomCard room;
	private WeaponCard weapon;
	private List<CharacterCard> characters;
	private List<RoomCard> rooms;
	private List<WeaponCard> weapons;

	/**
	 * Setup a new suggestion / accusation input dialog.
	 * 
	 * @param player
	 * @param playerRoom
	 */
	public CardInputDialog(Player player, Room playerRoom, String type,
			Deck deck) {
		super(type + " Input");
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// get lists of selectable cards
		characters = deck.getCharacters();
		rooms = deck.getRooms();
		weapons = deck.getWeapons();

		// setup the card selector headings panel
		JPanel headerPanel = new JPanel(new GridLayout(0, 3));
		headerPanel.add(new JLabel("Character Card Select"));
		headerPanel.add(new JLabel("Room Card Select"));
		headerPanel.add(new JLabel("Weapon Card Select"));
		panel.add(headerPanel);

		// setup the option panel to hold card input fields
		JPanel optionPanel = new JPanel();
		optionPanel.setLayout(new GridLayout(0, 3));

		// setup the character card selector
		JPanel characterSelectorPanel = new JPanel(new GridLayout(0, 1));
		characterRadioGroup = new ButtonGroup();
		boolean defaultSelected = false;

		// setup a radio button for each character card
		for (int i = 0; i < characters.size(); i++) {
			JRadioButton button;
			button = new JRadioButton(characters.get(i).toString(), false);
			
			// grey out the character buttons the player knows has been refuted
			if (!player.getNonRefutedCharacters().contains(characters.get(i))) {
				button.setEnabled(false);
			} else if (!defaultSelected) {
				// set the default selected button if it has not been chosen yet
				button.setSelected(true);
				character = characters.get(i);
				defaultSelected = true;
			}

			// add the new radio button to the container and radio button group
			characterSelectorPanel.add(button);
			characterRadioGroup.add(button);

			// create a listener for the new radio button
			button.addItemListener(new CharacterRadioButtonHandler(characters
					.get(i)));
		}
		optionPanel.add(characterSelectorPanel);

		// setup the room card selector
		JPanel roomSelectorPanel = new JPanel(new GridLayout(0, 1));
		roomRadioGroup = new ButtonGroup();
		defaultSelected = false;

		// setup a radio button for each room card
		for (int i = 0; i < rooms.size(); i++) {
			JRadioButton button;
			button = new JRadioButton(rooms.get(i).toString(), false);

			// if this dialog is for a suggestion action
			if (type.equals("Suggestion")) {
				// if the current room button matches the player room
				if (rooms.get(i).toString().equals(playerRoom.getName())) {
					// the player must select the room they are currently in
					room = rooms.get(i);
					button.setSelected(true);
					defaultSelected = true;

					// grey out the button if the player knows it to be refuted
					if (!player.getNonRefutedRooms().contains(rooms.get(i))) {
						button.setEnabled(false);
					}
				} else {
					button.setEnabled(false);
				}
			}

			// grey out the room buttons the player knows has been refuted
			else if (!player.getNonRefutedRooms().contains(rooms.get(i))) {
				button.setEnabled(false);
			} else if (!defaultSelected) {
				// set the default selected button if it has not been chosen yet
				button.setSelected(true);
				room = rooms.get(i);
				defaultSelected = true;
			}

			// add the new radio button to the container and radio button group
			roomSelectorPanel.add(button);
			roomRadioGroup.add(button);

			// create a listener for the new radio button
			button.addItemListener(new RoomRadioButtonHandler(rooms.get(i)));
		}
		optionPanel.add(roomSelectorPanel);

		// setup the weapon card selector
		JPanel weaponSelectorPanel = new JPanel(new GridLayout(0, 1));
		weaponRadioGroup = new ButtonGroup();
		defaultSelected = false;

		// setup a radio button for each weapon card
		for (int i = 0; i < weapons.size(); i++) {
			JRadioButton button;
			button = new JRadioButton(weapons.get(i).toString(), false);

			// add the new radio button to the container and radio button group
			weaponSelectorPanel.add(button);
			weaponRadioGroup.add(button);

			// grey out the weapon buttons the player knows has been refuted
			if (!player.getNonRefutedWeapons().contains(weapons.get(i))) {
				button.setEnabled(false);
			} else if (!defaultSelected) {
				// set the default selected button if it has not been chosen yet
				button.setSelected(true);
				weapon = weapons.get(i);
				defaultSelected = true;
			}

			// create a listener for the new radio button
			button.addItemListener(new WeaponRadioButtonHandler(weapons.get(i)));
		}
		optionPanel.add(weaponSelectorPanel);

		panel.add(optionPanel);

		// setup the panel that contains the selected card images
		JPanel imagePanel = new JPanel();
		imagePanel.setLayout(new GridLayout(1, 3));

		// add the currently selected character card image
		characterImageLabel = new JLabel(new ImageIcon(character.getImage()));
		imagePanel.add(characterImageLabel);

		// add the currently selected room card image
		roomImageLabel = new JLabel(new ImageIcon(room.getImage()));
		imagePanel.add(roomImageLabel);

		// add the currently selected weapon card image
		weaponImageLabel = new JLabel(new ImageIcon(weapon.getImage()));
		imagePanel.add(weaponImageLabel);
		panel.add(imagePanel);

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
		setSize(700, 650);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * An inner class that handles the events that occur when the user changes
	 * the selected character card radio button.
	 */
	private class CharacterRadioButtonHandler implements ItemListener {

		// character card associated with this radio button
		private CharacterCard card;

		public CharacterRadioButtonHandler(CharacterCard card) {
			this.card = card;
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			// if the radio button was selected
			if (e.getStateChange() == ItemEvent.SELECTED) {
				// set the dialog box character card to this character card
				character = card;
				
				// set the dialog box character image to this card image
				characterImageLabel.setIcon(new ImageIcon(character.getImage()));
			}
		}
	}

	/**
	 * An inner class that handles the events that occur when the user changes
	 * the selected room card radio button.
	 */
	private class RoomRadioButtonHandler implements ItemListener {

		// room card associated with this radio button
		private RoomCard card;

		public RoomRadioButtonHandler(RoomCard card) {
			this.card = card;
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			// if the radio button was selected
			if (e.getStateChange() == ItemEvent.SELECTED) {
				// set the dialog box room card to this room card
				room = card;
				
				// set the dialog box room image to this card image
				roomImageLabel.setIcon(new ImageIcon(room.getImage()));
			}
		}
	}

	/**
	 * An inner class that handles the events that occur when the user changes
	 * the selected weapon card radio button.
	 */
	private class WeaponRadioButtonHandler implements ItemListener {

		// weapon card associated with this radio button
		private WeaponCard card;

		public WeaponRadioButtonHandler(WeaponCard card) {
			this.card = card;
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			// if the radio button was selected
			if (e.getStateChange() == ItemEvent.SELECTED) {
				// set the dialog box weapon card to this weapon card
				weapon = card;
				
				// set the dialog box weapon image to this card image
				weaponImageLabel.setIcon(new ImageIcon(weapon.getImage()));
			}
		}
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
