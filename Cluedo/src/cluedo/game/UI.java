package cluedo.game;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import cluedo.actions.*;
import cluedo.board.Board;
import cluedo.board.Room;
import cluedo.cards.*;
import cluedo.tiles.WallTile;
import cluedo.tokens.*;

/**
 * User interface class that performs all the input and output operations for
 * the cluedo game. All information that needs to be printed will be done here.
 * All information that the user must provide for the game to function will be
 * read in here.
 */
public class UI {

	// user interface fields
	private Scanner s = new Scanner(System.in);
	private Board board;

	/**
	 * Setup a new user interface instance.
	 * 
	 * @param board
	 *            The game board.
	 */
	public UI(Board board) {
		this.board = board;
	}

	/**
	 * Prompts the user to provide the number of players playing this game of
	 * cluedo.
	 * 
	 * @return The number of players determined by the user input.
	 */
	public int requestNumberPlayers() {
		int n = 0;

		// ask the user for a number until they give valid input
		while (n == 0) {

			// get the user input and check if it is of valid type and value
			boolean correctType = false;
			while (!correctType) {
				try {
					System.out
							.print("Please enter the number of players (3-6): ");
					n = s.nextInt();
					correctType = true;
				} catch (InputMismatchException ime) {
					System.out
							.println("Invalid input. Thats not a number! Try again.\n");
					s.next();
				}
			}
			if (n < 3 || 6 < n) {
				// invalid user input
				System.out
						.println("Invalid input. Only 3 to 6 players are allowed.\n");
				n = 0;
			}
		}

		return n;
	}

	/**
	 * Prompts the user to provide a character for the current player. They must
	 * make a selection from the given list of available characters, this is so
	 * each player has a unique character assigned.
	 * 
	 * @param characters
	 *            List of characters that have not been selected yet.
	 * @param playerNumber
	 *            The id of the player choosing a character.
	 * @return The character token determined by the user input.
	 */
	public CharacterToken requestCharacter(List<String> characters,
			int playerNumber) {
		int n = 0;
		System.out.printf("\nPlayer %d character select. ", playerNumber);

		// print out the list of available characters
		System.out.println("Available characters:");
		for (int i = 0; i < characters.size(); i++) {
			System.out.printf("%d: %s\n", i + 1, characters.get(i));
		}

		// ask the user for a character number until they give valid input
		while (n == 0) {

			// get the user input and check if it is of valid type and value
			boolean correctType = false;
			while (!correctType) {
				try {
					System.out
							.printf("Please enter the number of character you want (1-%d): ",
									characters.size());
					n = s.nextInt();
					correctType = true;
				} catch (InputMismatchException ime) {
					System.out
							.println("Invalid input. Thats not a number! Try again.\n");
					s.next();
				}
			}
			if (n < 1 || characters.size() < n) {
				// invalid user input
				System.out
						.println("Invalid input. Character does not exist.\n");
				n = 0;
			}
		}

		// return the corresponding character selected
		return board.getCharacterToken(characters.remove(n - 1));
	}

