package cluedo.board;
import java.util.HashSet;

import cluedo.tiles.*;
import cluedo.tokens.*;

public class Room {
	String name;
	HashSet<DoorTile> entrances;
	HashSet<CharacterToken> characterTokens;
	HashSet<WeaponToken> weaponTokens;
	Room passage;
	
	
	/**
	 * A class storing information for a room on the board. A room can have multiple entrances and playing pieces,
	 * and may also have a secret passage to another room.
	 * @param name The name of this room.
	 */
	public Room(String name){
		this.name = name;
		this.entrances = new HashSet<DoorTile>();
		this.characterTokens = new HashSet<CharacterToken>();
		this.weaponTokens = new HashSet<WeaponToken>();
		passage = null;
	}
	
	/**
	 * Add a DoorTile to the list of entrances
	 * @param entrance A DoorTile that belongs to this room.
	 */
	public void addEntrance(DoorTile entrance){
		entrances.add(entrance);
	}
	
	/**
	 * Get the set of entrances.
	 * @return A set of entrances.
	 */
	public HashSet<DoorTile> getEntrances(){
		return entrances;
	}
	
	/**
	 * Adds a character to this room.
	 * @param t A CharacterToken to add.
	 */
	public void addToken(CharacterToken t){
		characterTokens.add(t);
	}
	
	/**
	 * Adds a weapon to this room.
	 * @param t A WeaponToken to add.
	 */
	public void addToken(WeaponToken t){
		weaponTokens.add(t);
	}
	
	/**
	 * Removes this character from the room.
	 * @param t CharacterToken to remove from the room.
	 * @return True if character successfully removed, false otherwise.
	 */
	public boolean removeToken (CharacterToken t){
		return characterTokens.remove(t);
	}
	
	/**
	 * Removes this weapon from the room.
	 * @param t WeaponToken to remove from the room.
	 * @return True if weapon successfully removed, false otherwise.
	 */
	public boolean removeToken (WeaponToken t){
		return weaponTokens.remove(t);
	}
	
	/**
	 * Checks if this room has a secret passage.
	 * @return True if room has a secret passage, false otherwise.
	 */
	public boolean hasPassage(){
		return (passage != null);
	}
	
	/**
	 * Returns the Room that the secret passage links to.
	 * @return Room on the other end of passage, null if none.
	 */
	public Room getPassage(){
		return passage;
	}
	
	/**
	 * Sets a Room at the other end of the secret passage.
	 * @param room Room on the other end of the passage.
	 */
	public void setPassage(Room room){
		this.passage = room;
	}
	
	/**
	 * Returns the name of the room.
	 * @return Name of the room.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returns a verbose list of the name and tokens in room, returns an empty String if the room is empty.
	 */
	public String toString() {
		if (!characterTokens.isEmpty() || !weaponTokens.isEmpty()) {
			String roomString = "";
			roomString += name + "\n";
			roomString += "-----\n";
			if (!characterTokens.isEmpty()) {
				roomString += "Characters: ";
				for (CharacterToken c : characterTokens) {
					roomString += c.getName() + ", ";
				}
				roomString = roomString.substring(0, roomString.length() - 2);
				roomString += "\n";
			}
			if (!weaponTokens.isEmpty()) {
				roomString += "Weapons: ";
				for (WeaponToken w : weaponTokens) {
					roomString += w.getName() + ", ";
				}
				roomString = roomString.substring(0, roomString.length() - 2);
				roomString += "\n";
			}
			return roomString;
		}
		return new String();
	}

}
