package battleshipGame;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;

public class BattleshipGameFrame implements ActionListener {

	public JFrame frame;

	// panel for player's and computer's board
	public JPanel playerPanel;
	public JPanel computerPanel;

	// panels for displaying the total shots, hits, misses, ships sunk
	public JPanel playerInfo;
	public JPanel computerInfo;

	// label for each value listed in above comments (p = player; c = computer)
	public JLabel pShots;
	public JLabel pHits;
	public JLabel pMisses;
	public JLabel pShipsRemain;

	public JLabel cShots;
	public JLabel cHits;
	public JLabel cMisses;
	public JLabel cShipsRemain;

	// variables to hold each value listed above (player and computer)
	public int pTotalShots = 0;
	public int pTotalHits = 0;
	public int pTotalMisses = 0;
	public int pTotalShipsRemain = 5;

	public int cTotalShots = 0;
	public int cTotalHits = 0;
	public int cTotalMisses = 0;
	public int cTotalShipsRemain = 5;

	// used to store the numbers and letters surrounding the board
	public JLabel[][] numGrids;
	public JLabel[] alphebetGrids;

	public JButton[][] playerButtons;
	public JButton[][] computerButtons;
	public JButton[] shipButtons = new JButton[5]; // hold the buttons of each type of ship
	JLabel shipNames [] = new JLabel[5]; // store the name labels of each ship

	// lengths of each ship in respective order of ship buttons
	final public int shipLengths[] = { 5, 4, 3, 3, 2 };

	// shipPosition[ship][0] --> row, shipPosition[ship][1] --> col,
	// shipPosition[ship][2] --> direction (0 = vertical, 1 = horizontal)
	public int playerShipPos[][] = new int[5][3]; // hold coordinate positions of each player's ship's "head"
	public int computerShipPos[][] = new int[5][3]; // hold coordinate positions of each computer's ship's "head"
	// numShipHits(P or C)[ship] = # of hits;
	public int numShipHitsP[] = new int[5]; // hold the number of times player's ships have been hit
	public int numShipHitsC[] = new int[5]; // hold the number of times computer's ships have been hit

	// initialize grids used to hold data on each button --> hit, miss, border etc.
	public int playerGrid[][] = new int[10][10];
	public int computerGrid[][] = new int[10][10];

	// other buttons the game uses
	public JButton placeCarrier;
	public JButton placeBattleship;
	public JButton placeCruiser;
	public JButton placeSubmarine;
	public JButton placeDestroyer;
	public JButton rotateBoat;
	public JButton clearAllBoats;
	public JButton clearOneBoat;
	public JButton StartGame;
	public JButton playAgain;

	// labels for each type of ship
	public JLabel carrierName;
	public JLabel battleshipName;
	public JLabel cruiserName;
	public JLabel submarineName;
	public JLabel destroyerName;

	public JPanel turnPanel;
	public JLabel firstTurnText;
	public int firstTurn; // determine who goes first

    public boolean isComplex; // mode of game

	AudioInputStream audioInput;
	Clip audioClip;

	/**
	 * This method will stop the music
	 * @param audioInput
	 * @param audioClip
	 */
	public void stopMusic(AudioInputStream audioInput, Clip audioClip){
		audioClip.stop();
		audioClip.close();
	}

