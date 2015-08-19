package cluedo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

@SuppressWarnings("serial")
public class Frame extends JFrame {

	// frame fields
	private Canvas canvas;
	private JButton rollDiceButton, secretPassageButton, skipMovementButton,
			suggestionButton, accusationButton, endTurnButton;
	private int actionButtonSelected;

	public Frame(Board board) {
		super("Cluedo Game");

		// setup menu bar
		JMenuBar menuBar = new JMenuBar();

		// setup menu
		JMenu menu = new JMenu("Game");
		menu.getAccessibleContext().setAccessibleDescription(
				"The only menu in this program that has menu items");
		menuBar.add(menu);

		// setup restart menu item
		JMenuItem restartMenuItem = new JMenuItem("Restart");
		menu.add(restartMenuItem);

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
		canvas = new Canvas(board);
		setLayout(new BorderLayout());
		add(canvas, BorderLayout.CENTER);

		// setup panel with action buttons
		JPanel actionPanel = new JPanel();
		actionPanel.setLayout(new GridLayout(1, 0, 10, 10));
		actionPanel.add(Box.createVerticalStrut(5));

		// setup roll dice button
		rollDiceButton = new JButton("Roll Dice");
		rollDiceButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				actionButtonSelected = 1;
			}
		});
		actionPanel.add(rollDiceButton);

		// setup secret passage button
		secretPassageButton = new JButton("Secret Passage");
		secretPassageButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				actionButtonSelected = 2;
			}
		});
		actionPanel.add(secretPassageButton);

		// setup skip movement button
		skipMovementButton = new JButton("Skip Movement");
		skipMovementButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				actionButtonSelected = 3;
			}
		});
		actionPanel.add(skipMovementButton);

		// setup suggestion button
		suggestionButton = new JButton("Suggestion");
		suggestionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				actionButtonSelected = 4;
			}
		});
		actionPanel.add(suggestionButton);

		// setup accusation button
		accusationButton = new JButton("Accusation");
		accusationButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				actionButtonSelected = 5;
			}
		});
		actionPanel.add(accusationButton);

		// setup end turn button
		endTurnButton = new JButton("End Turn");
		endTurnButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				actionButtonSelected = 6;
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
		setSize(1280, 720);
		setResizable(true);
		setLocationRelativeTo(null);
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

	public int requestActionButtonInput() {
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
		return action;
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
			skipMovementButton.setEnabled(b);
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
		case "skipMovement":
			skipMovementButton.setEnabled(b);
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
}
