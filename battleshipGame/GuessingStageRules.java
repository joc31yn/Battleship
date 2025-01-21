package battleshipGame;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GuessingStageRules {
	
	private JFrame frame;
	private JPanel panel;
	
	public GuessingStageRules(){
		// Placing game title
		JLabel ruleTitle = new JLabel("RULES: Guessing Stage");
		ruleTitle.setFont(new Font("Impact", Font.BOLD, 30));
		ruleTitle.setBounds(15, 0, 350, 50);
		
		// Placing grid example
		ImageIcon grid = new ImageIcon("guessingExample.png");
		JLabel gridExample = new JLabel(grid);
		gridExample.setBounds(30, 270, 400, 373);
		
		// Placing game rules
		JLabel actualRules = new JLabel("<html> GUESSING STAGE:"
				+ "<br/>Players take turns guessing the location of their opponent's ships by selecting a square button on the grid (e.g., \"A3\")."
				+ "<br/>If the opponent's ship is located at the chosen square, it is a \"hit\"."
				+ "<br/>If the square is empty, it is a \"miss\"."
				+ "<br/>The grid will change colour accordingly to \"hit\" or \"miss\"."
				+ "<br/>The guessing continues until one player sinks all of their opponent's ships.</html>",
				SwingConstants.CENTER);
		actualRules.setFont(new Font("Monospaced", Font.BOLD, 15));
		actualRules.setBounds(20, 80, 850, 150);
		
		// Placing more game rules
		JLabel explain = new JLabel("<html> Red: Hit"
				+ "<br/>Green: Miss"
				+ "<br/><br/><br/><br/>END OF THE GAME"
				+ "<br/>The game is over when one player sinks all of their opponent's ships."
				+ "<br/>The player who sinks all of their opponent's ships first is the winner."
				+ "<html>", SwingConstants.CENTER);
		explain.setFont(new Font("Monospaced", Font.BOLD, 15));
		explain.setBounds(450, 250, 400, 350);

		// Create Panel and add all the rules and images
		panel = new JPanel();
		panel.setLayout(null);
		panel.add(ruleTitle);
		panel.add(actualRules);
		panel.add(gridExample);
		panel.add(explain);

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
}
