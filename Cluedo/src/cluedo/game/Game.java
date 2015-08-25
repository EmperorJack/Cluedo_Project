package cluedo.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import cluedo.game.ClockThread;
import cluedo.board.*;
import cluedo.cards.Card;
import cluedo.control.Controller;
import cluedo.actions.*;
import cluedo.tokens.CharacterToken;
import cluedo.view.*;

/**
 * Main cluedo class that handles the game logic.
 */
public class Game {

	// system fields
	private UI ui;
	private Board board;
	private Frame frame;
	private Controller controller;

	// player fields
	private int numberPlayers;
	private Player[] players;
	private Player currentPlayer;

	// game fields
	private Deck deck;
	private Dice dice;
	private int winner;
	private boolean rolled;
	private boolean moved;
	private boolean suggested;
	private boolean endTurn;
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
		// setup game systems
		dice = new Dice();
		board = new Board(WEAPONS, ROOMS, dice);
		ui = new UI(board);
		controller = new Controller(board, this);
		frame = new Frame(board, controller);
		winner = 0;

		// start the clock thread for continuous board updating
		ClockThread clk = new ClockThread(16, board, frame);
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
		dice = new Dice();
		board = new Board(WEAPONS, ROOMS, dice);
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
			currentPlayer = players[playerIndex];

			// if the current player has been eliminated
			if (currentPlayer.isEliminated()) {
				// skip their turn
				continue;
			}

			// check the current player is not the only player remaining
			if (playersRemaining() < 2) {
				// this player wins as they are the last remaining player
				winner = currentPlayer.getId();
				continue;
			}

			// get the room the player is in (null for no room)
			Room playerRoom = board.roomIn(currentPlayer.getToken());

			// update the board with the current player
			board.setPlayer(currentPlayer);
			
			// reset the dice and movement range from the last turn
			dice.resetValues();
			board.setValidTiles();

			// TODO Remove once GUI replaces text output, delete UI class
			System.out
					.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.printf("%s (%s)'s turn.\n", currentPlayer.getName(),
					currentPlayer.getCharacterName());
			// print player card info
			ui.printPlayerCardInfo(currentPlayer);

			// reset turn flags
			rolled = false;
			moved = false;
			suggested = false;
			endTurn = false;

