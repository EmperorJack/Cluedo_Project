package cluedo.actions;

import java.util.List;

import cluedo.board.Location;
import cluedo.tiles.Tile;
import cluedo.tokens.CharacterToken;

/**
 * Represents a movement action. Has a target location and moves the player
 * character token performing the action to the target.
 */
public class MoveAction implements Action {
	private Location endLocation;
	public List<Tile> path;
	public int frameCounter;

	/**
	 * Setup a new move action.
	 * 
	 * @param target_x
	 *            Target x position.
	 * @param target_y
	 *            Target y position.
	 */
	public MoveAction(Location loc, List<Tile> path) {
		this.endLocation = loc;
		this.path = path;
	}

	public Location getLocation() {
		return endLocation;
	}
	
	public void tick(){
		frameCounter++;
	}
	
	public void reset(){
		frameCounter=0;
	}
	
	public int getFrame(){
		return frameCounter;
	}

	public Tile nextTile() {
		if (!path.isEmpty()) {
			Tile t = path.get(0);
			path.remove(t);
			return t;
		}
		return null;
	}
}
