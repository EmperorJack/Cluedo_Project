package cluedo.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import cluedo.game.ClockThread;
import cluedo.board.Board;
import cluedo.board.Location;
import cluedo.board.Room;
import cluedo.cards.Card;
import cluedo.actions.*;
import cluedo.tokens.CharacterToken;
import cluedo.view.CardInputDialog;
import cluedo.view.PlayerInputDialog;
import cluedo.view.Frame;

/**
 * Main cluedo class that handles the game logic.
 */
public class Game {

	// system fields
	private UI ui;
	private Board board;
	private Frame frame;

	// player fields
	private int numberPlayers;
	private Player[] players;

	// game fields
	private Deck deck;
	private Dice dice;
	private int winner;
	public static final String[] CHARACTERS = { "Miss Scarlett",
			"Colonel Mustard", "Mrs. White", "The Reverend Green",
			"Mrs. Peacock", "Professor Plum" };
	public static final String[] ROOMS = { "Kitchen", "Ballroom",
			"Conservatory", "Billiard Room", "Library", "Study", "Hall",
			"Lounge", "Dining Room" };
	public static final String[] WEAPONS = { "Candlestick", "Dagger",
			"Lead Pipe", "Revolver", "Rope", "Spanner" };

	/**
	 * Setup a new game of Cluedo.
	 */
	public Game() {
		// setup system
		board = new Board(WEAPONS, ROOMS);
		ui = new UI(board);
		frame = new Frame(board);
		dice = new Dice();
		winner = 0;

		// start the clock thread for continuous board updating
		ClockThread clk = new ClockThread(1, board, frame);
		clk.start();

		// generate a new complete deck
		deck = new Deck(CHARACTERS, ROOMS, WEAPONS);

		// request the user for the number of users playing
		numberPlayers = frame.numberPlayersRequestDialog();

		// create new players each with a unique character token
		players = setupPlayers();

		// generate the solution cards
		deck.generateSolution();

		// deal the remaining cards to the players
		deck.dealCards(players, numberPlayers);

		// display the frame
		frame.setVisible(true);
	}

	/**
	 * Setup a new Game of Cluedo with no UI input. This is used exclusively for
	 * JUnit Testing.
	 */
	public Game(String test) {
		// setup system
		board = new Board(WEAPONS, ROOMS);
		ui = new UI(board);

	}

	/**
	 * Main game logic loop
	 */
	private void gameLoop() {
		// current player
		int playerIndex = -1;

		// loop until a player wins or the game exits
		do {
			// move onto the next player
			playerIndex = (playerIndex + 1) % numberPlayers;
			Player player = players[playerIndex];

			// if the current player has been eliminated
			if (player.isEliminated()) {
				// skip their turn
				continue;
			}

			// check the current player is not the only player remaining
			if (playersRemaining() < 2) {
				// this player wins as they are the last remaining player
				winner = player.getId();
				continue;
			}

			// get the room the player is in (null for no room)
			Room playerRoom = board.roomIn(player.getToken());

			// TODO Remove once GUI replaces text output
			System.out
					.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.printf("%s (%s)'s turn.\n", player.getName(),
					player.getCharacterName());

			// first player action turn stage (movement or accusation)

			// enable the buttons that can be used in the first turn stage
			frame.setButtonSelectable("rollDice", true);
			if (playerRoom != null && playerRoom.hasPassage()) {
				// if the player is in a room with secret passage
				frame.setButtonSelectable("secretPassage", true);
			}
			frame.setButtonSelectable("skipMovement", true);
			frame.setButtonSelectable("accusation", true);

			// wait for the frame to get player input for selected action
			int actionSelected = frame.requestActionButtonInput();

			// if the player did not choose to skip moving their character
			if (actionSelected != 3) {
				// first create the action from the given selection
				Action action = createActionSelected(player, playerRoom,
						actionSelected);

				// then perform the action
				performAction(player, playerRoom, action);

				// update the player room in case it changed
				playerRoom = board.roomIn(player.getToken());
			}

			// disable all frame action buttons
			frame.setButtonSelectable("all", false);

			// check if the player has been eliminated or became the winner
			if (winner != 0 || player.isEliminated()) {
				// skip the second action stage of the player turn
				continue;
			}

			// second player action turn stage (suggestion or accusation)

			// enable the buttons that can be used in the second turn stage
			if (playerRoom != null) {
				// if the player is in a room
				frame.setButtonSelectable("suggestion", true);
			}
			frame.setButtonSelectable("accusation", true);
			frame.setButtonSelectable("endTurn", true);

			// wait for the frame to get player input for selected action
			actionSelected = frame.requestActionButtonInput();

			// if the player did not choose to end their turn
			if (actionSelected != 6) {
				// first create the action from the given selection
				Action action = createActionSelected(player, playerRoom,
						actionSelected);

				// then perform the action
				performAction(player, playerRoom, action);
			}

			// disable all frame action buttons
			frame.setButtonSelectable("all", false);
		} while (winner == 0);

		// print the winner information and the solution
		ui.printWinner(players[winner - 1], deck.getSolution());
	}

