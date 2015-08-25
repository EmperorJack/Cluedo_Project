package cluedo.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cluedo.cards.*;
import cluedo.tokens.CharacterToken;

/**
 * Represents a player for the cluedo game. Each player has a unique character
 * token and hand of cards. Has an eliminated state to determine if the player
 * is out of the game. Each player also has a set of non-refuted cards so they
 * know what cards are still possible to be the solution cards.
 */
public class Player {

	// player fields
	private CharacterToken character;
	private String name;
	private int id;
	private HashSet<Card> hand;
	private ArrayList<CharacterCard> nonRefutedCharacters;
	private ArrayList<RoomCard> nonRefutedRooms;
	private ArrayList<WeaponCard> nonRefutedWeapons;
	private boolean eliminated;
	public static final int HAND_X = 10;
	public static final int HAND_Y = 160;
	public static final int CARD_WD = 150;
	public static final int CARD_HT = 223;

	/**
	 * Setup a new player.
	 * 
	 * @param character
	 *            The unique character token the user chose.
	 * @param id
	 *            A unique id number.
	 */
	public Player(CharacterToken character, String name, int id) {
		this.character = character;
		this.name = name;
		this.id = id;
		hand = new HashSet<Card>();
		eliminated = false;
	}

	/**
	 * Add a given card to the player hand. Removes the card from the
	 * non-refuted cards as obviously the card cannot be in the solution
	 * envelope.
	 * 
	 * @param c
	 *            The card to add.
	 */
	public void addCard(Card c) {
		// add the given card to the player hand
		hand.add(c);

		// remove the card from possible suggestion cards
		nonRefutedCharacters.remove(c);
		nonRefutedRooms.remove(c);
		nonRefutedWeapons.remove(c);
	}

	/**
	 * Given lists of all possible cards will set the players sets of
	 * non-refuted cards to each list. Used in initial game setup, should be
	 * given a complete listing of the deck of cards.
	 * 
	 * @param characterCards
	 * @param roomCards
	 * @param weaponCards
	 */
	public void setNonRefutedCards(List<CharacterCard> characterCards,
			List<RoomCard> roomCards, List<WeaponCard> weaponCards) {
		nonRefutedCharacters = new ArrayList<CharacterCard>(characterCards);
		nonRefutedRooms = new ArrayList<RoomCard>(roomCards);
		nonRefutedWeapons = new ArrayList<WeaponCard>(weaponCards);
	}

	/**
	 * Given a card will remove it from the non-refuted card sets. The card has
	 * been ruled out as a possible solution.
	 * 
	 * @param c
	 *            The card to refute.
	 */
	public void refuteCard(Card c) {
		nonRefutedCharacters.remove(c);
		nonRefutedRooms.remove(c);
		nonRefutedWeapons.remove(c);
	}

	/**
	 * Tests to see if this player has been eliminated or not.
	 * 
	 * @return True if the player is eliminated.
	 */
	public boolean isEliminated() {
		return eliminated;
	}

	/**
	 * Eliminate this player from the game by changing the elminated state.
	 */
	public void eliminate() {
		eliminated = true;
	}

	public void draw(Graphics2D g2d) {
		g2d.drawImage(character.getPortrait(), 1318, 371, null);
		g2d.setFont(new Font("Arial", Font.PLAIN, 20));
		//http://www.coderanch.com/t/336616/GUI/java/Center-Align-text-drawString
		int stringLen = (int) g2d.getFontMetrics().getStringBounds(name, g2d).getWidth();
		int start = CARD_WD / 2 - stringLen / 2;
		g2d.setColor(Color.BLACK);
		g2d.drawString(name, start + 1318,622);
		int row = 0;
		int col = 0;
		int i = 0;
		for (Card card : hand) {
			row = i / 2;
			col = i % 2;
			g2d.drawImage(card.getImage(), HAND_X + col * CARD_WD, HAND_Y + row
					* CARD_HT, null);
			i++;
		}

	}

	// get methods below to return player fields

	public CharacterToken getToken() {
		return character;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public HashSet<Card> getHand() {
		return hand;
	}

	public ArrayList<CharacterCard> getNonRefutedCharacters() {
		return nonRefutedCharacters;
	}

	public ArrayList<RoomCard> getNonRefutedRooms() {
		return nonRefutedRooms;
	}

	public ArrayList<WeaponCard> getNonRefutedWeapons() {
		return nonRefutedWeapons;
	}

	public String getCharacterName() {
		return character.getName();
	}
}
