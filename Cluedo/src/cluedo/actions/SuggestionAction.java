package cluedo.actions;

import cluedo.cards.*;

/**
 * Represents a suggestion action that is used when a player suggests a
 * character, room and weapon to the other players who will refute the
 * suggestion if they have matching cards in their hand.
 */
public class SuggestionAction implements Action {

	private CharacterCard character;
	private RoomCard room;
	private WeaponCard weapon;

	/**
	 * Setup a new suggestion action.
	 * 
	 * @param character
	 *            The character being suggested.
	 * @param room
	 *            The room being suggested.
	 * @param weapon
	 *            The weapon being suggested.
	 */
	public SuggestionAction(CharacterCard character, RoomCard room,
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
