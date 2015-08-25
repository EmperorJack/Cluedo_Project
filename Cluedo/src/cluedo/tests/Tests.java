package cluedo.tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.*;

import cluedo.game.*;
import cluedo.tokens.CharacterToken;
import cluedo.board.Board;
import cluedo.board.Location;
import cluedo.board.Room;
import cluedo.cards.*;

public class Tests {

	@Test
	public void testDeckCorrectSize() {
		Deck deck = new Deck(Game.CHARACTERS, Game.ROOMS, Game.WEAPONS);
		assertTrue(deck.getDeck().size() == 21);
	}

	@Test
	public void testDeckCorrectAfterSolutionGenerated() {
		Deck deck = new Deck(Game.CHARACTERS, Game.ROOMS, Game.WEAPONS);
		deck.generateSolution();
		assertTrue(deck.getDeck().size() == 18);
	}

	@Test
	public void testSolutionContainsCharacterCard() {
		Deck deck = new Deck(Game.CHARACTERS, Game.ROOMS, Game.WEAPONS);
		deck.generateSolution();
		List<Card> solution = deck.getSolution();

		boolean containsCard = false;
		for (Card card : solution) {
			if (card instanceof CharacterCard) {
				containsCard = true;
			}
		}
		assertTrue(containsCard);
	}

	@Test
	public void testSolutionContainsRoomCard() {
		Deck deck = new Deck(Game.CHARACTERS, Game.ROOMS, Game.WEAPONS);
		deck.generateSolution();
		List<Card> solution = deck.getSolution();

		boolean containsCard = false;
		for (Card card : solution) {
			if (card instanceof RoomCard) {
				containsCard = true;
			}
		}
		assertTrue(containsCard);
	}

	@Test
	public void testSolutionContainsWeaponCard() {
		Deck deck = new Deck(Game.CHARACTERS, Game.ROOMS, Game.WEAPONS);
		deck.generateSolution();
		List<Card> solution = deck.getSolution();

		boolean containsCard = false;
		for (Card card : solution) {
			if (card instanceof WeaponCard) {
				containsCard = true;
			}
		}
		assertTrue(containsCard);
	}

	@Test
	public void testDeckContainsCard() {
		Deck deck = new Deck(Game.CHARACTERS, Game.ROOMS, Game.WEAPONS);
		List<Card> deckCards = deck.getDeck();
		assertTrue(deckCards.contains(new CharacterCard("Miss Scarlett")));
	}

	@Test
	public void testDeckDoesContainsCard() {
		Deck deck = new Deck(Game.CHARACTERS, Game.ROOMS, Game.WEAPONS);
		List<Card> deckCards = deck.getDeck();
		assertFalse(deckCards.contains(new CharacterCard("Miss Mustard")));
	}
	
	@Test
	public void testCardsEqual() {
		CharacterCard card1 = new CharacterCard("Miss Scarlett");
		CharacterCard card2 = new CharacterCard("Miss Scarlett");
		assertTrue(card1.equals(card2));
	}
	
	@Test
	public void testCardsDoNotEqual() {
		CharacterCard card1 = new CharacterCard("Miss Scarlett");
		CharacterCard card2 = new CharacterCard("Miss Mustard");
		assertFalse(card1.equals(card2));
	}
	
	@Test
	public void testDiceRollsValid() {
		Dice dice = new Dice();
		int i = 1000;
		while (0 < i) {
			dice.roll();
			int roll = dice.getResult();
			assertTrue(2 <= roll && roll <= 12);
			i--;
		}
	}
	
	@Test
	public void charTestOne() {
		Board b = new Board(Game.WEAPONS, Game.ROOMS, new Dice());
		CharacterToken missScarlett = b.getCharacterToken("Miss Scarlett");
		// getToken should not return null
		assertFalse(missScarlett == null);
		// getToken should return Miss Scarlett
		assertTrue(missScarlett.getName().equals("Miss Scarlett"));
	}

	@Test
	public void charTestTwo() {
		Board b = new Board(Game.WEAPONS, Game.ROOMS, new Dice());
		CharacterToken missPurp = b.getCharacterToken("Miss Purp");
		// getToken should return null
		assertTrue(missPurp == null);
	}

	@Test
	public void pathTestOne(){
		Board b = new Board(Game.WEAPONS, Game.ROOMS, new Dice());
		CharacterToken missScarlett = b.getCharacterToken("Miss Scarlett");
		//Path from Miss Scarlett to the Study
		assertEquals(b.calculatePathLength(missScarlett, new Location(5,19)), 8);
	}
	
	@Test
	public void pathTestTwo(){
		Board b = new Board(Game.WEAPONS, Game.ROOMS, new Dice());
		CharacterToken missScarlett = b.getCharacterToken("Miss Scarlett");
		//Shouldn't be able to find a path to a wallTile
		assertEquals(b.calculatePathLength(missScarlett, new Location(1,1)), -1);
	}
	
	@Test
	public void moveTestOne(){
		Board b = new Board(Game.WEAPONS, Game.ROOMS, new Dice());
		CharacterToken missScarlett = b.getCharacterToken("Miss Scarlett");
		//Miss Scarlett should move
		b.movePlayer(missScarlett, new Location(5,18));
		assertTrue(missScarlett.getLocation().equals(new Location(5, 18)));
	}
	
	@Test
	public void moveTestTwo(){
		Board b = new Board(Game.WEAPONS, Game.ROOMS, new Dice());
		CharacterToken missScarlett = b.getCharacterToken("Miss Scarlett");
		//Miss Scarlett shouldn't move
		b.movePlayer(missScarlett, new Location(1,1));
		assertTrue(missScarlett.getLocation().equals(new Location(6, 24)));
	}
	
	@Test
	public void moveTestThree(){
		Board b = new Board(Game.WEAPONS, Game.ROOMS, new Dice());
		CharacterToken missScarlett = b.getCharacterToken("Miss Scarlett");
		CharacterToken colMustard = b.getCharacterToken("Colonel Mustard");
		//Miss Scarlett shouldn't move
		b.movePlayer(missScarlett, colMustard.getLocation());
		assertTrue(missScarlett.getLocation().equals(new Location(6, 24)));
	}
	
	
	@Test
	public void roomTestOne(){
		Board b = new Board(Game.WEAPONS, Game.ROOMS, new Dice());
		CharacterToken missScarlett = b.getCharacterToken("Miss Scarlett");
		b.movePlayer(missScarlett, new Location(5,18));
		//Miss Scarlett shouldn't be in a room
		assertFalse(missScarlett.inRoom());
	}
	
	@Test
	public void roomTestTwo(){
		Board b = new Board(Game.WEAPONS, Game.ROOMS, new Dice());
		CharacterToken missScarlett = b.getCharacterToken("Miss Scarlett");
		b.movePlayer(missScarlett, new Location(5,19));
		//Miss Scarlett should be in a room
		assertTrue(missScarlett.inRoom());
	}
	
	@Test
	public void roomTestThree(){
		Board b = new Board(Game.WEAPONS, Game.ROOMS, new Dice());
		CharacterToken missScarlett = b.getCharacterToken("Miss Scarlett");
		b.movePlayer(missScarlett, new Location(5,19));
		Room r = missScarlett.getRoom();
		Room p = r.getPassage();
	//	b.moveTokenToRoom(missScarlett, p);
		//moved through passage
		assertTrue(missScarlett.getRoom().equals(p));
	}
}