	private Action createActionSelected(Player player, Room playerRoom,
			int actionSelected) {
		// process the selected action given by the player input
		if (actionSelected == 1) {
			// roll dice action selected

			// roll the dice
			dice.roll();

			// get the value of the rolled dice
			int rollAmount = dice.getResult();
			System.out.println(rollAmount);

			// TODO allowing mouse to select move target should start here
			return ui.requestMove(player, rollAmount);
		}

		if (actionSelected == 2 && playerRoom != null) {
			// secret passage action selected
			return new SecretPassageAction(playerRoom, playerRoom.getPassage());
		}

		// suggestion action selected
		if (actionSelected == 4 && playerRoom != null) {
			// setup a dialog box for the player to input their suggested
			CardInputDialog dialog = new CardInputDialog(player, playerRoom,
					"Suggestion");

			// wait for the dialog box to get player input
			dialog.requestInput();
			
			// create a suggestion action based on the dialog input
			SuggestionAction suggestion = new SuggestionAction(
					dialog.getCharacter(), dialog.getRoom(), dialog.getWeapon());

			// dispose of the dialog box
			dialog.dispose();

			return suggestion;
		}

		// accusation action selected
		if (actionSelected == 5) {
			// check the player wants to make an accusation
			Action accusation = ui.requestAccusationYN(player);

			// return the accusation action if the player followed through
			// with the action after the warning message
			if (accusation != null) {
				// TODO replace with accusation dialog
				return accusation;
			}
		}
		
		return null;
	}

	private void performAction(Player player, Room playerRoom, Action action) {
		// if a valid move action was chosen
		if (action instanceof MoveAction) {
			// move the player and print the board result
			Location loc = ((MoveAction) action).getLocation();
			board.movePlayer(player.getToken(), loc);
			ui.printBoard();
			return;
		}

		// if a secret passage action was chosen
		if (action instanceof SecretPassageAction) {
			// move the player via secret passage
			SecretPassageAction passageAction = (SecretPassageAction) action;
			board.moveTokenToRoom(player.getToken(),
					passageAction.getDestination());
			return;
		}

		// if an accusation action was chosen
		if (action instanceof AccusationAction) {
			// set winner to the result of the accusation
			// 1 means they won and 0 means the player was eliminated
			winner = performAccusation(player, (AccusationAction) action);
			return;
		}

		// if a suggestion action was chosen
		if (action instanceof SuggestionAction) {
			// perform the suggestion action requested by the player
			performSuggestion(player, (SuggestionAction) action, playerRoom);
			return;
		}
	}

