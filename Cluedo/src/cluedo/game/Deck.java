package cluedo.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cluedo.cards.*;

/**
 * Represents the cluedo deck of cards. Is constructed to hold the complete deck
 * and can perform operations such as generating solutions and dealing cards to
 * players.
 */
public class Deck {

	// deck fields
	private List<Card> deck;
	private List<Card> characterCards;
	private List<Card> roomCards;
	private List<Card> weaponCards;
	private List<Card> solution;

	/**
	 * Setup a new complete deck of shuffled cards.
	 * 
	 * @param characters
	 *            List of possible cluedo characters.
	 * @param rooms
	 *            List of possible cluedo rooms.
	 * @param weapons
	 *            List of possible cluedo weapons.
	 */
	public Deck(String[] characters, String[] rooms, String[] weapons) {
		// define a new deck of cards
		deck = new ArrayList<Card>();
		characterCards = new ArrayList<Card>();
		roomCards = new ArrayList<Card>();
		weaponCards = new ArrayList<Card>();

		// add character cards
		for (int i = 0; i < characters.length; i++) {
			CharacterCard character = new CharacterCard(characters[i]);
			deck.add(character);
			characterCards.add(character);
		}

		// add room cards
		for (int i = 0; i < rooms.length; i++) {
			RoomCard room = new RoomCard(rooms[i]);
			deck.add(room);
			roomCards.add(room);
		}

		// add weapon cards
		for (int i = 0; i < weapons.length; i++) {
			WeaponCard weapon = new WeaponCard(weapons[i]);
			deck.add(weapon);
			weaponCards.add(weapon);
		}

		// shuffle the complete deck of cards
		Collections.shuffle(deck);
	}

	/**
	 * Generate the solution cards or 'envelope' cards. One of each card type
	 * will be selected at random to create the solution (character, room and
	 * weapon cards). These cards will be removed from the deck.
	 */
	public void generateSolution() {
		// define a set of solution cards
		solution = new ArrayList<Card>();

		// pick a character card from the deck
		Card character = null;
		int i = 0;
		while (character == null) {
			Card next = deck.get(i++);
			if (next instanceof CharacterCard) {
				character = next;
				deck.remove(character);
			}
		}
		solution.add(character);

		// pick a room card from the deck
		Card room = null;
		i = 0;
		while (room == null) {
			Card next = deck.get(i++);
			if (next instanceof RoomCard) {
				room = next;
				deck.remove(room);
			}
		}
		solution.add(room);

		// pick a weapon card from the deck
		Card weapon = null;
		i = 0;
		while (weapon == null) {
			Card next = deck.get(i++);
			if (next instanceof WeaponCard) {
				weapon = next;
				deck.remove(weapon);
			}
		}
		solution.add(weapon);
	}

	/**
	 * Given a list of players will deal the deck of cards to each player
	 * iteratively. The solution cards will already have been removed at this
	 * point.
	 * 
	 * @param players
	 *            The list of player objects.
	 * @param numberPlayers
	 *            The number f players in the game.
	 */
	public void dealCards(Player[] players, int numberPlayers) {
		int i = 0;

		// while the deck still has cards remaining
		while (deck.size() > 0) {
			// deal a card to the next player
			players[i].addCard(deck.remove(0));
			i = (i + 1) % numberPlayers;
		}
	}

	// get methods below to return the deck data structures
	
	public List<Card> getDeck() {
		return deck;
	}

	public List<Card> getCharacters() {
		return characterCards;
	}
	
	public List<Card> getRooms() {
		return roomCards;
	}
	
	public List<Card> getWeapons() {
		return weaponCards;
	}

	public List<Card> getSolution() {
		return solution;
	}
}
