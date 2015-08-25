package cluedo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cluedo.board.Board;
import cluedo.cards.Card;
import cluedo.control.Controller;
import cluedo.game.Player;

/**
 * The main swing component for the cluedo game. Creates a GUI frame that
 * contains a menu, canvas for drawing / player movement and action buttons for
 * player interaction. Utilizes a key listener to allow the player to use
 * shortcut keys instead of using the action buttons.
 */
@SuppressWarnings("serial")
public class Frame extends JFrame implements KeyListener {

	// frame fields
	private Canvas canvas;
	private JButton rollDiceButton, secretPassageButton, suggestionButton,
			accusationButton, endTurnButton;
	private int actionButtonSelected;
	private boolean awaitingInput;

	/**
	 * Setup a new frame.
	 * 
	 * @param board
	 *            The game board.
	 * @param controller
	 *            The mouse controller.
	 */
	public Frame(Board board, Controller controller) {
		super("Cluedo Game");

		// setup menu bar
		JMenuBar menuBar = new JMenuBar();

		// setup menu
		JMenu menu = new JMenu("Game");
		menuBar.add(menu);

		// setup quit menu item
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				quitRequestDialog();
			}
		});
		menu.add(quitMenuItem);
		setJMenuBar(menuBar);

		// setup canvas with center border layout
		canvas = new Canvas(board, controller);
		canvas.setSize(988, 985);
		setLayout(new BorderLayout());
		add(canvas, BorderLayout.CENTER);

		// setup panel with action buttons
		JPanel actionPanel = new JPanel();
		actionPanel.setLayout(new GridLayout(1, 0, 10, 10));
		actionPanel.add(Box.createVerticalStrut(5));

		// setup roll dice button
		rollDiceButton = new JButton("Roll Dice (R)");
		rollDiceButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				actionButtonSelected = 1;
			}
		});
		actionPanel.add(rollDiceButton);

		// setup secret passage button
		secretPassageButton = new JButton("Secret Passage (P)");
		secretPassageButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				actionButtonSelected = 2;
			}
		});
		actionPanel.add(secretPassageButton);

		// setup suggestion button
		suggestionButton = new JButton("Suggestion (S)");
		suggestionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				actionButtonSelected = 3;
			}
		});
		actionPanel.add(suggestionButton);

		// setup accusation button
		accusationButton = new JButton("Accusation (A)");
		accusationButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				actionButtonSelected = 4;
			}
		});
		actionPanel.add(accusationButton);

		// setup end turn button
		endTurnButton = new JButton("End Turn (E)");
		endTurnButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				actionButtonSelected = 5;
			}
		});
		actionPanel.add(endTurnButton);

		actionPanel.add(Box.createVerticalStrut(5));
		setButtonSelectable("all", false);

		// setup close operation
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				quitRequestDialog();
			}
		});

		// finish setting up the frame attributes
		actionButtonSelected = 0;
		actionPanel.setPreferredSize(new Dimension(0, 80));
		actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(actionPanel, BorderLayout.SOUTH);
		pack();
		setSize(1000, 1000);
		setResizable(true);
		setLocationRelativeTo(null);

		// setup key listener
		addKeyListener(this);
		setFocusable(true);
	}

	public void update() {
		canvas.repaint();
	}

	/**
	 * Prompts the user to provide the number of players playing this game of
	 * cluedo.
	 * 
	 * @return The number of players determined by the user input.
	 */
	public int numberPlayersRequestDialog() {
		// user choices available
		Object[] choices = { 3, 4, 5, 6 };

		// get user input from a dialog box with a drop down menu
		Object input = JOptionPane.showInputDialog(null,
				"Please enter the number of players", "Cluedo Game Setup",
				JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);

		// check a valid input was selected before returning
		if (input == null) {
			// dialog box was closed
			System.exit(0);

			// will not be reached
			return 0;
		} else {
			return (int) input;
		}
	}

	/**
	 * Awaits the player to give an action input via button or shortcut key.
	 * Keeps the main thread isolated and will return to the caller only once
	 * input has been made.
	 * 
	 * @return
	 */
	public int requestActionButtonInput() {
		// indicate the frame is waiting for player action input
		awaitingInput = true;

		// while an action button has not been pressed
		while (actionButtonSelected == 0) {
			try {
				// wait a bit
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// a thread interrupted exception occurred
				e.printStackTrace();
			}
		}

		// copy the selected action to a temporary variable
		int action = actionButtonSelected;

		// reset the selected action and return the temporary variable
		actionButtonSelected = 0;
		awaitingInput = false;
		return action;
	}

	public boolean accusationConfirmDialog() {
		// get user input from a dialog box
		int reply = JOptionPane.showConfirmDialog(null,
				"A bad accusation will eliminate you from the game.\n"
						+ "A correct one will make you the winner!\n"
						+ "Are you sure you want to make an accusation?",
				"Accusation Confirmation", JOptionPane.YES_NO_OPTION);

		// quit if the yes reply was selected
		if (reply == JOptionPane.YES_OPTION) {
			return true;
		}

		return false;
	}

	/**
	 * Display a message about the result of a suggestion action. The result
	 * being a card was refuted.
	 * 
	 * @param player
	 *            The player who made the suggestion.
	 * @param refutingPlayer
	 *            The player who refuted a suggestion card.
	 * @param refutedCard
	 *            The card that was refuted.
	 */
	public void displayRefutedInfoDialog(Player player, Player refutingPlayer,
			Card refutedCard) {
		String output = String
				.format("%s (%s) made a suggestion but the card [%s] was refuted by %s (%s)!",
						player.getName(), player.getCharacterName(),
						refutedCard, refutingPlayer.getName(),
						refutingPlayer.getCharacterName());

		// display a message dialog with one option to continue
		Object[] options = { "Ok" };
		JOptionPane.showOptionDialog(null, output, "Suggestion Result",
				JOptionPane.PLAIN_MESSAGE, JOptionPane.QUESTION_MESSAGE, null,
				options, options[0]);
	}

	/**
	 * Display a message about the result of a suggestion action. The result
	 * being no cards were refuted.
	 * 
	 * @param player
	 *            The player who made the suggestion.
	 */
	public void displayNonRefutedDialog(Player player) {
		String output = String
				.format("%s (%s) made a suggestion and no cards were refuted by the other players!",
						player.getName(), player.getCharacterName());

		// display a message dialog with one option to continue
		Object[] options = { "Ok" };
		JOptionPane.showOptionDialog(null, output, "Suggestion Result",
				JOptionPane.PLAIN_MESSAGE, JOptionPane.QUESTION_MESSAGE, null,
				options, options[0]);
	}

	/**
	 * Display a message about the winner in a dialog box. Once closed the game
	 * will exit.
	 * 
	 * @param player
	 *            The winning player.
	 * @param solution
	 *            The solution cards.
	 */
	public void playerWinnerDialog(Player player, List<Card> solution) {
		// construct a string with the player name, character name and solution
		// cards
		String output = String
				.format("%s (%s) has won the game! Either by correct accusation or elimination of all other players!\n"
						+ "The solution cards were: %s", player.getName(),
						player.getCharacterName(), solution.toString());

		// display a message dialog with one option to quit
		Object[] options = { "Quit" };
		JOptionPane.showOptionDialog(null, output, "Game Won!",
				JOptionPane.PLAIN_MESSAGE, JOptionPane.QUESTION_MESSAGE, null,
				options, options[0]);

		// quit the game
		System.exit(0);
	}

	/**
	 * Display a message about an eliminated player in a dialog box.
	 * 
	 * @param player
	 *            The eliminated player.
	 * @param accusation
	 *            The incorrect cards the player accused.
	 */
	public void playerEliminatedDialog(Player player, List<Card> accusation) {
		// construct a string with the player name, character name and incorrect
		// accusation cards
		String output = String.format(
				"%s (%s) has been eliminated from the game for making a bad accusation!\n"
						+ "The incorrect accusation cards were: %s",
				player.getName(), player.getCharacterName(),
				accusation.toString());

		// display a message dialog with one option to continue
		Object[] options = { "Ok" };
		JOptionPane.showOptionDialog(null, output, "Player Eliminated",
				JOptionPane.PLAIN_MESSAGE, JOptionPane.QUESTION_MESSAGE, null,
				options, options[0]);
	}

	/**
	 * Prompts the user to confirm they want to quit the game.
	 */
	public void quitRequestDialog() {
		// get user input from a dialog box
		int reply = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to quit?", "Quit Confirmation",
				JOptionPane.YES_NO_OPTION);

		// quit if the yes reply was selected
		if (reply == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	public void setButtonSelectable(String button, boolean b) {
		switch (button) {
		case "all":
			rollDiceButton.setEnabled(b);
			secretPassageButton.setEnabled(b);
			suggestionButton.setEnabled(b);
			accusationButton.setEnabled(b);
			endTurnButton.setEnabled(b);
			break;
		case "rollDice":
			rollDiceButton.setEnabled(b);
			break;
		case "secretPassage":
			secretPassageButton.setEnabled(b);
			break;
		case "suggestion":
			suggestionButton.setEnabled(b);
			break;
		case "accusation":
			accusationButton.setEnabled(b);
			break;
		case "endTurn":
			endTurnButton.setEnabled(b);
			break;
		default:
			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// only allow shortcut key presses if the frame is awaiting player input
		if (awaitingInput) {
			// shortcut keys will only activate if the relative button is
			// enabled
			if (e.getKeyCode() == KeyEvent.VK_R && rollDiceButton.isEnabled()) {
				// R key pressed for roll dice
				actionButtonSelected = 1;
			} else if (e.getKeyCode() == KeyEvent.VK_P
					&& secretPassageButton.isEnabled()) {
				// P key pressed for secret passage
				actionButtonSelected = 2;
			} else if (e.getKeyCode() == KeyEvent.VK_S
					&& suggestionButton.isEnabled()) {
				// S key pressed for suggestion
				actionButtonSelected = 3;
			} else if (e.getKeyCode() == KeyEvent.VK_A
					&& accusationButton.isEnabled()) {
				// A key pressed for accusation
				actionButtonSelected = 4;
			} else if (e.getKeyCode() == KeyEvent.VK_E
					&& endTurnButton.isEnabled()) {
				// E key pressed for end turn
				actionButtonSelected = 5;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// unused method
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// unused method
	}
}