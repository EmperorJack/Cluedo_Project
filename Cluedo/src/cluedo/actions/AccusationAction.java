package cluedo.actions;

import cluedo.cards.CharacterCard;
import cluedo.cards.RoomCard;
import cluedo.cards.WeaponCard;

/**
 * Represents an accusation action that is used when a player accuses a
 * character, room and weapon against the solution cards.
 */
public class AccusationAction extends SuggestionAction {

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
		super(character, room, weapon);
	}

}
