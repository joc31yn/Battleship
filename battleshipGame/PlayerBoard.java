package battleshipGame;

import javax.swing.*;
import java.awt.*;

/**
 * PlayerBoard class contains all the methods that a player can do
 * on their grid when they place their ships
 */
public class PlayerBoard {

    /**
     * This method will place the ship onto the grid, and mark the location as well
     * 
     * @param r
     * @param c
     * @param shipIndex
     * @param shipLength
     * @param shipDir
     * @param playerButtons
     * @param playerGrid
     * @param shipPositions
     */
    public void placeShips(int r, int c, int shipIndex, int shipLength, int shipDir, JButton playerButtons[][],
            int playerGrid[][], int shipPositions[][]) {

        // instantiate shipMethod class
        ShipMethods shipPlacements = new ShipMethods();

        // check if the position chosen is valid
        if (!shipPlacements.isShipValid(playerGrid, r, c, shipDir, shipLength)) {
            // output error message if not valid
            JOptionPane.showMessageDialog(null, "The square you have chosen is invalid.\nPlease choose another one.",
                    "Error", JOptionPane.ERROR_MESSAGE, null);
        }
        // ship placement is valid
        else {
            shipPlacements.placeShip(playerGrid, r, c, shipDir, shipLength, shipIndex + 1);
            // vertical
            if (shipDir == 0) {
                // update the players board (GUI) to show the ship
                for (int j = r; j < r + shipLength; j++) {
                    playerButtons[j][c].setBackground(new Color(43, 194, 14));
                }
            }
            // horizontal
            else {
                // update the players board (GUI) to show the ship
                for (int j = c; j < c + shipLength; j++) {
                    playerButtons[r][j].setBackground(new Color(43, 194, 14));
                }
            }
            // update the ships positions --> row, col, and direction respectively
            shipPositions[shipIndex][0] = r;
            shipPositions[shipIndex][1] = c;
            shipPositions[shipIndex][2] = shipDir;
        }
    }

    /**
     * This method will clear/delete a chosen ship off the player's board
     * 
     * @param r
     * @param c
     * @param shipIndex
     * @param shipLength
     * @param shipDir
     * @param playerButtons
     * @param playerGrid
     * @param shipPositions
     */
    public void clearShip(int r, int c, int shipIndex, int shipLength, int shipDir, JButton playerButtons[][],
            int playerGrid[][], int shipPositions[][]) {
        int rowEnd;
        int colEnd;
        int rowStart;
        int colStart;
        // vertical
        if (shipDir == 0) {
            // clear array and GUI board where ship was
            for (int x = r; x < r + shipLength; x++) {
                playerGrid[x][c] = 0;
                playerButtons[x][c].setBackground(new Color(111,111,111));
            }
            // determine squares surrounding the ships
            // if ships are at the border of the gird, take 9 instead of r + 1/c + 1
            // and take 0 instead of c - 1 / r - 1, otherwise result in out of bounds
            rowEnd = Math.min(playerGrid.length - 1, r + shipLength);
            colEnd = Math.min(playerGrid[0].length - 1, c + 1);
            rowStart = Math.max(0, r - 1);
            colStart = Math.max(0, c - 1);
        }
        // horizontal
        else {
            // clear array and GUI board where ship was
            for (int x = c; x < c + shipLength; x++) {
                playerGrid[r][x] = 0;
                playerButtons[r][x].setBackground(new Color(111,111,111));
            }
            // same explanation as vertical
            rowEnd = Math.min(playerGrid.length - 1, r + 1);
            colEnd = Math.min(playerGrid[0].length - 1, c + shipLength);
            rowStart = Math.max(0, r - 1);
            colStart = Math.max(0, c - 1);
        }
        // clear border surrounding the ships
        for (int x = rowStart; x <= rowEnd; x++) {
            for (int y = colStart; y <= colEnd; y++) {
                if (playerGrid[x][y] != 0) {
                    // add 1 to bordering grids because one ship is removed
                    playerGrid[x][y] = playerGrid[x][y] + 1;
                }
            }
        }
        // reset coordinates of ship, row and col
        shipPositions[shipIndex][0] = -1;
        shipPositions[shipIndex][1] = -1;
        shipPositions[shipIndex][2] = 0; // default to vertical/0
    }

    /**
     * This method will clear the board (can be used for player and computer),
     * deleting all the ships that have been placed
     * 
     * @param playerButtons
     * @param playerGrid
     * @param shipPositions
     */
    public void clearBoard(JButton buttons[][], int grid[][], int shipPositions[][]) {
        // clear GUI board
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setBackground(new Color(111,111,111));
            }
        }
        // clear grid values
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = 0;
            }
        }
        // clear all data on ship positions
        for (int i = 0; i < shipPositions.length; i++) {
            for (int j = 0; j < shipPositions[i].length; j++) {
                // j == 2 kept at 0 bc represents vertical, all other cells are set default to -1
                if (j != 2) {
                    shipPositions[i][j] = -1;
                }
            }
        }
    }

    /**
     * This method will determine if all ships have been placed on the board
     * 
     * @param shipPositions
     * @return true or false depending on state of all ships
     */
    public boolean allShipsPlaced(int shipPositions[][]) {
        for (int i = 0; i < shipPositions.length; i++) {
            // only need to check row because the position values are updated together
            // if row is -1, col should be too, so no need to check
            if (shipPositions[i][0] == -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method will HIGHLIGHT the ship chosen by the player
     * by setting the grids of the ship back to a grey colour
     * 
     * @param r
     * @param c
     * @param shipLength
     * @param shipDir
     * @param playerButtons
     */
    public void highlightShip(int r, int c, int shipLength, int shipDir, JButton playerButtons[][]) {
        // highlight ship to a neon green colour
        // if ship is vertical
        if (shipDir == 0) {
            for (int x = r; x < r + shipLength; x++) {
                playerButtons[x][c].setBackground(new Color(43, 194, 14));
            }
        }
        // if ship is horizontal
        else {
            for (int x = c; x < c + shipLength; x++) {
                playerButtons[r][x].setBackground(new Color(43, 194, 14));
            }
        }
    }

    /**
     * This method will UNHIGHLIGHT the ship chosen
     * by setting the grids of the ship back to a grey colour
     * 
     * @param r
     * @param c
     * @param shipLength
     * @param shipDir
     * @param playerButtons
     */
    public void unhighlightShip(int r, int c, int shipLength, int shipDir, JButton playerButtons[][]) {
        // unhighlight the ship by returning it to its original dark grey colour
        // if ship is vertical
        if (shipDir == 0) {
            
            for (int x = r; x < r + shipLength; x++) {
                playerButtons[x][c].setBackground(new Color(36, 36, 36));
            }
        }
        // if ship is horizontal
        else {
            for (int x = c; x < c + shipLength; x++) {
                playerButtons[r][x].setBackground(new Color(36, 36, 36));
            }
        }
    }
}
