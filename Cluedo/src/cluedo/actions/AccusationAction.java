package cluedo.actions;

import cluedo.cards.CharacterCard;
import cluedo.cards.RoomCard;
import cluedo.cards.WeaponCard;

/**
 * Represents an accusation action that is used when a player accuses a
 * character, room and weapon against the solution cards.
 */
public class AccusationAction implements Action {

	private CharacterCard character;
	private RoomCard room;
	private WeaponCard weapon;

	/**
	 * Setup a new accusation action.
	 * 
	 * @param character
	 *            The character being suggested.
	 * @param room
	 *            The room being suggested.
	 * @param weapon
	 *            The weapon being suggested.
	 */
	public AccusationAction(CharacterCard character, RoomCard room,
			WeaponCard weapon) {
		this.character = character;
		this.room = room;
		this.weapon = weapon;
	}

	// get methods below to return the cards that make up this action

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
