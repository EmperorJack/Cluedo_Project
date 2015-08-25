package cluedo.actions;

import java.util.List;

import static cluedo.board.Board.squareSize;
import static cluedo.board.Board.gridXoffset;
import static cluedo.board.Board.gridYoffset;
import cluedo.board.Location;
import cluedo.tiles.Tile;
import cluedo.tokens.CharacterToken;
import cluedo.tokens.Token;

/**
 * Represents a movement action. Has a target location and moves the player
 * character token performing the action to the target. The token will move
 * along a path to the target over time as an animation.
 */
public class MoveAction implements Action, BoardMove {
	private Location endLocation;
	private Tile nextInPath;
	private List<Tile> path;
	private int frameCounter;
	private boolean finished;

	/**
	 * Setup a new player movement action.
	 * 
	 * @param loc
	 *            The target location being moved to.
	 * @param path
	 *            The path to the target location.
	 */
	public MoveAction(Location loc, List<Tile> path) {
		this.endLocation = loc;
		this.path = path;
		nextInPath = nextTile();
		finished = false;
	}

	public Location getLocation() {
		return endLocation;
	}

	/**
	 * Called each time the clock thread ticks. Allows animation of the token
	 * moving over time.
	 * 
	 * @param playerToken
	 *            The character token being moved.
	 */
	public void tick(Token playerToken) {
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

	/**
	 * Gets the next tile on the movement path.
	 * 
	 * @return The next tile to move to in the path.
	 */
	public Tile nextTile() {
		if (!path.isEmpty()) {
			Tile t = path.get(0);
			path.remove(t);
			return t;
		}
		return null;
	}
}
