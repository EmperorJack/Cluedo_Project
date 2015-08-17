package cluedo.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import cluedo.board.Board;

@SuppressWarnings("serial")
public class Frame extends JFrame {

	JMenuBar menuBar;
	JMenu menu;
	JMenuItem restartMenuItem, quitMenuItem;

	private Canvas canvas;

	public Frame(Board board) {
		super("Cluedo Game");

		// setup menu bar
		menuBar = new JMenuBar();

		// setup menu
		menu = new JMenu("Game");
		menu.getAccessibleContext().setAccessibleDescription(
				"The only menu in this program that has menu items");
		menuBar.add(menu);

		// setup restart menu item
		restartMenuItem = new JMenuItem("Restart");
		menu.add(restartMenuItem);

		// setup quit menu item
		quitMenuItem = new JMenuItem("Quit");
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

		// setup close operation
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				quitRequestDialog();
			}
		});

		// pack components
		pack();
		setResizable(true);
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
		}
		return (int) input;
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
}
