package cluedo.actions;

import cluedo.board.Location;

/**
 * Represents a movement action. Has a target location and moves the player
 * character token performing the action to the target.
 */
public class MoveAction implements Action {

	private Location location;

	/**
	 * Setup a new move action.
	 * 
	 * @param target_x
	 *            Target x position.
	 * @param target_y
	 *            Target y position.
	 */
	public MoveAction(int target_x, int target_y) {
		this.location = new Location(target_x, target_y);
	}

	public Location getLocation() {
		return location;
	}
}
