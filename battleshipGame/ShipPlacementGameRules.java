package battleshipGame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ShipPlacementGameRules implements ActionListener{

	private JFrame frame;
	private JPanel panel;
	private JButton GuessingStageRules;

	public ShipPlacementGameRules(){

		// Uses original button image as example
		ImageIcon button = new ImageIcon("Cruiser.png");
		JLabel buttonExample = new JLabel(button);
		buttonExample.setBounds(30, 200, 150, 60);
		
		// Uses original grid as example
		ImageIcon grid = new ImageIcon("Example.png");
		JLabel gridExample = new JLabel(grid);
		gridExample.setBounds(30, 360, 150, 112);
		
		// Explain the game rules
		JLabel explainRules = new JLabel("<html>HOW TO PLACE SHIPS:"
				+ "<br/>1. Choose the name of the ship you want to place."
				+ "<br/><br/><br/>2. Then choose the direction using the vertical/horizontal button."
				+ "<br/><br/><br/>3. Lastly click on the square you want to place the head of the ship at."
				+ "<br/>"
				+ "<br/>P.S: You must always go in this order to move/delete a chosen ship."
				+ "<br/>The ships can be placed horizontally or vertically, but they cannot overlap or be adjacent to each other."
				+ "<br/>Once multiple ships have been placed, the currently chosen ship "
				+ "will be highlighted green to visually inform the user.", SwingConstants.CENTER);
		explainRules.setFont(new Font("Monospaced", Font.BOLD, 15));
		explainRules.setBounds(210, 200, 500, 375);
		
		// Button leading to Guessing Stage rules
		GuessingStageRules = new JButton("Read Guessing Stage Rules");
		GuessingStageRules.setBounds(320, 580, 260, 40);
		GuessingStageRules.setForeground(Color.BLACK);
		GuessingStageRules.setBackground(Color.WHITE);
		GuessingStageRules.setFont(new Font("Monospaced", Font.BOLD, 12));
		GuessingStageRules.setFocusable(false);
		GuessingStageRules.addActionListener(this);
		
		// Uses original button of rotate boat as example
		JButton rotateBoatExample = new JButton("Vertical");
		rotateBoatExample.setBounds(30, 280, 160, 40);
		rotateBoatExample.setForeground(Color.WHITE);
		rotateBoatExample.setBackground(Color.BLACK);
		rotateBoatExample.setFont(new Font("Monospaced", Font.BOLD, 12));
		rotateBoatExample.setFocusable(false);

		// Placing game title
		JLabel ruleTitle = new JLabel("RULES: Ship Placement!");
		ruleTitle.setFont(new Font("Impact", Font.BOLD, 30));
		ruleTitle.setBounds(15, 0, 350, 50);

		// Explain basic rules
		JLabel basicRules = new JLabel("<html> Battleship is a two-player game played on a grid."
				+ "<br/>Each player has a fleet of ships that they place on their grid before the game begins."
				+ "<br/>The goal of the game is to sink all of your opponent's ships by correctly guessing their locations on the grid.</html>",
				SwingConstants.CENTER);
		basicRules.setFont(new Font("Monospaced", Font.BOLD, 15));
		basicRules.setBounds(15, 40, 850, 150);

		// Create Panel
		panel = new JPanel();
		panel.setLayout(null);
		panel.add(ruleTitle);
		panel.add(basicRules);
		panel.add(buttonExample);
		panel.add(explainRules);
		panel.add(rotateBoatExample);
		panel.add(gridExample);
		panel.add(GuessingStageRules);

		// Create Frame
		frame = new JFrame();
		frame.setTitle("BATTLESHIP RULES");
		frame.setSize(920, 700);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		frame.setLayout(new BorderLayout());
		frame.add(panel);

		// Set Icon of game
		ImageIcon image = new ImageIcon("battleship.png");
		frame.setIconImage(image.getImage());
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == GuessingStageRules) {
			// Call guessing stage rules
			new GuessingStageRules();
		}
	}
}