			// enable the player to select another action until they end their
			// turn
			while (!endTurn) {
				// enable the buttons that can be used in the turn
				if (!moved) {
					// if the player has not moved yet
					if (!rolled)
						// if the dice has not been rolled yet
						frame.setButtonSelectable("rollDice", true);
					if (playerRoom != null && playerRoom.hasPassage()) {
						// if the player is in a room with secret passage
						frame.setButtonSelectable("secretPassage", true);
					}
				}
				// if the player has not made a suggestion yet
				if (!suggested && playerRoom != null) {
					// if the player is also in a room
					frame.setButtonSelectable("suggestion", true);
				}
				frame.setButtonSelectable("accusation", true);
				frame.setButtonSelectable("endTurn", true);

				// wait for the frame to get player input for selected action
				int actionSelected = frame.requestActionButtonInput();

				// disable all frame action buttons
				frame.setButtonSelectable("all", false);

				// if the player did not choose to end their turn
				if (actionSelected != 5) {
					// first create the action from the given selection
					Action action = createActionSelected(currentPlayer,
							playerRoom, actionSelected);

					// then perform the action if it is not null
					if (action != null) {
						performAction(currentPlayer, playerRoom, action);
					}

					// update the player room in case it changed
					playerRoom = board.roomIn(currentPlayer.getToken());
				} else {
					// player chose to end their turn
					endTurn = true;
				}
			}
			// disable all frame action buttons
			frame.setButtonSelectable("all", false);
		} while (winner == 0);

		// display the winner information and the solution
		frame.playerWinnerDialog(players[winner - 1], deck.getSolution());
	}

	/**
	 * Creates an action that can be performed given an index and the player who
	 * is making the action.
	 * 
	 * @param player
	 *            Player creating the action.
	 * @param playerRoom
	 *            The room the player is in, null if no room.
	 * @param actionSelected
	 *            The index of the action selected.
	 * @return The new created action.
	 */
	private Action createActionSelected(Player player, Room playerRoom,
			int actionSelected) {
		// process the selected action given by the player input
		if (actionSelected == 1) {
			// roll dice action selected

			// roll the dice
			dice.roll();

			// display the movement range on the board
			board.setValidTiles();

			// player has now rolled the dice
			rolled = true;
			return null;
		}

		// secret passage action selected
		if (actionSelected == 2) {
			// create a secret passage action to move the player
			return new SecretPassageAction(playerRoom, playerRoom.getPassage());
		}

		// suggestion action selected
		if (actionSelected == 3) {
			// setup a dialog box for the player to input their suggestion
			CardInputDialog dialog = new CardInputDialog(player, playerRoom,
					"Suggestion", deck);

			// wait for the dialog box to get player input
			dialog.requestInput();

			// create a suggestion action based on the dialog input
			SuggestionAction suggestion = new SuggestionAction(
					dialog.getCharacter(), dialog.getRoom(), dialog.getWeapon());

			// dispose of the dialog box
			dialog.dispose();

			// check if the window was closed before input confirmation given
			if (dialog.wasClosed()) {
				// don't return the action
				return null;
			}

			return suggestion;
		}

		// accusation action selected
		if (actionSelected == 4) {
			// check the player wants to make an accusation
			if (!frame.accusationConfirmDialog()) {
				return null;
			}

			// setup a dialog box for the player to input their suggestion
			CardInputDialog dialog = new CardInputDialog(player, playerRoom,
					"Accusation", deck);

			// wait for the dialog box to get player input
			dialog.requestInput();

			// create an accusation action based on the dialog input
			AccusationAction accusation = new AccusationAction(
					dialog.getCharacter(), dialog.getRoom(), dialog.getWeapon());

			// dispose of the dialog box
			dialog.dispose();

			// check if the window was closed before input confirmation given
			if (dialog.wasClosed()) {
				// don't return the action
				return null;
			}

			return accusation;
		}

		return null;
	}

	/**
	 * Given an action and a player the action will be executed and the game
	 * logic updated in fashion.
	 * 
	 * @param player
	 *            The player performing the action.
	 * @param playerRoom
	 *            The room the player is in, null if no room.
	 * @param action
	 *            The action being performed.
	 */
	private void performAction(Player player, Room playerRoom, Action action) {

		// if a secret passage action was chosen
		if (action instanceof SecretPassageAction) {
			// move the player via secret passage
			SecretPassageAction passageAction = (SecretPassageAction) action;
			board.moveTokenToRoom(player.getToken(),
					passageAction.getDestination());

			// the player has now moved
			moved = true;
			return;
		}

		// if an accusation action was chosen
		if (action instanceof AccusationAction) {
			// set winner to the result of the accusation
			// 1 means they won and 0 means the player was eliminated
			winner = performAccusation(player, (AccusationAction) action);

			// the player has now ended their turn
			endTurn = true;
			return;
		}

		// if a suggestion action was chosen
		if (action instanceof SuggestionAction) {
			// perform the suggestion action requested by the player
			performSuggestion(player, (SuggestionAction) action, playerRoom);

			// the player has now made a suggestion
			suggested = true;
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
	 * Called by the controller when the player clicks on the board. The given
	 * position will be used to compute a movement action the player would like
	 * to make.
	 * 
	 * @param x
	 *            The x position that was clicked.
	 * @param y
	 *            The y position that was clicked.
	 */
	public void triggerMove(int x, int y) {
		// if the player has not moved yet
		if (!moved) {
			// create a move action from the board
			MoveSequence move = board.triggerMove(x, y);

			// check the move action is valid
			if (move != null) {
				// the player has now moved
				moved = true;
			}
		}
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

			// construct a list of the accusation cards
			List<Card> accusationCards = new ArrayList<Card>();
			accusationCards.add(accusation.getCharacter());
			accusationCards.add(accusation.getRoom());
			accusationCards.add(accusation.getWeapon());

			// display the eliminated player information
			frame.playerEliminatedDialog(player, accusationCards);
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

		// move the suggested weapon token to the room
		String weapon = suggestion.getWeapon().toString();
		board.moveTokenToRoom(board.getWeaponToken(weapon), roomIn);

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
				frame.displayRefutedInfoDialog(player, players[i], refutedCard);

				// stop iterating through the players now
				return;
			}

			// move onto the next player
			i = (i + 1) % numberPlayers;
		}

		// print information that no suggested card was refuted
		frame.displayNonRefutedDialog(player);
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
			PlayerInputDialog dialog = new PlayerInputDialog(CHARACTERS,
					availableCharacters, board, i + 1);

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

			// check if the window was closed before input confirmation given
			if (dialog.wasClosed()) {
				// quit the game
				System.exit(0);
			}

			// give the player a copy of the deck for possible suggestion cards
			players[i].setNonRefutedCards(deck.getCharacters(),
					deck.getRooms(), deck.getWeapons());
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