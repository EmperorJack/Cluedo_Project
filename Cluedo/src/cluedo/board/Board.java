package cluedo.board;

import cluedo.game.Dice;
import cluedo.game.Player;
import cluedo.tiles.*;
import cluedo.tokens.*;
import cluedo.view.Canvas;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Board {
	Map<Location, Tile> tiles;
	String[] boardStrings;
	List<CharacterToken> characters;
	Map<String, Room> roomMap;
	List<WeaponToken> weapons;
	double scaleTest;
	private BufferedImage boardImage;
	int clkCnt = 0;
	int mouseX;
	int mouseY;
	private Player currentPlayer;
	private Dice dice;

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
		this.dice = dice;
		this.boardImage = (BufferedImage) Canvas.loadImage("board.jpg");
		scaleTest = 1;

		// Construct string version of the board
		boardStrings = BoardParser.parseStringBoard();
		// Construct the tile based version of the board
		roomMap = BoardParser.constructRoomSet();

		tiles = BoardParser.parseTileBoard(roomMap);
		characters = BoardParser.parseCharacters();
		this.weapons = new ArrayList<WeaponToken>();

		// init weapontokens
		ArrayList<String> roomsList = new ArrayList<String>();
		roomsList.addAll(Arrays.asList(rooms));
		for (int i = 0; i < weapons.length; i++) {
			int randomIndex = (int) (Math.random() * roomsList.size());
			WeaponToken tokenToAdd = new WeaponToken(weapons[i],
					roomMap.get(roomsList.get(randomIndex)));
			this.weapons.add(tokenToAdd);
			roomMap.get(roomsList.get(randomIndex)).addToken(tokenToAdd);
			roomsList.remove(randomIndex);
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

	/**
	 * Checks if a CharacterToken is on a given location on the board.
	 * 
	 * @param loc
	 *            Location to check.
	 * @return True if there is a CharacterToken, false if not.
	 */
	public boolean hasCharacterOn(Location loc) {
		for (CharacterToken c : characters) {
			if (c.getLocation().equals(loc))
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
		if (!(t instanceof WallTile) && !hasCharacterOn(loc)) {
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
		String[] updatedBoard = updateBoard();
		String boardString = ""; // Simply merges the array into a single string
									// at this point
		for (int i = 0; i < updatedBoard.length; i++) {
			boardString += updatedBoard[i] + "\n";
		}
		return boardString;
	}

	/**
	 * Splices character positions onto the text board.
	 * 
	 * @return A new String array of the board with the correct character
	 *         positions.
	 */
	public String[] updateBoard() {
		String[] updatedBoard = Arrays
				.copyOf(boardStrings, boardStrings.length);
		// for each Character get yPos, get corresponding string, replace new
		// concatenated string
		for (CharacterToken c : characters) {
			if (!c.inRoom()) {
				int charY = c.getLocation().getY();
				String charRow = updatedBoard[charY + 1];
				int charIndex = (c.getLocation().getX() * 2) + 4;
				String updatedRow = charRow.substring(0, charIndex)
						+ c.getChar()
						+ charRow.substring(charIndex + 1, charRow.length());
				updatedBoard[charY + 1] = updatedRow;
			}
		}
		return updatedBoard;
	}

	public void draw(Graphics2D g, int width, int height) {
		double scale = (double)height/boardImage.getHeight(null);
		AffineTransform transform = new AffineTransform();
		transform.translate((width - boardImage.getWidth(null) * scale)/ 2, 0);
		g.setTransform(transform);
		try {
			BufferedImage imageToDraw  = getScaledImage(boardImage, (int)(boardImage.getWidth(null) * scale), height);
			g.drawImage(imageToDraw,0,0, null);
		} catch (IOException e) {
		}
		g.drawString(mouseX + " " + mouseY, 10 ,10);
		// TODO draw the board
	}
	
	public static BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException {
	    int imageWidth  = image.getWidth();
	    int imageHeight = image.getHeight();

	    double scaleX = (double)width/imageWidth;
	    double scaleY = (double)height/imageHeight;
	    AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
	    AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

	    return bilinearScaleOp.filter(
	        image,
	        new BufferedImage(width, height, image.getType()));
	}

	public void tick() {
		clkCnt++;
		scaleTest = 0.9 + (0.1 * (Math.sin(Math.toRadians(clkCnt))));
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

	public void moveTokenToRoom(CharacterToken token, Room destination) {
		token.leaveRoom();
		token.setRoom(destination);
	}

	public void moveTokenToRoom(WeaponToken token, Room destination) {
		token.leaveRoom();
		token.setRoom(destination);
	}
	
	public void setPlayer(Player player) {
		currentPlayer = player;
	}

	public void updateMousePos(int x, int y) {
		mouseX = x;
		mouseY = y;
		
	}
}
