package battleshipGame;

import java.awt.Color;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class PlayerMoves {
	// Method plays Explosion sound effect when ship is hit.
	public void playExplosion() {
		String filePath = "ExplosionSoundEffect.wav";
		File musicPath = new File(filePath);
		try {
			if (musicPath.exists()) {
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
				Clip clip = AudioSystem.getClip();
				clip.open(audioInput);
				clip.start();
			} else {
				System.out.println("Can't find file");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * This method checks if the square chosen by the user is valid
	 * 
	 * @param r
	 * @param c
	 * @param computerGrid
	 * @return true if valid, false if not valid
	 */
	public boolean isMoveValid(int r, int c, int computerGrid[][]) {
		// if the square is already marked as missed or hit, it is invalid
		if (computerGrid[r][c] == -5 || computerGrid[r][c] > 5) {
			JOptionPane.showMessageDialog(null, "This square has already been chosen.\nPlease choose another one.",
					"Error", JOptionPane.ERROR_MESSAGE, null);
			return false;
		}
		return true;
	}

	public int makeMove(int r, int c, int computerGrid[][], JButton computerButtons[][], int shipLengths[],
			int numShipHitsC[], int computerShipPos[][], JLabel shipNames[]) {
		// if miss
		if (computerGrid[r][c] <= 0) {
			computerGrid[r][c] = -5;
			computerButtons[r][c].setBackground(new Color(57, 255, 20));
			return -1;// represents miss
		}
		// if a ship is hit
		else {
			playExplosion();
			// determine the index of which ship has been hit
			int shipIndex = computerGrid[r][c] - 1;
			// mark the ship as hit on GUI and 2D array
			computerGrid[r][c] += 5;
			computerButtons[r][c].setBackground(new Color(168, 9, 9));
			numShipHitsC[shipIndex]++; // count number of hits

			// check if the ship hit has been sunk
			int shipNum = shipSunk(shipIndex, shipLengths, numShipHitsC);
			// if it is sunk
			if (shipNum >= 0) {
				// determine name of ship that has been sunk
				String shipName = shipNames[shipNum].getText();
				// inform user they have sunk a ship
				ImageIcon sunkenShip = new ImageIcon("sunkenShip.png");
				JOptionPane.showMessageDialog(null, "You have sunk the " + shipName + "!", "Ship Sunk!",
						JOptionPane.INFORMATION_MESSAGE, sunkenShip);

				int rowEnd;
				int colEnd;
				int rowStart;
				int colStart;
				// determine squares surrounding the ships
				// if ships are at the border of the gird, take 9 instead of r + 1/c + 1
				// and take 0 instead of c - 1 / r - 1, otherwise result in out of bounds

				// vertical
				if (computerShipPos[shipIndex][2] == 0) {
					rowEnd = Math.min(computerGrid.length - 1, computerShipPos[shipIndex][0] + shipLengths[shipIndex]);
					colEnd = Math.min(computerGrid[0].length - 1, computerShipPos[shipIndex][1] + 1);
					rowStart = Math.max(0, computerShipPos[shipIndex][0] - 1);
					colStart = Math.max(0, computerShipPos[shipIndex][1] - 1);
				}
				// horizontal
				else {
					rowEnd = Math.min(computerGrid.length - 1, computerShipPos[shipIndex][0] + 1);
					colEnd = Math.min(computerGrid[0].length - 1,
							computerShipPos[shipIndex][1] + shipLengths[shipIndex]);
					rowStart = Math.max(0, computerShipPos[shipIndex][0] - 1);
					colStart = Math.max(0, computerShipPos[shipIndex][1] - 1);
				}

				// mark the borders of the ship as miss
				for (int x = rowStart; x <= rowEnd; x++) {
					for (int y = colStart; y <= colEnd; y++) {
						if (computerGrid[x][y] < 0) {
							computerGrid[x][y] = -5;
							computerButtons[x][y].setBackground(new Color(57, 255, 20));
						}
					}
				}
				return 2; // represents sink
			}
			return 1; // represents hit
		}
	}

	/**
	 * This method will determine if a player's ship has been sunk
	 * 
	 * @param shipIndex
	 * @param shipLengths
	 * @param numShipHitsC
	 * @return the index of which ship is sunk or -1 if the ship is not sunk
	 */
	public int shipSunk(int shipIndex, int shipLengths[], int numShipHitsC[]) {
		// if the number of hits is equal to the length of ship, its sunken
		if (numShipHitsC[shipIndex] == shipLengths[shipIndex]) {
			numShipHitsC[shipIndex] = -1; // mark ships that have been sunken
			return shipIndex;
		}
		return -1; // ship is not sunk
	}

	/**
	 * This method will determine if all the computer's ships have been sunk
	 * 
	 * @param numShipHitsC
	 * @return true if all ships are sunk, false otherwise
	 */
	public boolean isAllShipsSunk(int numShipHitsC[]) {
		for (int i = 0; i < numShipHitsC.length; i++) {
			if (numShipHitsC[i] != -1) {
				return false;
			}
		}
		return true;
	}
}
