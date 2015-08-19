package cluedo.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
	private CharacterCard character;
	private RoomCard room;
	private WeaponCard weapon;

	/**
	 * Setup a new suggestion / accusation input dialog.
	 * 
	 * @param player
	 * @param playerRoom
	 */
	public CardInputDialog(Player player, Room playerRoom, String type) {
		super(type + " Input");
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));

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
