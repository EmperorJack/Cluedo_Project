package cluedo.actions;

import java.util.List;

import static cluedo.board.Board.squareSize;
import static cluedo.board.Board.gridXoffset;
import static cluedo.board.Board.gridYoffset;
import cluedo.board.Location;
import cluedo.tiles.Tile;
import cluedo.tokens.CharacterToken;

/**
 * Represents a movement action. Has a target location and moves the player
 * character token performing the action to the target.
 */
public class MoveAction implements Action {
	private Location endLocation;
	Tile nextInPath;
	public List<Tile> path;
	public int frameCounter;
	boolean finished = false;

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
		nextInPath = nextTile();
	}

	public Location getLocation() {
		return endLocation;
	}

	public void tick(CharacterToken playerToken) {
		if (playerToken.getLocation().equals(nextInPath.getLocation())) {
			nextInPath = nextTile();
			if (nextInPath == null) {
				setFinished();
			}
		} else {
			frameCounter++;
			double moveFraction = frameCounter / 4.0f;
			double currentX = (playerToken.getLocation().getX() + (nextInPath
					.getLocation().getX() - playerToken.getLocation().getX())
					* moveFraction) * squareSize + gridXoffset;
			double currentY = (playerToken.getLocation().getY() + (nextInPath
					.getLocation().getY() - playerToken.getLocation().getY())
					* moveFraction) * squareSize + gridYoffset;
			playerToken.setX((int) currentX);
			playerToken.setY((int) currentY);
			if (frameCounter == 4) {
				playerToken.setLocation(nextInPath.getLocation());
				reset();
			}
		}
	}

	public void reset() {
		frameCounter = 0;
	}

	public int getFrame() {
		return frameCounter;
	}

	public void setFinished() {
		finished = true;
	}

	public boolean isFinished() {
		return finished;
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