	/**
	 * Prompts the user to choose an action for the current player. The action
	 * can be of types: move, accuse or secret passage (depending on the room
	 * the player is in). Alternatively the player may choose to do nothing.
	 * THere is also an option to print the internal information of each room,
	 * this includes the character and weapon tokens in each room.
	 * 
	 * @param player
	 *            The player whose turn is involved.
	 * @param rollAmount
	 *            The value rolled by the simulated dice.
	 * @param roomIn
	 *            The room the character is in, if any.
	 * @return The action selected for the player determined by user input.
	 */
	public Action requestPlayerAction(Player player, int rollAmount, Room roomIn) {
		int selection = 0;

		// print player turn information
		System.out
				.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.printf("%s (%s)'s turn.\n", player.getName(),
				player.getCharacterName());

		// print player card info
		printPlayerCardInfo(player);

		// print the roll amount for the player
		System.out.printf("You've rolled a %d this turn!\n", rollAmount);

		// print out the list of available actions
		System.out.println("\nAvailable actions:");
		System.out.println("1: Nothing");
		System.out.println("2: Move");
		System.out.println("3: Accuse");
		System.out.println("4: Print Internal Room Information");

		// if the player is in a room with secret passage allow that action
		if (roomIn != null && roomIn.hasPassage()) {
			System.out.printf("5: Secret Passage (to %s)\n", roomIn
					.getPassage().getName());
		}

		// ask the user for an action number until they give valid input
		while (selection == 0) {

			// get the user input and check if it is of valid type and value
			boolean correctType = false;
			while (!correctType) {
				try {
					System.out.print("Your choice of action: ");
					selection = s.nextInt();
					correctType = true;
				} catch (InputMismatchException ime) {
					System.out
							.println("Invalid input. Thats not a number! Try again.\n");
					s.next();
				}
			}
			if (selection == 1) {
				// do nothing selected
				return null;
			} else if (selection == 2) {
				// move action selected
				return requestMove(player, rollAmount);
			} else if (selection == 3) {
				// accusation action selected
				Action accusation = requestAccusationYN(player);

				// return the accusation action if the player followed through
				// with the action after the warning message
				if (accusation != null) {
					return accusation;
				}

				// otherwise request a different action
			} else if (selection == 4) {
				// print the tokens that are contained within each room
				printRoomInfo();
			} else if (selection == 5 && roomIn != null && roomIn.hasPassage()) {
				// secret passage action selected
				return new SecretPassageAction(roomIn, roomIn.getPassage());
			}

			// print invalid action selected selected if it was an invalid input
			if (selection != 3 && selection != 4) {
				System.out.println("Invalid action selected! Try again.\n");
				selection = 0;
			} else {
				System.out.println();
			}
			selection = 0;
		}

		// will never be called
		return null;
	}

	/**
	 * Prompt the user for target location coordinates resulting in a valid move
	 * action.
	 * 
	 * @param player
	 *            The player who is moving.
	 * @param rollAmount
	 *            The value rolled by the simulated dice.
	 * @return The valid move action.
	 */
	public MoveAction requestMove(Player player, int rollAmount) {
		// print the current state of the game board
		System.out.println();
		printBoard();
		MoveAction move = null;

		// print a notifier if the player is in a room
		if (player.getToken().inRoom()) {
			System.out.printf("%s is in the %s.\n",
					player.getToken().getName(), player.getToken().getRoom()
							.getName());
		}

		// ask the user for a move location until they give valid input
		while (move == null) {

			// print the roll amount for the player
			System.out.printf("You've rolled %d! Where do you want to move?\n",
					rollAmount);

			// ask the user for a column until they give valid input
			int col = -1;
			while (col < 0) {
				System.out
						.printf("\nEnter the column you wish to go to (A-Y): ");

				// get the user input and check if it is valid
				String input = s.next();
				col = Character.getNumericValue(Character.toUpperCase(input
						.charAt(0))) - 10;

				// invalid user input
				if (col < 0 || col > 24) {
					System.out
							.printf("Invalid input, please enter a letter from A to Y.\n");
					col = -1;
				}
			}

			// ask the user for a row until they give valid input
			int row = -1;
			while (row < 0) {

				// get the user input and check if it is of valid type and value
				boolean correctType = false;
				while (!correctType) {
					try {
						System.out
								.printf("Enter the row you wish to go to (1-25): ");
						row = s.nextInt() - 1;
						correctType = true;
					} catch (InputMismatchException ime) {
						System.out
								.println("Invalid input. Thats not a number! Try again.\n");
						s.next();
					}
				}

				if (row < 0 || row > 24) {
					// invalid user input
					System.out
							.printf("Invalid input, please enter a number from 1 to 25.\n");
					row = -1;
				}
			}

			// create a new move action
			move = new MoveAction(col, row);

			// check to see if the target tile is valid
			if (board.getTile(move.getLocation()) instanceof WallTile) {
				// invalid target location, is a wall
				System.out
						.printf("\nInvalid tile! Please select a valid path or doorway tile to move to!\n");
				move = null;
			}

			if (move != null && board.hasCharacterOn(move.getLocation())) {
				// invalid target location, has other character on that tile
				System.out
						.printf("\nInvalid tile! Please select a tile without a character on it.\n");
				move = null;
			}

			// if the target tile was a valid move location
			if (move != null) {
				// calculate the length of the path
				int pathLength = board.calculatePathLength(player.getToken(),
						move.getLocation());

				// check to see if the roll amount is large enough for the path
				if (pathLength > rollAmount) {
					// dice roll not large enough for given path
					System.out
							.printf("That's too far away, you rolled a %d and that move would take at least %d steps!\n\n",
									rollAmount, pathLength);
					move = null;
				}
			}
		}

		// return the valid move action
		return move;
	}

