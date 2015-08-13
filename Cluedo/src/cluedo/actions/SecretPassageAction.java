package cluedo.actions;

import cluedo.board.Room;

/**
 * Represents the action of a player moving from the corner rooms via a secret
 * passage. Has a source and destination room.
 * 
 * @author Jack
 *
 */
public class SecretPassageAction implements Action {

	private Room source;
	private Room destination;

	/**
	 * Setup a new secret passage action.
	 * 
	 * @param source
	 *            The room the player is leaving from.
	 * @param destination
	 *            The room the player is going to.
	 */
	public SecretPassageAction(Room source, Room destination) {
		this.source = source;
		this.destination = destination;
	}

	// get methods to return the rooms that make up this action
	
	public Room getSource() {
		return source;
	}

	public Room getDestination() {
		return destination;
	}
}