	/**
	 * Returns the number of players in the game who have not been eliminated.
	 * 
	 * @return The number of players remaining.
	 */
	private int playersRemaining() {
		int count = 0;
		for (int i = 0; i < numberPlayers; i++) {
			if (!players[i].isEliminated()) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Given a player and accusation action, perform the accusation such that if
	 * the accusation was correct (the accusation cards match the solution
	 * cards) the player will win the game. If not, eliminate the player from
	 * the game.
	 * 
	 * @param player
	 *            The player performing the accusation.
	 * @param accusation
	 *            The accusation action made up of a character, room and weapon.
	 * @return Player ID if they won, 0 if they were eliminated.
	 */
	private int performAccusation(Player player, AccusationAction accusation) {
		// test the accusation
		if (testAccusation(accusation)) {
			// the accusation passed and the player wins
			return player.getId();
		} else {
			// player is eliminated due to bad accusation
			player.eliminate();
			ui.printPlayerElimination(player);
		}

		// no winner was decided
		return 0;
	}

	/**
	 * Test the given accusation matches the solution cards.
	 * 
	 * @param accusation
	 *            The accusation action made up of a character, room and weapon.
	 * @return True if it matched, false if not.
	 */
	private boolean testAccusation(AccusationAction accusation) {
		// check the accusation action contains the same cards as the solution
		return (deck.getSolution().contains(accusation.getCharacter())
				&& deck.getSolution().contains(accusation.getRoom()) && deck
				.getSolution().contains(accusation.getWeapon()));
	}

	/**
	 * Given a player and suggestion action, perform the suggestion. Iterate
	 * clockwise through the other players and if one of the other players has
	 * one of the suggestion cards they will refute the player suggestion.
	 * Showing one of the cards that matched at random.
	 * 
	 * @param player
	 *            The player performing the suggestion.
	 * @param suggestion
	 *            The suggestion action made up of a character, room and weapon.
	 * @param roomIn
	 *            The room the player is currently in.
	 */
	private void performSuggestion(Player player, SuggestionAction suggestion,
			Room roomIn) {
		Card refutedCard;

		// move the suggested character token to the room
		String character = suggestion.getCharacter().toString();
		board.moveTokenToRoom(board.getCharacterToken(character), roomIn);
		ui.printCharacterMove(character, roomIn.getName());

		// move the suggested weapon token to the room
		String weapon = suggestion.getWeapon().toString();
		board.moveTokenToRoom(board.getWeaponToken(weapon), roomIn);
		ui.printWeaponMove(weapon, roomIn.getName());

		// iterate through all the other players clockwise
		int i = player.getId() - 1;
		i = (i + 1) % numberPlayers;
		while (i != (player.getId() - 1)) {
			// compare the suggestion and current player hand
			refutedCard = checkForRefute(suggestion, players[i].getHand());

			// if a suggested card was refuted by the current player
			if (refutedCard != null) {
				// forget the card that was refuted
				player.refuteCard(refutedCard);

				// print information that a suggested card was refuted
				ui.printRefutedInfo(players[i], refutedCard);

				// stop iterating through the players now
				return;
			}

			// move onto the next player
			i = (i + 1) % numberPlayers;
		}

		// print information that no suggested card was refuted
		ui.printNonRefuted();
	}

	/**
	 * Check if the given player hand contains any of the suggested cards. If
	 * there is more than one matching cards, return one at random.
	 * 
	 * @param suggestion
	 *            The given suggestion action.
	 * @param hand
	 *            The player hand to check against.
	 * @return A card that intersects the hand and suggestion.
	 */
	private Card checkForRefute(SuggestionAction suggestion, HashSet<Card> hand) {
		// build a list of cards that intersect the suggestion and the hand
		List<Card> matches = new ArrayList<Card>();

		// check the character suggestion
		if (hand.contains(suggestion.getCharacter())) {
			matches.add(suggestion.getCharacter());
		}

		// check the room suggestion
		if (hand.contains(suggestion.getRoom())) {
			matches.add(suggestion.getRoom());
		}

		// check the weapon suggestion
		if (hand.contains(suggestion.getWeapon())) {
			matches.add(suggestion.getWeapon());
		}

		// if there was intersecting cards return one of them at random
		if (matches.size() > 0) {
			return matches.get((int) (Math.random() * matches.size()));
		}

		// no intersecting cards were found
		return null;
	}

	/**
	 * Setup the player objects for the game. Each player will have a unique
	 * character associated with it. Each player will be initialized with a list
	 * of possible cards that can be used for suggestions, refuted cards will be
	 * removed from this list.
	 * 
	 * @param possibleCards
	 *            All possible cards available in the game of cluedo.
	 * @return The new list of players.
	 */
	private Player[] setupPlayers() {
		// create a list of players with size given by the number of players
		players = new Player[numberPlayers];

		// generate the available characters for token selection
		List<String> availableCharacters = new ArrayList<String>();
		availableCharacters.addAll(Arrays.asList(CHARACTERS));

		// for each player
		for (int i = 0; i < numberPlayers; i++) {
			// setup a dialog box for the player to input a name and character
			PlayerInputDialog dialog = new PlayerInputDialog(
					availableCharacters, i + 1);

			// wait for the dialog box to get player input
			dialog.requestInput();

			// setup a new player with the name and character from the dialog
			String name = dialog.getNameInput();
			CharacterToken token = board.getCharacterToken(dialog
					.getSelectedCharacter());

			// check the name is not an empty string
			if (name.isEmpty()) {
				// use a default name instead
				name = "Player " + (i + 1);
			}
			players[i] = new Player(token, name, i + 1);

			// remove the chosen character from the list of available characters
			availableCharacters.remove(dialog.getSelectedCharacter());

			// dispose of the dialog box
			dialog.dispose();

			// give the player a copy of the deck for possible suggestion cards
			players[i].setNonRefutedCards(deck.getCharacters(), deck.getRooms(), deck.getWeapons());
		}
		return players;
	}

	/**
	 * Returns the game board. This is used exclusively for JUnit Testing.
	 * 
	 * @return Game board.
	 */
	public Board getBoard() {
		return board;
	}

	public static void main(String[] args) {
		// setup a new game
		Game game = new Game();

		// start the game
		game.gameLoop();
	}
}