	/**
	 * Prompts the user for a yes / no to whether they want to make an
	 * accusation or not, as doing so may eliminated them from the game.
	 * 
	 * @param player
	 *            The player performing the accusation.
	 * @return The accusation action if it was followed out, else null.
	 */
	public Action requestAccusationYN(Player player) {
		// confirm the player wants to make an accusation
		String answer = null;

		// ask the user for an answer until they give valid input
		while (answer == null) {
			System.out
					.println("\nWARNING: A false accusation will eliminate you from the game! On the contary, a right one will make you the winner!");
			System.out
					.print("Are you sure you want to make an accusation? (Y/N) ");

			// get the user input and check if it is valid
			answer = s.next().toLowerCase();
			if (!answer.equals("y") && !answer.equals("n")) {
				// invalid user input
				System.out
						.println("Invalid input. Only Y (yes) and N (no) are allowed.\n");
				answer = null;
			}
		}

		// if the player wants to make an accusation
		if (answer.equals("y")) {
			// request the player for their accusation cards
			return requestAccusation(player);
		}

		// player did not make an accusation
		return null;
	}

	/**
	 * Prompts the user for the three cards that make up an accusation
	 * (character, room and weapon).
	 * 
	 * @param player
	 *            The player performing the accusation.
	 * @return The accusation action created.
	 */
	public AccusationAction requestAccusation(Player player) {
		System.out
				.println("\nYou must choose any three cards for an accusation. (Character, Room and Weapon)");

		// print player card info
		printPlayerCardInfo(player);

		// request the player for the three cards required for an accusation
		CharacterCard character = requestCharacterCard();
		RoomCard room = requestRoomCard();
		WeaponCard weapon = requestWeaponCard();

		// return the valid accusation action
		return new AccusationAction(character, room, weapon);
	}

	/**
	 * Prompts the user for a yes / no to whether they want to make a suggestion
	 * or not. If yes then prompts the user for the two cards that make up a
	 * suggestion (character and weapon) as the room is already specified by
	 * where the player is.
	 * 
	 * @param player
	 *            The player performing the suggestion.
	 * @param roomIn
	 *            The room the player is currently in.
	 * @return The suggestion action created.
	 */
	public SuggestionAction requestSuggestion(Player player, Room roomIn) {
		// ask the player if they would like to make a suggestion
		String answer = null;

		// ask the user for an answer until they give valid input
		while (answer == null) {
			System.out.print("\nWould you like to make a suggestion? (Y/N) ");

			// get the user input and check if it is valid
			answer = s.next().toLowerCase();
			if (!answer.equals("y") && !answer.equals("n")) {
				// invalid user input
				System.out
						.println("Invalid input. Only Y (yes) and N (no) are allowed.\n");
				answer = null;
			}
		}

		// if the player wants to make a suggestion
		if (answer.equals("y")) {
			// print the player hand
			System.out
					.printf("\nThe suggestion room is the %s. You must choose any other two cards. (Character and Weapon)\n",
							roomIn.getName());

			// print player card info
			printPlayerCardInfo(player);

			// request the player for the two cards required for an accusation
			CharacterCard character = requestCharacterCard();
			WeaponCard weapon = requestWeaponCard();

			// return the valid suggestion action
			return new AccusationAction(character, new RoomCard(
					roomIn.getName()), weapon);
		}

		// player did not make a suggestion
		return null;
	}

