package cluedo.board;

import cluedo.actions.MoveAction;
import cluedo.actions.MoveSequence;
import cluedo.actions.SuggestionAction;
import cluedo.actions.WarpAction;
import cluedo.game.Dice;
import cluedo.game.Player;
import cluedo.tiles.*;
import cluedo.tokens.*;
import cluedo.view.Canvas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Board {
	Map<Location, Tile> tiles;
	Set<Tile> validTiles;
	String[] boardStrings;
	List<CharacterToken> characters;
	Map<String, Room> roomMap;
	List<WeaponToken> weapons;
	double scaleTest;
	private Image boardImage;
	int clkCnt = 0;
	int mouseX;
	int mouseY;
	int boardXOffset;
	int boardYOffset;
	double boardScale;
	private Player currentPlayer;
	private Dice dice;

	boolean tokenMoving = false;
	List<MoveSequence> moves;

	public static final int SQUARE_SIZE = 36;
	public static final int GRID_X_OFFSET = 318 + 61;
	public static final int GRID_Y_OFFSET = 41;
	public static final int BOARD_OFFSET = 318;
	public static final int BOARD_WIDTH = 1477;
	private static final double BOARD_HEIGHT = 985;

	/**
	 * Creates the game board. The underlying board for the game logic is a Map
	 * of Locations(x,y) to Tile objects, while an array of Strings is used to
	 * return a more visually appealing representation to the UI class.
	 * 
	 * @param weapons
	 *            An array of the names of the weapons on the board.
	 * @param rooms
	 *            An array of the names of the rooms on the board.
	 * 
	 * @author Kelly
	 */
	public Board(String[] weapons, String[] rooms, Dice dice) {
		moves = new ArrayList<MoveSequence>();
		validTiles = new HashSet<Tile>();
		this.dice = dice;
		this.boardImage = Canvas.loadImage("board.jpg");
		scaleTest = 1;

		// Construct string version of the board
		boardStrings = BoardParser.parseStringBoard();
		// Construct the tile based version of the board
		roomMap = BoardParser.constructRoomSet();

		tiles = BoardParser.parseTileBoard(roomMap);
		characters = BoardParser.parseCharacters();
		for (CharacterToken t : characters) {
			t.setX(t.getLocation().getX() * SQUARE_SIZE + GRID_X_OFFSET);
			t.setY(t.getLocation().getY() * SQUARE_SIZE + GRID_Y_OFFSET);
		}
		this.weapons = new ArrayList<WeaponToken>();

		// init weapontokens
		ArrayList<String> roomsList = new ArrayList<String>();
		roomsList.addAll(Arrays.asList(rooms));
		for (int i = 0; i < weapons.length; i++) {
			int randomIndex = (int) (Math.random() * roomsList.size());
			WeaponToken tokenToAdd = new WeaponToken(weapons[i],
					roomMap.get(roomsList.get(randomIndex)));
			this.weapons.add(tokenToAdd);
			Set<RoomTile> tiles = roomMap.get(roomsList.get(randomIndex))
					.getRoomTiles();
			tokenToAdd.setRoom(roomMap.get(roomsList.get(randomIndex)));
			tokenToAdd.setLocation(tiles.iterator().next().getLocation());
			roomsList.remove(randomIndex);
		}

		for (WeaponToken t : this.weapons) {
			t.setX(t.getLocation().getX() * SQUARE_SIZE + GRID_X_OFFSET);
			t.setY(t.getLocation().getY() * SQUARE_SIZE + GRID_Y_OFFSET);
		}

	}

	/**
	 * Uses Dijkstra's algorithm to calculate the shortest possible path to a
	 * location on the board.
	 * 
	 * @param token
	 *            CharacterToken to move
	 * @param loc
	 *            Destination location.
	 * @return The number of steps in the shortest path, or -1 if there is no
	 *         available path.
	 */
	public int calculatePathLength(CharacterToken token, Location loc) {
		if (!token.inRoom()) {
			Location charLoc = token.getLocation();
			Dijkstra d = new Dijkstra(tiles);
			int pathLength = d.findPath(charLoc, loc);
			// System.out.printf("Path length %d\n", pathLength);
			return (pathLength);
		} else {
			Room currentRoom = token.getRoom();
			int pathLength = Integer.MAX_VALUE;
			for (DoorTile door : currentRoom.getEntrances()) {
				Dijkstra d = new Dijkstra(tiles);
				int pathFromDoor = d.findPath(door.getLocation(), loc);
				if (pathFromDoor < pathLength)
					pathLength = pathFromDoor;
			}
			return (pathLength);
		}
	}

	public void setValidTiles() {
		if (dice.getResult() > 0) {
			if (currentPlayer.getToken().inRoom()) {
				Room room = currentPlayer.getToken().getRoom();
				Set<DoorTile> doors = room.getEntrances();
				validTiles.clear();
				for (DoorTile door : doors) {
					Dijkstra d = new Dijkstra(tiles);
					validTiles.addAll(d.getValidTiles(door.getLocation(),
							dice.getResult()));
				}
				validTiles.removeAll(doors);

			} else {
				Location charLoc = currentPlayer.getToken().getLocation();
				Dijkstra d = new Dijkstra(tiles);
				validTiles = d.getValidTiles(charLoc, dice.getResult());
			}
			for (CharacterToken c : characters) {
				validTiles.remove(tiles.get(c.getLocation()));
			}
		} else
			validTiles.clear();
	}

	/**
	 * Checks if a CharacterToken is on a given location on the board.
	 * 
	 * @param loc
	 *            Location to check.
	 * @return True if there is a CharacterToken, false if not.
	 */
	public boolean hasTokenOn(Location loc) {
		for (CharacterToken c : characters) {
			if (c.getLocation().equals(loc))
				return true;
		}
		for (WeaponToken w : weapons) {
			if (w.getLocation().equals(loc))
				return true;
		}
		return false;
	}

	/**
	 * Moves the player token to the set location on the board, if the
	 * characterToken is on a door tile it moves the token into the room.
	 * 
	 * @param token
	 *            CharacterToken to move.
	 * @param loc
	 *            Location to place on board.
	 */
	public void movePlayer(CharacterToken token, Location loc) {
		Tile t = getTile(loc);
		if (!(t instanceof WallTile) && !hasTokenOn(loc)) {
			if (token.inRoom()) {
				token.leaveRoom();
			}
			if (t instanceof PathTile) {
				token.setLocation(loc);
			}
			if (t instanceof DoorTile) {
				DoorTile door = (DoorTile) t;
				token.setLocation(loc);
				token.setRoom(door.getRoom());
			}
		}
	}

	/**
	 * Returns the room a character is in.
	 * 
	 * @param token
	 *            Character in room.
	 * @return Room the character is in, null if none.
	 */
	public Room roomIn(CharacterToken token) {
		if (!token.inRoom())
			return null;
		return token.getRoom();
	}

	public String getRoomInfo() {
		String roomInfo = "";
		for (Room r : roomMap.values()) {
			String room = r.toString();
			if (!room.equals(""))
				roomInfo += r + "\n";
		}
		return roomInfo;
	}

	@Override
	public String toString() {
		String boardString = "";
		return boardString;
	}

	public Tile getSelectedTile() {
		double newGridX = GRID_X_OFFSET * boardScale;
		double newGridY = GRID_Y_OFFSET * boardScale;
		double newSquareSize = SQUARE_SIZE * boardScale;
		int X = (int) ((mouseX - boardXOffset - newGridX) / newSquareSize);
		int Y = (int) ((mouseY - boardYOffset - newGridY) / newSquareSize);
		if (X >= 0 && X <= 23 && Y >= 0 && Y <= 24) {
			return tiles.get(new Location(X, Y));
		} else
			return null;
	}

	public void draw(Graphics2D g, int width, int height) {
		double boardXScale = (double) width / BOARD_WIDTH;
		double boardYScale = (double) height / BOARD_HEIGHT; // Scalar of the
																// image
		if (boardXScale < boardYScale) {
			boardScale = boardXScale; // Scalar of the image
			boardXOffset = 0;
			boardYOffset = (int) (height - BOARD_HEIGHT * boardScale) / 2;
		} else {
			boardScale = boardYScale; // Scalar of the image
			boardXOffset = (int) (width - BOARD_WIDTH * boardScale) / 2;
			boardYOffset = 0;
		}
		AffineTransform transform = new AffineTransform();
		transform.translate(boardXOffset, boardYOffset);
		g.setTransform(transform);
		AffineTransform scaleTransform = AffineTransform.getScaleInstance(
				boardScale, boardScale);

		AffineTransformOp bilinearScaleOp = new AffineTransformOp(
				scaleTransform, AffineTransformOp.TYPE_BILINEAR);
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, (int) (BOARD_WIDTH * boardScale),
				(int) (BOARD_HEIGHT * boardScale));
		g.drawImage(bilinearScaleOp.filter((BufferedImage) boardImage,
				new BufferedImage(
						(int) (boardImage.getWidth(null) * boardScale), (int) (boardImage.getHeight(null) * boardScale),
						((BufferedImage) boardImage).getType())),
				(int) (BOARD_OFFSET * boardScale), 0, null);

		g.drawString(mouseX + " " + mouseY, 10, 10);
		transform.scale(boardScale, boardScale);
		g.setTransform(transform);

		Tile selected = getSelectedTile();
		if (selected != null) {
			if (validTiles.contains(selected)) {
				selected.draw(g, new Color(0, 255, 0, 125)); // You can move
																// here, draw
																// green
			} else {
				selected.draw(g, new Color(255, 0, 0, 125)); // You cannot move
																// here, draw
																// red
			}
		}

		for (Tile t : validTiles) {
			if (selected != null) { // If there is a selected tile do not draw
									// it
				if (!t.equals(selected)) {
					if (t instanceof PathTile) {
						t.draw(g, new Color(0, 0, 255, 125));
					}
					if (t instanceof DoorTile) {
						t.draw(g, new Color(0, 255, 255, 125));
					}

				}
			} else { // Otherwise draw all valid tiles.
				if (t instanceof PathTile) {
					t.draw(g, new Color(0, 0, 255, 125));
				}
				if (t instanceof DoorTile) {
					t.draw(g, new Color(0, 255, 255, 125));
				}
			}
		}
		
		AffineTransform playerTransform = new AffineTransform();
		playerTransform.translate(boardXOffset, boardYOffset);
		playerTransform.scale(boardScale, boardScale);
		g.setTransform(playerTransform);
		currentPlayer.draw(g);

		if (dice.getResult() > 0) { // Draw dice at the bottom right next to the
									// board
			AffineTransform diceTransform = new AffineTransform();
			diceTransform.translate(boardXOffset, boardYOffset);
			diceTransform.scale(boardScale, boardScale);
			diceTransform.translate(35, 851);
			g.setTransform(diceTransform);
			dice.draw(g);
		}

		for (CharacterToken t : characters) {
			AffineTransform tokenTransform = new AffineTransform();
			tokenTransform.translate(boardXOffset, boardYOffset);
			tokenTransform.scale(boardScale, boardScale);
			tokenTransform.translate(t.getXPos(), t.getYPos());

			g.setTransform(tokenTransform);
			t.draw(g);
		}

		for (WeaponToken t : weapons) {
			AffineTransform tokenTransform = new AffineTransform();
			tokenTransform.translate(boardXOffset, boardYOffset);
			tokenTransform.scale(boardScale, boardScale);
			tokenTransform.translate(t.getXPos(), t.getYPos());

			g.setTransform(tokenTransform);
			t.draw(g);
		}

	}

	public void tick() {
		if (!moves.isEmpty()) {
			for (int i = 0; i < moves.size(); i++) {
				if (moves.get(i).isFinished()) {
					moves.remove(i);
				}
			}
			for (MoveSequence move : moves) {
				if (!move.isFinished()) {
					move.tick();
				}
			}
		}
	}

	/**
	 * Returns a CharacterToken of the given name.
	 * 
	 * @param name
	 *            The name of the CharacterToken required.
	 * @return CharacterToken of the requested character.
	 */
	public CharacterToken getCharacterToken(String name) {
		for (CharacterToken c : characters) {
			if (c.getName().equals(name))
				return c;
		}
		return null;
	}

	public WeaponToken getWeaponToken(String name) {
		for (WeaponToken w : weapons) {
			if (w.getName().equals(name))
				return w;
		}

		return null;
	}

	public Tile getTile(Location loc) {
		return tiles.get(loc);
	}

	public void setPlayer(Player player) {
		currentPlayer = player;
	}

	public void updateMousePos(int x, int y) {
		mouseX = x;
		mouseY = y;
	}

	public void moveTokensForSuggest(SuggestionAction s) {
		WeaponToken weapon = getWeaponToken(s.getWeapon().toString());
		CharacterToken character = getCharacterToken(s.getCharacter()
				.toString());
		Room thisRoom = currentPlayer.getToken().getRoom();
		Location collisionDetect = new Location(0, 0);
		// If tokens are not already in the room, move them.
		if (!character.inRoom() || !character.getRoom().equals(thisRoom)) {
			Set<RoomTile> roomTiles = thisRoom.getRoomTiles();
			for (RoomTile roomTile : roomTiles) {
				if (!hasTokenOn(roomTile.getLocation())) {
					collisionDetect = roomTile.getLocation();
					moves.add(new MoveSequence(new WarpAction(roomTile
							.getLocation()), character));
					character.leaveRoom();
					character.setRoom(thisRoom);
					break;
				}
			}
		}
		if (!weapon.getRoom().equals(thisRoom)) {
			Set<RoomTile> roomTiles = thisRoom.getRoomTiles();
			for (RoomTile roomTile : roomTiles) {
				if (!hasTokenOn(roomTile.getLocation())
						&& !roomTile.getLocation().equals(collisionDetect)) {
					moves.add(new MoveSequence(new WarpAction(roomTile
							.getLocation()), weapon));
					weapon.leaveRoom();
					weapon.setRoom(thisRoom);
				}
			}
		}

	}

	public MoveSequence triggerMove(int x, int y) {
		MoveSequence move = null;
		mouseX = x;
		mouseY = y;
		// updateValid tiles
		setValidTiles();
		Tile selected = getSelectedTile();
		if (selected == null || !validTiles.contains(selected))
			return null; // can't move here, can't move yet
		if (!currentPlayer.getToken().inRoom()) {
			Dijkstra d = new Dijkstra(tiles);
			List<Tile> path = d.getDijsktraPath(currentPlayer.getToken()
					.getLocation(), selected.getLocation());
			move = new MoveSequence(
					new MoveAction(selected.getLocation(), path),
					currentPlayer.getToken());
		} else {
			Room room = currentPlayer.getToken().getRoom();
			currentPlayer.getToken().leaveRoom();
			Set<DoorTile> doors = room.getEntrances();
			for (DoorTile door : doors) {
				Dijkstra d = new Dijkstra(tiles);
				Set<Tile> tilesToCheck = d.getValidTiles(door.getLocation(),
						dice.getResult());
				if (tilesToCheck.contains(selected)) {
					List<Tile> path = d.getDijsktraPath(door.getLocation(),
							selected.getLocation());
					move = new MoveSequence(new WarpAction(door.getLocation()),
							currentPlayer.getToken());
					move.addAction(new MoveAction(selected.getLocation(), path));
					break;
				}
			}
		}
		if (selected instanceof DoorTile) {
			currentPlayer.getToken().setRoom(((DoorTile) selected).getRoom());
			HashSet<RoomTile> roomTiles = ((DoorTile) selected).getRoom()
					.getRoomTiles();
			for (RoomTile t : roomTiles) {
				if (move != null && !hasTokenOn(t.getLocation())) {
					move.addAction(new WarpAction(t.getLocation()));
					break;
				}
			}

		}
		moves.add(move);
		dice.resetValues();
		setValidTiles();
		return move;
	}

	public void moveViaPassage(CharacterToken token, Room destination) {
		token.leaveRoom();
		Set<RoomTile> roomTiles = destination.getRoomTiles();
		for (RoomTile roomTile : roomTiles) {
			if (!hasTokenOn(roomTile.getLocation())) {
				moves.add(new MoveSequence(new WarpAction(roomTile
						.getLocation()), token));
				token.setRoom(destination);
				break;
			}
		}
	}
}
