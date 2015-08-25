package cluedo.actions;


import static cluedo.board.Board.SQUARE_SIZE;
import static cluedo.board.Board.GRID_X_OFFSET;
import static cluedo.board.Board.GRID_Y_OFFSET;


import cluedo.board.Location;
import cluedo.tokens.Token;

public class WarpAction implements BoardMove{
	private Location endLocation;
	private int frameCounter;
	private boolean finished;
	
	public WarpAction(Location loc) {
		this.endLocation = loc;
		finished = false;
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

	@Override
	public void tick(Token t) {
		frameCounter++;
		double moveFraction = frameCounter / 15.0f;
		double currentX = (t.getLocation().getX() + (endLocation.getX() - t.getLocation().getX())
				* moveFraction) * SQUARE_SIZE + GRID_X_OFFSET;
		double currentY = (t.getLocation().getY() + (endLocation.getY() - t.getLocation().getY())
				* moveFraction) * SQUARE_SIZE + GRID_Y_OFFSET;
		t.setX((int) currentX);
		t.setY((int) currentY);
		if (frameCounter == 15){
			t.setLocation(endLocation);
			setFinished();
		}
		
	}
	
	public void setFinished() {
		finished = true;
	}



}