	/**
	 * Prompts the player to choose a character card for accusation or
	 * suggestion actions.
	 * 
	 * @return The selected character card.
	 */
	public CharacterCard requestCharacterCard() {
		int n = 0;
		// print out the list of available characters
		System.out.println("\nCharacter Card select. Available characters:");
		for (int i = 0; i < Game.CHARACTERS.length; i++) {
			System.out.printf("%d: %s\n", i + 1, Game.CHARACTERS[i]);
		}

		// ask the user for a character number until they give valid input
		while (n == 0) {

			// get the user input and check if it is of valid type and value
			boolean correctType = false;
			while (!correctType) {
				try {
					System.out
							.printf("Please enter the number of the character you want to suggest / accuse (1-%d): ",
									Game.CHARACTERS.length);
					n = s.nextInt();
					correctType = true;
				} catch (InputMismatchException ime) {
					System.out
							.println("Invalid input. Thats not a number! Try again.\n");
					s.next();
				}
			}
			if (n < 1 || Game.CHARACTERS.length < n) {
				// invalid user input
				System.out
						.println("Invalid input. Character does not exist.\n");
				n = 0;
			}
		}

		// return the corresponding character selected
		return new CharacterCard(Game.CHARACTERS[n - 1]);
	}

	/**
	 * Prompts the player to choose a room card for accusation or suggestion
	 * actions.
	 * 
	 * @return The selected room card.
	 */
	public RoomCard requestRoomCard() {
		int n = 0;
		// print out the list of available room
		System.out.println("\nRoom Card select. Available rooms:");
		for (int i = 0; i < Game.ROOMS.length; i++) {
			System.out.printf("%d: %s\n", i + 1, Game.ROOMS[i]);
		}

		// ask the user for a room number until they give valid input
		while (n == 0) {

			// get the user input and check if it is of valid type and value
			boolean correctType = false;
			while (!correctType) {
				try {
					System.out
							.printf("Please enter the number of the room you want to suggest / accuse (1-%d): ",
									Game.ROOMS.length);
					n = s.nextInt();
					correctType = true;
				} catch (InputMismatchException ime) {
					System.out
							.println("Invalid input. Thats not a number! Try again.\n");
					s.next();
				}
			}
			if (n < 1 || Game.ROOMS.length < n) {
				// invalid user input
				System.out.println("Invalid input. Room does not exist.\n");
				n = 0;
			}
		}

		// return the corresponding room selected
		return new RoomCard(Game.ROOMS[n - 1]);
	}

	/**
	 * Prompts the player to choose a weapon card for accusation or suggestion
	 * actions.
	 * 
	 * @return The selected weapon card.
	 */
	public WeaponCard requestWeaponCard() {
		int n = 0;
		// print out the list of available weapons
		System.out.println("\nWeapon Card select. Available weapons:");
		for (int i = 0; i < Game.WEAPONS.length; i++) {
			System.out.printf("%d: %s\n", i + 1, Game.WEAPONS[i]);
		}

		// ask the user for a weapons number until they give valid input
		while (n == 0) {

			// get the user input and check if it is of valid type and value
			boolean correctType = false;
			while (!correctType) {
				try {
					System.out
							.printf("Please enter the number of the weapon you want to suggest / accuse (1-%d): ",
									Game.WEAPONS.length);
					n = s.nextInt();
					correctType = true;
				} catch (InputMismatchException ime) {
					System.out
							.println("Invalid input. Thats not a number! Try again.\n");
					s.next();
				}
			}
			if (n < 1 || Game.WEAPONS.length < n) {
				// invalid user input
				System.out.println("Invalid input. Weapon does not exist.\n");
				n = 0;
			}
		}

		// return the corresponding weapon selected
		return new WeaponCard(Game.WEAPONS[n - 1]);
	}

