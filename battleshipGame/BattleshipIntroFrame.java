package battleshipGame;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class BattleshipIntroFrame implements ActionListener {
	
	//Buttons and Frame
	private JFrame frame;
	private JPanel panel;
	private JButton easyMode;
	private JButton hardMode;
	private JButton information;
	
	// Boolean to determine complexity (hard or easy)
	public boolean hard = false;

	private AudioInputStream audioInput;
	private Clip clip;

	/**
	 * This method plays the music
	 */
	public void playMusic() {
		String filepath = "BGM.wav";
		File musicPath = new File(filepath);
		try {
			if(musicPath.exists()) {
				audioInput = AudioSystem.getAudioInputStream(musicPath);
				clip = AudioSystem.getClip();
				clip.open(audioInput);
				clip.start();
			}
			else {
				System.out.println("Can't find file");
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public BattleshipIntroFrame() {

		// Create panel without layout
		panel = new JPanel();
		panel.setLayout(null);

		// Information Button (Game rules)
		information = new JButton("RULES");
		information.setBounds(40, 30, 80, 35);
		information.setForeground(Color.WHITE);
		information.setBackground(Color.BLACK);
		information.setFocusable(false);
		information.addActionListener(this);
		information.setBorder(null);
		information.setBorderPainted(false);
		information.setContentAreaFilled(false);
		information.setOpaque(false);
		panel.add(information);

		// Button that leads to easy mode
		easyMode = new JButton("PLAY EASY MODE");
		easyMode.setBounds(190, 390, 150, 40);
		easyMode.setForeground(Color.WHITE);
		easyMode.setBackground(Color.BLACK);
		easyMode.setFocusable(false);
		easyMode.addActionListener(this);
		easyMode.setBorder(null);
		easyMode.setBorderPainted(false);
        easyMode.setContentAreaFilled(false);
        easyMode.setOpaque(false);
		panel.add(easyMode);

		// Button that leads to hard mode
		hardMode = new JButton("PLAY HARD MODE");
		hardMode.setBounds(590, 390, 150, 40);
		hardMode.setForeground(Color.WHITE);
		hardMode.setBackground(Color.BLACK);
		hardMode.setFocusable(false);
		hardMode.addActionListener(this);
	    hardMode.setBorder(null);
        hardMode.setBorderPainted(false);
        hardMode.setContentAreaFilled(false);
        hardMode.setOpaque(false);
		panel.add(hardMode);

		// Background image
		ImageIcon shipImage = new ImageIcon("BattleshipBG.png");
		JLabel battleshipImage = new JLabel(shipImage);
		battleshipImage.setBounds(0, 0, 920, 518);
		panel.add(battleshipImage);

		// Create Frame
		frame = new JFrame();
		frame.setTitle("BATTLESHIP");
		frame.setSize(920, 518);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setLayout(new BorderLayout());
		frame.add(panel);
		ImageIcon image = new ImageIcon("battleship.png");
		frame.setIconImage(image.getImage());

		playMusic(); // play music
	}
	
	
	// Buttons
	public void actionPerformed(ActionEvent e) {
		// begin game with complex AI
		if(e.getSource() == hardMode) {
			hard = true;
			try {
				new BattleshipGameFrame(hard, audioInput, clip);
				frame.dispose();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		// begin game with simple AI
		if(e.getSource() == easyMode) {
			try {
				new BattleshipGameFrame(hard, audioInput, clip);
				frame.dispose();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		// display rules
		if(e.getSource() == information) {
			new ShipPlacementGameRules();
		}
	}
}
