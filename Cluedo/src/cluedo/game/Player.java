package cluedo.game;

import java.awt.Graphics;
import java.util.HashSet;
import java.util.List;

import cluedo.cards.Card;
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
	private int id;
	private HashSet<Card> hand;
	private HashSet<Card> nonRefutedCards;
	private boolean eliminated;

	/**
	 * Setup a new player.
	 * 
	 * @param character
	 *            The unique character token the user chose.
	 * @param id
	 *            A unique id number.
	 */
	public Player(CharacterToken character, int id) {
		this.character = character;
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
		nonRefutedCards.remove(c);
	}

	/**
	 * Given a list of cards will set the players set of non-refuted cards to
	 * the list. Used in initial game setup, should be given a complete listing
	 * of the deck of cards.
	 * 
	 * @param cards
	 *            The list of cards to set the non-refuted cards too.
	 */
	public void setNonRefutedCards(List<Card> cards) {
		nonRefutedCards = new HashSet<Card>(cards);
	}

	/**
	 * Given a card will remove it from the non-refuted cards. The card has been
	 * ruled out as a possible solution.
	 * 
	 * @param c
	 *            The card to refute.
	 */
	public void refuteCard(Card c) {
		nonRefutedCards.remove(c);
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
	
	public void draw(Graphics g) {
		
	}

	// get methods below to return player fields
	
	public CharacterToken getToken() {
		return character;
	}

	public int getId() {
		return id;
	}

	public HashSet<Card> getHand() {
		return hand;
	}

	public HashSet<Card> getNonRefutedCards() {
		return nonRefutedCards;
	}

	public String getCharacterName() {
		return character.getName();
	}
}
