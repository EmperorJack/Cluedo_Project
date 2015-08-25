package cluedo.board;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import cluedo.tiles.*;
import cluedo.tokens.CharacterToken;

public class BoardParser {

	/**
	 * Parses the display board.
	 * 
	 * @return A String array containing the display board.
	 */
	public static String[] parseStringBoard() {
		String[] boardStrings = new String[27]; // construct array of strings
		try {
			Scanner stringMapScanner = new Scanner(
					new File("cluedomap.txt"));
			int i = 0;
			while (stringMapScanner.hasNextLine()) { // scan each line of the
														// board into the array
				boardStrings[i] = stringMapScanner.nextLine();
				i++;
			}
			stringMapScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return boardStrings;
	}

	/**
	 * Constructs a map of the rooms on the board.
	 * 
	 * @return A map of Strings to Rooms.
	 */
	public static Map<String, Room> constructRoomSet() {
		Map<String, Room> rooms = new HashMap<String, Room>();
		rooms.put("Kitchen", new Room("Kitchen"));
		rooms.put("Ballroom", new Room("Ballroom"));
		rooms.put("Conservatory", new Room("Conservatory"));
		rooms.put("Billiard Room", new Room("Billiard Room"));
		rooms.put("Library", new Room("Library"));
		rooms.put("Study", new Room("Study"));
		rooms.put("Hall", new Room("Hall"));
		rooms.put("Lounge", new Room("Lounge"));
		rooms.put("Dining Room", new Room("Dining Room"));
		rooms.get("Kitchen").setPassage(rooms.get("Study"));
		rooms.get("Study").setPassage(rooms.get("Kitchen"));
		rooms.get("Lounge").setPassage(rooms.get("Conservatory"));
		rooms.get("Conservatory").setPassage(rooms.get("Lounge"));
		return rooms;
	}

	/**
	 * Constructs a map of Locations to Tiles for to carry out the board logic,
	 * also adds DoorTiles to the appropriate rooms.
	 * 
	 * @param rooms
	 *            A map of rooms on the board
	 * @return A map of Locations to Tiles.
	 */
	public static Map<Location, Tile> parseTileBoard(Map<String, Room> rooms) {
		Map<Location, Tile> tiles = new HashMap<Location, Tile>();
		try {
			Scanner parseMapScanner = new Scanner(new File("parsemap.txt"));
			int i = 0;
			while (parseMapScanner.hasNextLine()) {
				String currentLine = parseMapScanner.nextLine();
				char[] currentChars = currentLine.toCharArray(); // turns each
																	// line of
																	// the map
																	// into an
																	// array of
																	// chars for
																	// parsing

				for (int j = 0; j < currentChars.length; j++) {
					// constructs each column of this row based on the value of
					// the char
					switch (currentChars[j]) {
					case ('W'): {
						Location loc = new Location(j, i);
						tiles.put(loc, new WallTile(loc));
						break;
					}
					case (' '):
					case ('g'):
					case ('w'):
					case ('b'):
					case ('p'):
					case ('m'):
					case ('s'): {
						Location loc = new Location(j, i);
						tiles.put(loc, new PathTile(loc));
						break;
					}
					case ('1'): {
						Location loc = new Location(j, i);
						DoorTile entrance = new DoorTile(loc,
								rooms.get("Study"));
						tiles.put(loc, entrance);
						rooms.get("Study").addEntrance(entrance);
						break;
					}
					case ('2'): {
						Location loc = new Location(j, i);
						DoorTile entrance = new DoorTile(loc,
								rooms.get("Hall"));
						tiles.put(loc, entrance);
						rooms.get("Hall").addEntrance(entrance);
						break;
					}
					case ('3'): {
						Location loc = new Location(j, i);
						DoorTile entrance = new DoorTile(loc,
								rooms.get("Lounge"));
						tiles.put(loc, entrance);
						rooms.get("Lounge").addEntrance(entrance);
						break;
					}
					case ('4'): {
						Location loc = new Location(j, i);
						DoorTile entrance = new DoorTile(loc,
								rooms.get("Library"));
						tiles.put(loc, entrance);
						rooms.get("Library").addEntrance(entrance);
						break;
					}
					case ('5'): {
						Location loc = new Location(j, i);
						DoorTile entrance = new DoorTile(loc,
								rooms.get("Billiard Room"));
						tiles.put(loc, entrance);
						rooms.get("Billiard Room").addEntrance(entrance);
						break;
					}
					case ('6'): {
						Location loc = new Location(j, i);
						DoorTile entrance = new DoorTile(loc,
								rooms.get("Conservatory"));
						tiles.put(loc, entrance);
						rooms.get("Conservatory").addEntrance(entrance);
						break;
					}
					case ('7'): {
						Location loc = new Location(j, i);
						DoorTile entrance = new DoorTile(loc, rooms.get("Ballroom"));
						tiles.put(loc, entrance);
						rooms.get("Ballroom").addEntrance(entrance);
						break;
					}
					case ('8'): {
						Location loc = new Location(j, i);
						DoorTile entrance = new DoorTile(loc,
								rooms.get("Kitchen"));
						tiles.put(loc, entrance);
						rooms.get("Kitchen").addEntrance(entrance);
						break;
					}
					case ('9'): {
						Location loc = new Location(j, i);
						DoorTile entrance = new DoorTile(loc,
								rooms.get("Dining Room"));
						tiles.put(loc, entrance);
						rooms.get("Dining Room").addEntrance(entrance);
						break;
					}
					case ('!'): {
						Location loc = new Location(j, i);
						RoomTile roomTile = new RoomTile(loc,
								rooms.get("Study"));
						tiles.put(loc, roomTile);
						rooms.get("Study").addRoomTile(roomTile);
						break;
					}
					case ('@'): {
						Location loc = new Location(j, i);
						RoomTile roomTile = new RoomTile(loc,
								rooms.get("Hall"));
						tiles.put(loc, roomTile);
						rooms.get("Hall").addRoomTile(roomTile);
						break;
					}
					case ('#'): {
						Location loc = new Location(j, i);
						RoomTile roomTile = new RoomTile(loc,
								rooms.get("Lounge"));
						tiles.put(loc, roomTile);
						rooms.get("Lounge").addRoomTile(roomTile);
						break;
					}
					case ('$'): {
						Location loc = new Location(j, i);
						RoomTile roomTile = new RoomTile(loc,
								rooms.get("Library"));
						tiles.put(loc, roomTile);
						rooms.get("Library").addRoomTile(roomTile);
						break;
					}
					case ('%'): {
						Location loc = new Location(j, i);
						RoomTile roomTile = new RoomTile(loc,
								rooms.get("Billiard Room"));
						tiles.put(loc, roomTile);
						rooms.get("Billiard Room").addRoomTile(roomTile);
						break;
					}
					case ('^'): {
						Location loc = new Location(j, i);
						RoomTile roomTile = new RoomTile(loc,
								rooms.get("Conservatory"));
						tiles.put(loc, roomTile);
						rooms.get("Conservatory").addRoomTile(roomTile);
						break;
					}
					case ('&'): {
						Location loc = new Location(j, i);
						RoomTile roomTile = new RoomTile(loc,
								rooms.get("Ballroom"));
						tiles.put(loc, roomTile);
						rooms.get("Ballroom").addRoomTile(roomTile);
						break;
					}
					case ('*'): {
						Location loc = new Location(j, i);
						RoomTile roomTile = new RoomTile(loc,
								rooms.get("Kitchen"));
						tiles.put(loc, roomTile);
						rooms.get("Kitchen").addRoomTile(roomTile);
						break;
					}
					case ('('): {
						Location loc = new Location(j, i);
						RoomTile roomTile = new RoomTile(loc,
								rooms.get("Dining Room"));
						tiles.put(loc, roomTile);
						rooms.get("Dining Room").addRoomTile(roomTile);
						break;
					}
					default:
						break;
					}

				}
				i++;
			}
			parseMapScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return tiles;
	}

	/**
	 * Constructs a list of CharacterTokens constructed with their initial
	 * position on the board.
	 * 
	 * @return A list of CharacterTokens on the board.
	 */
	public static List<CharacterToken> parseCharacters() {
		List<CharacterToken> characters = new ArrayList<CharacterToken>();
		try {
			Scanner parseMapScanner = new Scanner(new File("src/parsemap_2.txt"));
			int i = 0;
			while (parseMapScanner.hasNextLine()) {
				String currentLine = parseMapScanner.nextLine();
				char[] currentChars = currentLine.toCharArray(); // turns each
																	// line of
																	// the map
																	// into an
																	// array of
																	// chars for
																	// parsing
				for (int j = 0; j < currentChars.length; j++) {
					switch (currentChars[j]) {
					case ('g'): {
						characters.add(new CharacterToken("The Reverend Green",
								'G', j, i));
						break;
					}
					case ('w'): {
						characters.add(new CharacterToken("Mrs. White", 'W', j,
								i));
						break;
					}
					case ('b'): {
						characters.add(new CharacterToken("Mrs. Peacock", 'B',
								j, i));
						break;
					}
					case ('p'): {
						characters.add(new CharacterToken("Professor Plum",
								'P', j, i));
						break;
					}
					case ('m'): {
						characters.add(new CharacterToken("Colonel Mustard",
								'M', j, i));
						break;
					}
					case ('s'): {
						characters.add(new CharacterToken("Miss Scarlett", 'S',
								j, i));
						break;
					}
					default:
						break;
					}
				}
				i++;
			}
			parseMapScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return characters;
	}
}