	/**
	 * Print the cluedo game board.
	 */
	public void printBoard() {
		System.out.println(board);
	}

	/**
	 * Print the internal info of each room. This includes which characters and
	 * weapon tokens are in each room.
	 */
	public void printRoomInfo() {
		System.out.println("\nInternal Room Information:");
		System.out.print(board.getRoomInfo());
	}

	/**
	 * Prints the given player card information. This included their hand and
	 * the list of non-refuted cards.
	 * 
	 * @param player
	 *            The player to print card information for.
	 */
	public void printPlayerCardInfo(Player player) {
		// print the player hand
		System.out.printf("Your cards are: %s\n", player.getHand());

		// print the known refuted suggestion cards for this player
		System.out.printf("Non-refuted possible suggestion cards are: %s\n",
				player.getNonRefutedCards());
	}

	/**
	 * Prints a notification that a character token was moved to a new room.
	 * This is due to a suggestion for the given token being called in another
	 * room.
	 * 
	 * @param token
	 *            The moved character token.
	 * @param destination
	 *            Where the token moved to.
	 */
	public void printCharacterMove(String token, String destination) {
		System.out.printf("\n%s has been moved to the %s.", token, destination);
	}

	/**
	 * Prints a notification that a weapon token was moved to a new room. This
	 * is due to a suggestion for the given token being called in another room.
	 * 
	 * @param token
	 *            The moved weapon token.
	 * @param destination
	 *            Where the token moved to.
	 */
	public void printWeaponMove(String token, String destination) {
		System.out.printf("\nThe %s weapon has been moved to the %s.", token,
				destination);
	}

	/**
	 * Prints a notification with the result of a suggestion action performed by
	 * a player. Prints the card that was refuted by another player.
	 * 
	 * @param refutingPlayer
	 *            The player who refuted another player's suggestion.
	 * @param refutedCard
	 *            The card that was refuted.
	 */
	public void printRefutedInfo(Player refutingPlayer, Card refutedCard) {
		// print that this player made a suggestion and a card was refuted
		System.out
				.printf("\nYou made a suggestion but the card [%s] was refuted by %s (Player %d)!\n",
						refutedCard, refutingPlayer.getCharacterName(),
						refutingPlayer.getId());
	}

	/**
	 * Prints a notification with the result of a suggestion action performed by
	 * a player. Prints the fact that none of the suggested cards were refuted.
	 */
	public void printNonRefuted() {
		// print that this player made a suggestion but no cards were refuted
		System.out
				.println("\nYou made a suggestion and no cards were refuted by the other players!");
	}

	/**
	 * Given a player announce them as the winner of the cluedo game!
	 * 
	 * @param player
	 *            The player who won.
	 * @param solution
	 *            The solution cards for this game of cluedo.
	 */
	public void printWinner(Player player, List<Card> solution) {
		// print out the winning player's id and character name
		System.out
				.printf("\n%s (Player %d) has won the game! Either by correct accusation or elimination of all other players!\n",
						player.getCharacterName(), player.getId());
		System.out.printf("The solution cards were: %s", solution.toString());
	}

	/**
	 * Given a player announce their status of elimination due to making a bad
	 * accusation.
	 * 
	 * @param player
	 *            The player who was eliminated.
	 */
	public void printPlayerElimination(Player player) {
		// print out the player's id and character name if they were elimated
		System.out
				.printf("\n%s (Player %d) has been eliminated from the game for making a bad accusation!\n",
						player.getCharacterName(), player.getId());
	}
}
