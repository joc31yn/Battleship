package battleshipGame;

public class ShipMethods {
    /**
     * This method will check if the position chosen to place the ship is valid
     * @param grid
     * @param r
     * @param c
     * @param dir
     * @param length
     * @return
     */
    public boolean isShipValid(int grid[][], int r, int c, int dir, int length) {
        // check if the ship exceeds the boundaries of the game board 
        // and if another ship has already taken the area

        // vertical
        if (dir == 0) {
            if (r + length - 1 >= grid.length) {
                return false;
            }
            for (int i = r; i < r + length; i++) {
                if (grid[i][c] != 0) {
                    return false;
                }
            }
        }
        // horizontal
        else {
            if (c + length - 1 >= grid[0].length) {
                return false;
            }
            for (int i = c; i < c + length; i++) {
                if (grid[r][i] != 0) {
                    return false;
                }
            }

        }
        return true;
    }

    /**
     * This method will place the ship on its chosen position
     * @param grid
     * @param r
     * @param c
     * @param dir
     * @param length
     * @param shipNum
     */
    public void placeShip(int grid[][], int r, int c, int dir, int length, int shipNum) {
        int rowEnd;
        int colEnd;
        int rowStart;
        int colStart;
        // vertical
        if (dir == 0) {
            // mark the ship
            for (int i = r; i < r + length; i++) {
                grid[i][c] = shipNum;
            }

            // determine squares surrounding the ships
            // if ships are at the border of the gird, take 9 instead of r + 1/c + 1
            // and take 0 instead of c - 1 / r - 1, otherwise result in out of bounds
            rowEnd = Math.min(grid.length - 1, r + length);
            colEnd = Math.min(grid[0].length - 1, c + 1);
            rowStart = Math.max(0, r - 1);
            colStart = Math.max(0, c - 1);

        } else {
            // mark the ship
            for (int i = c; i < c + length; i++) {
                grid[r][i] = shipNum;
            }
            rowEnd = Math.min(grid.length - 1, r + 1);
            colEnd = Math.min(grid[0].length - 1, c + length);
            rowStart = Math.max(0, r - 1);
            colStart = Math.max(0, c - 1);
        }
        // mark the ship's surrounding border
        for (int i = rowStart; i <= rowEnd; i++) {
            for (int j = colStart; j <= colEnd; j++) {
                if (grid[i][j] != shipNum) {
                    grid[i][j] = grid[i][j] - 1;
                }
            }
        }
    }
}