package battleshipGame;

import java.awt.Color;
import javax.swing.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class AI {

	// used to check the surrounding 4 directions when a ship is hit
	// directions: up, down, left, right
	int[] directionsR = { -1, 1, 0, 0 };
	int[] directionsC = { 0, 0, -1, 1 };

	// Diagonals: up left, up right, down left, down right
	int[] diagonalR = { -1, -1, 1, 1 };
	int[] diagonalC = { -1, 1, -1, 1 };

	// current coordinates
	public int r;
	public int c;

	// hold coordinates of first hit
	public int initialCoordinateR;
	public int initialCoordinateC;

	// hold previous/temporary coordinates
	public int tempCoordinateR;
	public int tempCoordinateC;

	public int tempDiagonalR;
	public int tempDiagonalC;

	// if known, hold direction of ship hit
	public int tempDirectionR;
	public int tempDirectionC;

	public int randDirection;
	public int randDiagonal;
	public int shipIndex;

	private boolean firstMove = true;

	// Method plays Explosion sound effect when ship is hit.
	public void playExplosion() {
		String filepath = "ExplosionSoundEffect.wav";
		File musicPath = new File(filepath);
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
	 * This method is for the plays made by the simple AI
	 * 
	 * @param playerGrid
	 * @param playerButtons
	 * @param shipLengths
	 * @param numShipHitsP
	 * @param playerShipPos
	 * @return value returned from making a move (hit, miss, or sunk)
	 */
	public int simpleAI(int playerGrid[][], JButton playerButtons[][], int shipLengths[], int numShipHitsP[],
			int playerShipPos[][], JLabel shipNames[]) {
		// generate a random coordinate and make the move
		randomCoordinate(playerGrid);
		return makeMove(playerGrid, playerButtons, shipLengths, numShipHitsP, playerShipPos, shipNames);
	}

	/**
	 * This method is for the plays made by the complex AI
	 * 
	 * @param playerGrid
	 * @param playerButtons
	 * @param shipLengths
	 * @param numShipHitsP
	 * @param playerShipPos
	 * @return value returned from making a move (hit, miss, or sunk)
	 */
	public int complexAI(int playerGrid[][], JButton playerButtons[][], int shipLengths[], int numShipHitsP[],
			int playerShipPos[][], JLabel shipNames[]) {
		shipIndex = shipsPartiallyHit(shipLengths, numShipHitsP);
		// Generate random coordinate when there is no partially hit ships on the board
		if (shipIndex < 0) {
			// Random coordinate if the hit is the first move
			if (firstMove) {
				randomCoordinate(playerGrid);
				firstMove = false;
			} 
			// Random coordinate if the diagonals of the coordinate are invalid
			else if (!diagonalValid(r, c, playerGrid)){
				randomCoordinate(playerGrid);
			}
			// Goes diagonally if there is valid diagonal coordinates
			else {
				randomDiagonal(playerGrid);
			}
			int hitValue = makeMove(playerGrid, playerButtons, shipLengths, numShipHitsP, playerShipPos, shipNames);
			// If a ship is hit in this step, store the coordinates of the first point hit
			if (hitValue > 0) {
				initialCoordinateR = r;
				initialCoordinateC = c;
				firstMove = true;
			}
			return hitValue;
		}
		// If there is ship partially hit only once
		else if (numShipHitsP[shipIndex] == 1) {
			// regenerate direction and coordinates if current coordinates are out of bounds
			// or not valid
			do {
				randDirection = (int) (Math.random() * 4);
				tempCoordinateR = initialCoordinateR + directionsR[randDirection];
				tempCoordinateC = initialCoordinateC + directionsC[randDirection];
			} while (!isCoordinateValid(tempCoordinateR, tempCoordinateC, playerGrid));
			r = tempCoordinateR;
			c = tempCoordinateC;
			int hitValue = makeMove(playerGrid, playerButtons, shipLengths, numShipHitsP, playerShipPos, shipNames);
			// check if this shot hits a ship
			if (hitValue == 1) {
				// if there's another hit, the direction of the ship is known --> store
				// direction
				tempDirectionR = directionsR[randDirection];
				tempDirectionC = directionsC[randDirection];
			} else if (hitValue == 2) {
				tempDirectionR = 0;
				tempDirectionC = 0;
			}
			return hitValue;

			// If more than two points of a ship is discovered
		} else {
			tempCoordinateR += tempDirectionR;
			tempCoordinateC += tempDirectionC;
			// If the next shot is not valid, search the other direction
			if (!isCoordinateValid(tempCoordinateR, tempCoordinateC, playerGrid)) {
				// switch to opposite direction (i.e. originally left, search right 
				// or originally down, search up)
				tempDirectionR *= -1;
				tempDirectionC *= -1;
				// recalculate coordinates
				tempCoordinateR = initialCoordinateR + tempDirectionR;
				tempCoordinateC = initialCoordinateC + tempDirectionC;
			}
			r = tempCoordinateR;
			c = tempCoordinateC;

			int hitValue = makeMove(playerGrid, playerButtons, shipLengths, numShipHitsP, playerShipPos, shipNames);

			// If the shot misses, go the other direction
			if (hitValue == -1) {
				tempDirectionR *= -1;
				tempDirectionC *= -1;
				tempCoordinateR = initialCoordinateR;
				tempCoordinateC = initialCoordinateC;
			}
			return hitValue;
		}
	}

	/**
	 * This method will mark the move on GUI frame and update the value in the
	 * array.
	 * It will also return a number corresponding to a miss, hit, or sink
	 * -1 --> miss
	 * 1 --> hit
	 * 2 --> hit and sink
	 * 
	 * @param playerGrid
	 * @param playerButtons
	 * @param shipLengths
	 * @param numShipHitsP
	 * @param playerShipPos
	 * @return
	 */
	public int makeMove(int playerGrid[][], JButton playerButtons[][], int shipLengths[], int numShipHitsP[],
			int playerShipPos[][], JLabel shipNames[]) {
		// mark the miss on grid and array
		if (playerGrid[r][c] <= 0) {
			playerGrid[r][c] = -5;
			playerButtons[r][c].setBackground(new Color(57, 255, 20));
			return -1; // represent miss
		}
		// mark the hit on GUI and array
		else {
			// Play explosion sound effect
			playExplosion();

			shipIndex = playerGrid[r][c] - 1;
			playerGrid[r][c] += 5;// mark as hit
			numShipHitsP[shipIndex]++;// count number of times a ship has been hit
			playerButtons[r][c].setBackground(new Color(168, 9, 9));// display a hit ship

			int shipNum = shipSunk(shipIndex, shipLengths, numShipHitsP);

			// if a ship has been sunk once hit
			if (shipNum >= 0) {
				// determine name of ship that has been sunk
				String shipName = shipNames[shipNum].getText();
				// tell user their ship has been sunk
				ImageIcon sunkenShip = new ImageIcon("sunkenShip.png");
				JOptionPane.showMessageDialog(null, "The computer has sunk your " + shipName + "!", "Ship Sunk!",
						JOptionPane.INFORMATION_MESSAGE, sunkenShip);
				int rowEnd;
				int colEnd;
				int rowStart;
				int colStart;
				// determine squares surrounding the ships
				// if ships are at the border of the gird, take 9 instead of r + 1/c + 1
				// and take 0 instead of c - 1 / r - 1, otherwise result in out of bounds
				// vertical
				if (playerShipPos[shipIndex][2] == 0) {
					rowEnd = Math.min(playerGrid.length - 1, playerShipPos[shipIndex][0] + shipLengths[shipIndex]);
					colEnd = Math.min(playerGrid[0].length - 1, playerShipPos[shipIndex][1] + 1);
					rowStart = Math.max(0, playerShipPos[shipIndex][0] - 1);
					colStart = Math.max(0, playerShipPos[shipIndex][1] - 1);
				}
				// horizontal
				else {
					rowEnd = Math.min(playerGrid.length - 1, playerShipPos[shipIndex][0] + 1);
					colEnd = Math.min(playerGrid[0].length - 1, playerShipPos[shipIndex][1] + shipLengths[shipIndex]);
					rowStart = Math.max(0, playerShipPos[shipIndex][0] - 1);
					colStart = Math.max(0, playerShipPos[shipIndex][1] - 1);
				}
				// mark the borders of the ship as miss because whole ship is sunk
				for (int row = rowStart; row <= rowEnd; row++) {
					for (int col = colStart; col <= colEnd; col++) {
						if (playerGrid[row][col] < 0) {
							playerGrid[row][col] = -5; // mark miss
							// Surrounding colour
							playerButtons[row][col].setBackground(new Color(57, 255, 20));
						}
					}
				}
				return 2; // hit and sink ship
			} else {
				return 1; // only hit
			}
		}
	}

	/**
	 * This method will determine if a ship has been sunk
	 * 
	 * @param shipIndex
	 * @param shipLengths
	 * @param numShipHitsP
	 * @return the index of which ship has been sunk, or -1 if no ship is sunk
	 */
	public int shipSunk(int shipIndex, int shipLengths[], int numShipHitsP[]) {
		if (numShipHitsP[shipIndex] == shipLengths[shipIndex]) {
			numShipHitsP[shipIndex] = -1; // ship has already been sunk
			return shipIndex;
		}
		return -1;
	}

	/**
	 * This method will check if all the players ships have been sunk
	 * 
	 * @param numShipHitsP
	 * @return
	 */
	public boolean isAllShipsSunk(int numShipHitsP[]) {
		for (int i = 0; i < numShipHitsP.length; i++) {
			if (numShipHitsP[i] != -1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This method will generate a random coordinate until valid
	 * Used for both simple and complex AI.
	 * 
	 * @param playerGrid
	 */
	private void randomCoordinate(int playerGrid[][]) {
		do {
			r = (int) (Math.random() * 10);
			c = (int) (Math.random() * 10);
		} while (!isCoordinateValid(r, c, playerGrid));
	}

	/**
	 * This method will check if any ship has been hit one or more times, but not
	 * fully sunk
	 * 
	 * @param shipLengths
	 * @param numShipHitsP
	 * @return the index of a ship that has been partially hit, -1 if no ship has
	 *         been partially hit
	 */
	public int shipsPartiallyHit(int shipLengths[], int numShipHitsP[]) {
		for (int i = 0; i < shipLengths.length; i++) {
			// as long as number of hits is not 0 or the length of the ship
			if (numShipHitsP[i] != 0 && numShipHitsP[i] != shipLengths[i] && numShipHitsP[i] != -1) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * This method will check if a coordinate is valid
	 * 
	 * @param r
	 * @param c
	 * @param playerGrid
	 * @return false if not valid, true if valid
	 */
	public boolean isCoordinateValid(int r, int c, int playerGrid[][]) {
		// check if the coordinate is within bounds
		// check if the coordinate has already been missed (-5)
		// check if the coordinate has already been hit (> 5)
		if (r >= 10 || r < 0 || c < 0 || c >= 10 || playerGrid[r][c] == -5 || playerGrid[r][c] > 5) {
			return false;
		}
		return true;
	}

	/**
	 * This method will generate a random coordinate diagonally
	 * 
	 * @param playerGrid
	 */
	public void randomDiagonal(int playerGrid[][]) {
		// regenerate until a coordinate is valid
		do {
			randDiagonal = (int) (Math.random() * 4);
			tempDiagonalR = r + diagonalR[randDiagonal];
			tempDiagonalC = c + diagonalC[randDiagonal];
		} while (!isCoordinateValid(tempDiagonalR, tempDiagonalC, playerGrid));
		r = tempDiagonalR;
		c = tempDiagonalC;
	}

	/**
	 * This method will check if there is valid diagonal coordinates around
	 * 
	 * @param r
	 * @param c
	 * @param playerGrid
	 * @return
	 */
	public boolean diagonalValid(int r, int c, int playerGrid[][]) {
		int validCoordinates = 0;
		int tempR;
		int tempC;

		// Check all diagonals if they are valid or not
		for (int i = 0; i < diagonalR.length; i++) {
			tempR = r + diagonalR[i];
			tempC = c + diagonalC[i];
			if (isCoordinateValid(tempR, tempC, playerGrid))
				validCoordinates++;
		} // end for

		// If none coordinates are valid, return false
		if (validCoordinates == 0)
			return false;

		// At least one valid coordinate, return true
		return true;
	}
}