	/*** CONSTRUCTOR ***/
	public BattleshipGameFrame(boolean isHard, AudioInputStream input, Clip clip) throws IOException {
		audioInput = input;
		audioClip = clip;
        if(isHard == false){
            isComplex = false;
        } else{
            isComplex = true;
        }
		// GUI --> create grid for player's and computer's board
		playerPanel = new JPanel();
		playerPanel.setLayout(new GridLayout(10, 10));
		playerPanel.setBounds(300, 100, 350, 350);

		computerPanel = new JPanel();
		computerPanel.setLayout(new GridLayout(10, 10));
		computerPanel.setBounds(750, 100, 350, 350);

		// initialize the grid buttons for player and computer
		playerButtons = new JButton[10][10];
		computerButtons = new JButton[10][10];

		// Add buttons to panel with grid layout
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				// create settings for each player button
				playerButtons[i][j] = new JButton();
				playerButtons[i][j].setBackground(new Color(111, 111, 111));
				playerButtons[i][j].addActionListener(this);

				// create settings for each computer button
				computerButtons[i][j] = new JButton();
				computerButtons[i][j].setBackground(new Color(111, 111, 111));
				computerButtons[i][j].addActionListener(this);

				// add them to their respective panel
				playerPanel.add(playerButtons[i][j]);
				computerPanel.add(computerButtons[i][j]);
			}
		}
		// determine settings for each ship icon --> colour, dimensions etc.
		// add each ship button to the shipButtons array
		ImageIcon carrier = new ImageIcon("Carrier.png");
		placeCarrier = new JButton(carrier);
		placeCarrier.setBounds(50, 80, 130, 60);
		shipButtons[0] = placeCarrier;

		ImageIcon btshipIcon = new ImageIcon("BtshipIcon.png");
		placeBattleship = new JButton(btshipIcon);
		placeBattleship.setBounds(50, 180, 130, 60);
		shipButtons[1] = placeBattleship;

		ImageIcon cruiser = new ImageIcon("Cruiser.png");
		placeCruiser = new JButton(cruiser);
		placeCruiser.setBounds(50, 280, 130, 60);
		shipButtons[2] = placeCruiser;

		ImageIcon sub = new ImageIcon("Submarine.png");
		placeSubmarine = new JButton(sub);
		placeSubmarine.setBounds(50, 380, 130, 55);
		shipButtons[3] = placeSubmarine;

		ImageIcon destroyer = new ImageIcon("Destroyer.png");
		placeDestroyer = new JButton(destroyer);
		placeDestroyer.setBounds(50, 480, 130, 60);
		shipButtons[4] = placeDestroyer;

		// add action listener to each button and set focusable to false
		for (int i = 0; i < shipButtons.length; i++) {
			shipButtons[i].setFocusable(false);
			shipButtons[i].addActionListener(this);
		}

		// determine settings of each button used to place ships
		rotateBoat = new JButton("VERTICAL");
		rotateBoat.setBounds(300, 480, 160, 40);
		rotateBoat.setForeground(new Color(57, 255, 20));
		rotateBoat.setBackground(Color.BLACK);
		rotateBoat.setFont(new Font("Monospaced", Font.BOLD, 12));
		rotateBoat.setFocusable(false);
		rotateBoat.addActionListener(this);

		clearAllBoats = new JButton("CLEAR ALL BOATS");
		clearAllBoats.setBounds(480, 480, 150, 40);
		clearAllBoats.setForeground(new Color(57, 255, 20));
		clearAllBoats.setBackground(Color.BLACK);
		clearAllBoats.setFont(new Font("Monospaced", Font.BOLD, 12));
		clearAllBoats.setFocusable(false);
		clearAllBoats.addActionListener(this);

		clearOneBoat = new JButton("CLEAR SELECTED BOAT");
		clearOneBoat.setBounds(650, 480, 180, 40);
		clearOneBoat.setForeground(new Color(57, 255, 20));
		clearOneBoat.setBackground(Color.BLACK);
		clearOneBoat.setFont(new Font("Monospaced", Font.BOLD, 12));
		clearOneBoat.setFocusable(false);
		clearOneBoat.addActionListener(this);

		StartGame = new JButton("START GAME");
		StartGame.setBounds(870, 480, 120, 40);
		StartGame.setForeground(new Color(57, 255, 20));
		StartGame.setBackground(Color.BLACK);
		StartGame.setFont(new Font("Monospaced", Font.BOLD, 12));
		StartGame.setFocusable(false);
		StartGame.addActionListener(this);

		// determine settings for playAgain button once game is finished
		playAgain = new JButton("PLAY AGAIN");
		playAgain.setBounds(550, 500, 175, 50);
		playAgain.setForeground(new Color(57, 255, 20));
		playAgain.setBackground(Color.BLACK);
		playAgain.setFont(new Font("Monospaced", Font.BOLD, 20));
		playAgain.setFocusable(false);
		playAgain.addActionListener(this);
		playAgain.setVisible(false); // not visible until game is over

		// set background of the game frame
		ImageIcon shipImage = new ImageIcon("background.jpg");
		JLabel battleshipImage = new JLabel(shipImage);
		battleshipImage.setBounds(0, 0, 1200, 636);

		// determine settings of each ship label and add to shipNames array
		carrierName = new JLabel("CARRIER");
		carrierName.setFont(new Font("Monospaced", Font.BOLD, 18));
		carrierName.setForeground(Color.WHITE);
		carrierName.setBounds(70, 100, 120, 100);
		shipNames[0] = carrierName;

		battleshipName = new JLabel("BATTLESHIP");
		battleshipName.setFont(new Font("Monospaced", Font.BOLD, 18));
		battleshipName.setForeground(Color.WHITE);
		battleshipName.setBounds(65, 200, 120, 100);
		shipNames[1] = battleshipName;

		cruiserName = new JLabel("CRUISER");
		cruiserName.setFont(new Font("Monospaced", Font.BOLD, 18));
		cruiserName.setForeground(Color.WHITE);
		cruiserName.setBounds(70, 300, 120, 100);
		shipNames[2] = cruiserName;

		submarineName = new JLabel("SUBMARINE");
		submarineName.setFont(new Font("Monospaced", Font.BOLD, 18));
		submarineName.setForeground(Color.WHITE);
		submarineName.setBounds(65, 400, 120, 100);
		shipNames[3] = submarineName;

		destroyerName = new JLabel("DESTROYER");
		destroyerName.setFont(new Font("Monospaced", Font.BOLD, 18));
		destroyerName.setForeground(Color.WHITE);
		destroyerName.setBounds(65, 500, 120, 100);
		shipNames[4] = destroyerName;

		// create the game frame
		frame = new JFrame();
		frame.setTitle("BATTLESHIP");
		frame.setSize(1200, 636);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setLayout(null);

		// player info table design
		playerInfo = new JPanel();
		playerInfo.setBounds(20, 75, 225, 225);
		playerInfo.setBackground(Color.BLACK);
		Border border = BorderFactory.createLineBorder(new Color(57, 255, 20), 3);// add border to panel
		playerInfo.setBorder(border);
		playerInfo.setLayout(null);
		playerInfo.setVisible(false);
		// player info label settings
		JLabel pTitle = new JLabel("PLAYER INFO");
		pTitle.setFont(new Font("Monospaced", Font.BOLD, 20));
		pTitle.setForeground(new Color(57, 255, 20));
		pTitle.setBounds(25, 5, 225, 43);
		playerInfo.add(pTitle);

		pShots = new JLabel("Total Shots: " + String.valueOf(pTotalShots));
		pShots.setFont(new Font("Monospaced", Font.BOLD, 15));
		pShots.setForeground(new Color(57, 255, 20));
		pShots.setBounds(25, 40, 225, 43);
		playerInfo.add(pShots);

		pMisses = new JLabel("Total Misses: " + String.valueOf(pTotalMisses));
		pMisses.setFont(new Font("Monospaced", Font.BOLD, 15));
		pMisses.setForeground(new Color(57, 255, 20));
		pMisses.setBounds(25, 83, 225, 43);
		playerInfo.add(pMisses);

		pHits = new JLabel("Total Hits: " + String.valueOf(pTotalHits));
		pHits.setFont(new Font("Monospaced", Font.BOLD, 15));
		pHits.setForeground(new Color(57, 255, 20));
		pHits.setBounds(25, 126, 225, 43);
		playerInfo.add(pHits);

		pShipsRemain = new JLabel("Ships Remaining: " + String.valueOf(pTotalShipsRemain));
		pShipsRemain.setFont(new Font("Monospaced", Font.BOLD, 15));
		pShipsRemain.setForeground(new Color(57, 255, 20));
		pShipsRemain.setBounds(25, 171, 225, 43);
		playerInfo.add(pShipsRemain);

		// computer info table design
		computerInfo = new JPanel();
		computerInfo.setBounds(20, 325, 225, 225);
		computerInfo.setBackground(Color.BLACK);
		computerInfo.setBorder(border);
		computerInfo.setLayout(null);
		computerInfo.setVisible(false);
		// computer info label settings
		JLabel cTitle = new JLabel("COMPUTER INFO");
		cTitle.setFont(new Font("Monospaced", Font.BOLD, 20));
		cTitle.setForeground(new Color(57, 255, 20));
		cTitle.setBounds(25, 5, 225, 43);
		computerInfo.add(cTitle);

		cShots = new JLabel("Total Shots: " + String.valueOf(cTotalShots));
		cShots.setFont(new Font("Monospaced", Font.BOLD, 15));
		cShots.setForeground(new Color(57, 255, 20));
		cShots.setBounds(25, 40, 225, 43);
		computerInfo.add(cShots);

		cMisses = new JLabel("Total Misses: " + String.valueOf(cTotalMisses));
		cMisses.setFont(new Font("Monospaced", Font.BOLD, 15));
		cMisses.setForeground(new Color(57, 255, 20));
		cMisses.setBounds(25, 83, 225, 43);
		computerInfo.add(cMisses);

		cHits = new JLabel("Total Hits: " + String.valueOf(cTotalHits));
		cHits.setFont(new Font("Monospaced", Font.BOLD, 15));
		cHits.setForeground(new Color(57, 255, 20));
		cHits.setBounds(25, 126, 225, 43);
		computerInfo.add(cHits);

		cShipsRemain = new JLabel("Ships Remaining: " + String.valueOf(cTotalShipsRemain));
		cShipsRemain.setFont(new Font("Monospaced", Font.BOLD, 15));
		cShipsRemain.setForeground(new Color(57, 255, 20));
		cShipsRemain.setBounds(25, 171, 225, 43);
		computerInfo.add(cShipsRemain);

		int x1 = 310, x2 = 760; // hold dimensions/location of label
		numGrids = new JLabel[2][10];

		// for each panel
		for (int i = 0; i < 2; i++) {
			// add numbers (i.e. for coordinates A"1", B"3", C"5" etc.) to the edge of each
			// grid
			for (int j = 0; j < 10; j++) {
				if (i <= 0) {
					int tp = j + 1;
					String temp = Integer.toString(tp);
					numGrids[i][j] = new JLabel(temp);
					numGrids[i][j].setForeground(Color.WHITE);
					numGrids[i][j].setBounds(x1, 55, 50, 50);
					numGrids[i][j].setFont(new Font("Monospaced", Font.BOLD, 18));
					frame.add(numGrids[i][j]);
					x1 += 34; // increment x value
				}
				if (i == 1) {
					int tp = j + 1;
					String temp = Integer.toString(tp);
					numGrids[i][j] = new JLabel(temp);
					numGrids[i][j].setForeground(Color.WHITE);
					numGrids[i][j].setBounds(x2, 55, 50, 50);
					numGrids[i][j].setFont(new Font("Monospaced", Font.BOLD, 18));
					frame.add(numGrids[i][j]);
					x2 += 34; // increment x value
				}
			}
		}

		alphebetGrids = new JLabel[10];
		char my_temp;
		// for each panel
		for (int i = 0; i < 2; i++) {
			int m = 0, location1 = 90, location2 = 90; // hold dimensions/location
			// add letters (i.e. for coordinates "A"1, "B"3, "C"5 etc.) to the edge of each
			// grid
			if (i <= 0) {
				for (my_temp = 'A'; my_temp < 'K'; my_temp++) {
					String temp = String.valueOf(my_temp);
					alphebetGrids[m] = new JLabel(temp);
					alphebetGrids[m].setForeground(Color.WHITE);
					alphebetGrids[m].setBounds(268, location1, 50, 50);
					alphebetGrids[m].setFont(new Font("Monospaced", Font.BOLD, 18));
					frame.add(alphebetGrids[m]);
					m++;
					location1 += 36;
				}
			} else {
				for (my_temp = 'A'; my_temp < 'K'; my_temp++) {
					String temp = String.valueOf(my_temp);
					alphebetGrids[m] = new JLabel(temp);
					alphebetGrids[m].setForeground(Color.WHITE);
					alphebetGrids[m].setBounds(720, location2, 50, 50);
					alphebetGrids[m].setFont(new Font("Monospaced", Font.BOLD, 18));
					frame.add(alphebetGrids[m]);
					m++;
					location2 += 36;
				}
			}
		}
		// set frame icon
		ImageIcon image = new ImageIcon("battleship.png");
		frame.setIconImage(image.getImage());

		// set default values to the positions of each ship, -1 representing not placed
		for (int i = 0; i < playerShipPos.length; i++) {
			for (int j = 0; j < playerShipPos[i].length; j++) {
				// j == 2 already default set to 0, which represents vertical direction
				if (j != 2) {
					playerShipPos[i][j] = -1;
				}
			}
		}
		// components of first turn message
		turnPanel = new JPanel();
		turnPanel.setLayout(null);
		turnPanel.setBackground(Color.black);
		turnPanel.setBorder(border); // uses same border as info panels

		firstTurn = (int) (Math.random() * 2); // 0 = player goes first, 1 = computer goes first

		// set text and bounds of label and panel bounds
		firstTurnText = new JLabel();
		if (firstTurn == 0) {
			firstTurnText = new JLabel("You go first");
			firstTurnText.setBounds(10, 0, 250, 50);
			turnPanel.setBounds(300, 480, 250, 50);
		} else if (firstTurn == 1) {
			firstTurnText = new JLabel("The computer will go first");
			firstTurnText.setBounds(10, 0, 500, 50);
			turnPanel.setBounds(300, 480, 500, 50);
		}
		// determine settings of label
		firstTurnText.setHorizontalTextPosition(JLabel.CENTER);
		firstTurnText.setVerticalTextPosition(JLabel.CENTER);
		firstTurnText.setForeground(new Color(57, 255, 20));
		firstTurnText.setBackground(Color.BLACK);
		firstTurnText.setFont(new Font("Monospaced", Font.BOLD, 30));
		firstTurnText.setFocusable(false);

		turnPanel.setVisible(false);
		turnPanel.add(firstTurnText);
		frame.add(turnPanel);

		// instantiate computerBoard class
		ComputerBoard computer = new ComputerBoard();
		computer.placeShips(computerGrid, computerShipPos, shipLengths);// place computer ships
		computer.writeToFile(computerGrid); // write ans to file

		// add all components to frame
		frame.add(playerPanel);
		frame.add(computerPanel);
		frame.add(placeCarrier);
		frame.add(placeBattleship);
		frame.add(placeCruiser);
		frame.add(placeSubmarine);
		frame.add(placeDestroyer);
		frame.add(rotateBoat);
		frame.add(clearAllBoats);
		frame.add(clearOneBoat);
		frame.add(StartGame);
		frame.add(playerInfo);
		frame.add(computerInfo);
		frame.add(playAgain);
		frame.add(carrierName);
		frame.add(destroyerName);
		frame.add(cruiserName);
		frame.add(submarineName);
		frame.add(battleshipName);
		frame.add(battleshipImage);
	}

	public int shipIndex = -1; // hold the index of which ship is chosen
	public int curShipDir = 0; // direction of the current ship chosen
	public boolean isHighlighted = false; // determine if ship is highlighted
	public int tempShipIndex = -1; // hold the index of the previous ship chosen
	public boolean hasGameStarted = false; // used to check if game as started or finished

	// instantiate player classes
	PlayerBoard player = new PlayerBoard(); 
	PlayerMoves playerMoves = new PlayerMoves();
	// instantiate class with all the AI methods
    AI ai = new AI();
	
	public void actionPerformed(ActionEvent e) {
		// check which ship player wants to place
		for (int i = 0; i < shipButtons.length; i++) {
			if (e.getSource() == shipButtons[i]) {
				shipIndex = i;
				// if no ship is currently highlighted, highlight chosen ship --> visual for user
				if(!isHighlighted){
					shipNames[shipIndex].setForeground(new Color(57, 255, 20));
					isHighlighted = true;
					tempShipIndex = shipIndex;
				} 
				// if previous ship exists
				else if(tempShipIndex != -1){
					// unhighlight previously chosen ship name
					shipNames[tempShipIndex].setForeground(Color.white);
					// if the previous ship is already placed on the board, unhighlight it
					if(playerShipPos[tempShipIndex][0] != -1){
						player.unhighlightShip(playerShipPos[tempShipIndex][0], playerShipPos[tempShipIndex][1],
						shipLengths[tempShipIndex], playerShipPos[tempShipIndex][2], playerButtons);
					}
					// highlight current chosen ship
					shipNames[i].setForeground(new Color(57, 255, 20));
					tempShipIndex = shipIndex;
				}
				// if ship is on grid already, unhighlight previous ship
				// and highlight current chosen one
				if (playerShipPos[shipIndex][0] != -1) {
					// unhighlight previous ship
					player.unhighlightShip(playerShipPos[tempShipIndex][0], playerShipPos[tempShipIndex][1],
							shipLengths[tempShipIndex], playerShipPos[tempShipIndex][2], playerButtons);
					// highlight new ship
					player.highlightShip(playerShipPos[shipIndex][0], playerShipPos[shipIndex][1],
							shipLengths[shipIndex], playerShipPos[shipIndex][2], playerButtons);
					tempShipIndex = shipIndex;
				}
				break; // no need to check other buttons once clicked button is found
			}
		}
		// loop through all player's buttons
		for (int r = 0; r < playerButtons.length; r++) {
			for (int c = 0; c < playerButtons[r].length; c++) {
				// check which button player clicked
				if (e.getSource() == playerButtons[r][c]) {
					// if a ship has been chosen
					if (shipIndex != -1) {
						// if a ship has already been placed, must remove the current position
						// of the ship before placing its new location
						if (playerShipPos[shipIndex][0] != -1) {
							player.clearShip(playerShipPos[shipIndex][0], playerShipPos[shipIndex][1], shipIndex,
									shipLengths[shipIndex], playerShipPos[shipIndex][2], playerButtons, playerGrid,
									playerShipPos);
						}
						// place or move ship
						player.placeShips(r, c, shipIndex, shipLengths[shipIndex], curShipDir, playerButtons,
								playerGrid, playerShipPos);
					}
					break;
				}
			}
		}
		// rotate direction of boat of the chosen ship
		if (shipIndex != -1 && e.getSource() == rotateBoat) {
			if (rotateBoat.getText().equals("VERTICAL")) {
				curShipDir = 1;
				rotateBoat.setText("HORIZONTAL");
			} else {
				curShipDir = 0;
				rotateBoat.setText("VERTICAL");
			}
		}
		// clear current/chosen ship
		if (e.getSource() == clearOneBoat && shipIndex != -1 && playerShipPos[shipIndex][0] != -1) {
			player.clearShip(playerShipPos[shipIndex][0], playerShipPos[shipIndex][1], shipIndex,
					shipLengths[shipIndex], playerShipPos[shipIndex][2], playerButtons, playerGrid, playerShipPos);
			// unhighlight ship name
			shipNames[shipIndex].setForeground(Color.white);
			isHighlighted = false;
			shipIndex = -1;
		}

		// clear all ships
		if (e.getSource() == clearAllBoats) {
			player.clearBoard(playerButtons, playerGrid, playerShipPos);
			//unhighlight ship name if one is highlighted
			if(isHighlighted){
				shipNames[shipIndex].setForeground(Color.white);
			}
			isHighlighted = false;
			shipIndex = -1;
		}

		// ***GAME BEGINS***
		if (e.getSource() == StartGame) {
			// check if player has placed all their ships
			if (!player.allShipsPlaced(playerShipPos)) {
				// display error message if all ships are not placed
				JOptionPane.showMessageDialog(null,
						"Not all ships have been placed. Please place them\nall on the grid before starting the game.",
						"Error", JOptionPane.ERROR_MESSAGE, null);
			} else {
				// if a ship or name is highlighted, unhighlight it before starting the game
				if (isHighlighted) {
					player.unhighlightShip(playerShipPos[shipIndex][0], playerShipPos[shipIndex][1],
							shipLengths[shipIndex], playerShipPos[shipIndex][2], playerButtons);
				}
				if(isHighlighted){
					shipNames[shipIndex].setForeground(new Color(57, 255, 20));
				}
				shipIndex = -1; // reset shipIndex
				
				// set the visibility of all components involved in the placement of ships to false
				for (int i = 0; i < shipButtons.length; i++) {
					shipButtons[i].setVisible(false);
				}
				rotateBoat.setVisible(false);
				clearAllBoats.setVisible(false);
				clearOneBoat.setVisible(false);
				StartGame.setVisible(false);

				carrierName.setVisible(false);
				battleshipName.setVisible(false);
				submarineName.setVisible(false);
				destroyerName.setVisible(false);
				cruiserName.setVisible(false);

				hasGameStarted = true; // mark the start of the game
				turnPanel.setVisible(true); // display who goes first
				// show info tables
				playerInfo.setVisible(true);
				computerInfo.setVisible(true);
			}
		}
		
		// GUESSING STAGE
		if (hasGameStarted) {
			// loop through entire computer buttons board
			for (int r = 0; r < computerButtons.length; r++) {
				for (int c = 0; c < computerButtons[r].length; c++) {
					// if computer go first, they make the first move
					if (firstTurn == 1) {
                        int cHitValue; // store if a move is a hit miss or sink
						// check which game difficulty user has chose
                        if(isComplex){
                            cHitValue = ai.complexAI(playerGrid, playerButtons, shipLengths, numShipHitsP, playerShipPos, shipNames);
                        } else{
                            cHitValue = ai.simpleAI(playerGrid, playerButtons, shipLengths, numShipHitsP, playerShipPos, shipNames);
                        }
						// update computer's info given a hit or miss 
						// no need to check for sink because not possible to sink a ship in first try
						if (cHitValue == -1) {
							cTotalMisses++;
							cMisses.setText("Total Misses: " + String.valueOf(cTotalMisses));
						} else if (cHitValue == 1) {
							cTotalHits++;
							cHits.setText("Total Hits: " + String.valueOf(cTotalHits));
						}
						// update total shots
						cTotalShots++;
						cShots.setText("Total Shots: " + String.valueOf(cTotalShots));
						firstTurn = -1; // variable not needed anymore, set to value that won't be needed
					}
					// check which button player wants to hit
					if (e.getSource() == computerButtons[r][c]) {
						turnPanel.setVisible(false);// once player has made move, no need for this label
						// PLAYER TURN
						if (playerMoves.isMoveValid(r, c, computerGrid)) {
							// make move and store if it was a hit miss or sink
							int pHitValue = playerMoves.makeMove(r, c, computerGrid, computerButtons, shipLengths,
									numShipHitsC, computerShipPos, shipNames);
							// update player's info given a hit, miss, or ships remaining
							if (pHitValue == -1) {
								pTotalMisses++;
								pMisses.setText("Total Misses: " + String.valueOf(pTotalMisses));
							} else if (pHitValue == 1) {
								pTotalHits++;
								pHits.setText("Total Hits: " + String.valueOf(pTotalHits));
							} else if (pHitValue == 2) {
								pTotalHits++;
								pHits.setText("Total Hits: " + String.valueOf(pTotalHits));

								cTotalShipsRemain--;
								cShipsRemain.setText("Ships Remaining: " + String.valueOf(cTotalShipsRemain));
							}
							// update total shots
							pTotalShots++;
							pShots.setText("Total Shots: " + String.valueOf(pTotalShots));
							// check if player has sunk all ships
							if (playerMoves.isAllShipsSunk(numShipHitsC)) {
								// clear both the player's and computer's grids when the game is over
								player.clearBoard(playerButtons, playerGrid, playerShipPos);
								player.clearBoard(computerButtons, computerGrid, computerShipPos);
								// output message that player has won
								ImageIcon win = new ImageIcon("win.png");
								JOptionPane.showMessageDialog(null,
										"Congratulations, you have sunken all\nthe computer's ships and WIN THE GAME!!! ",
										"Ship Sunk!", JOptionPane.INFORMATION_MESSAGE, win);
								hasGameStarted = false; // end game
								playAgain.setVisible(true); // display play again button
								break; // if player wins, computer shouldn't make a move
							}
							// COMPUTER TURN
							int cHitValue; // store if a move is a hit miss or sink
							// check which game difficulty user has chose
                            if(isComplex){
                                cHitValue = ai.complexAI(playerGrid, playerButtons, shipLengths, numShipHitsP, playerShipPos, shipNames);
                            } else{
                                cHitValue = ai.simpleAI(playerGrid, playerButtons, shipLengths, numShipHitsP, playerShipPos, shipNames);
                            }
							// update the computer's info given a hit, miss, or ships remaining
							if (cHitValue == -1) {
								cTotalMisses++;
								cMisses.setText("Total Misses: " + String.valueOf(cTotalMisses));
							} else if (cHitValue == 1) {
								cTotalHits++;
								cHits.setText("Total Hits: " + String.valueOf(cTotalHits));
							} else if (cHitValue == 2) {
								cTotalHits++;
								cHits.setText("Total Hits: " + String.valueOf(cTotalHits));

								pTotalShipsRemain--;
								pShipsRemain.setText("Ships Remaining: " + String.valueOf(pTotalShipsRemain));
							}
							// update total shots
							cTotalShots++;
							cShots.setText("Total Shots: " + String.valueOf(cTotalShots));
							// check if AI has sunk all player's ships
							if (ai.isAllShipsSunk(numShipHitsP)) {
								// clear both the player's and computer's grids when the game is over
								player.clearBoard(playerButtons, playerGrid, playerShipPos);
								player.clearBoard(computerButtons, computerGrid, computerShipPos);

								// output message that player has lost
								ImageIcon lose = new ImageIcon("lose.png");
								JOptionPane.showMessageDialog(null,
										"Unfortunately, you lose... :(\nThe computer has sunk all your ships.",
										"Ship Sunk!", JOptionPane.INFORMATION_MESSAGE, lose);
								hasGameStarted = false; // end game
								playAgain.setVisible(true); // display play again button
							}
						}
						break; 
					}
				}
			}
		}
		// if user wants to play again
		if (e.getSource() == playAgain) {
			stopMusic(audioInput, audioClip); // stop music when game ends
			new BattleshipIntroFrame(); // instantiate intro frame
			frame.dispose(); // close game frame
		}
	}
}