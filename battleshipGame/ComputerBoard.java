package battleshipGame;

import java.io.*;

public class ComputerBoard {

    /**
     * This method will place the ships on the computers grid
     * @param computerGrid
     */
    public void placeShips(int computerGrid[][], int computerShipPos[][], int shipLengths[]) {
        ShipMethods shipPlacements = new ShipMethods(); // instantiate shipMethods class

        // generate a coordinate and direction for each ship
        for (int i = 0; i < shipLengths.length; i++) {
            int r = (int) (Math.random() * 10);
            int c = (int) (Math.random() * 10);
            int dir = (int) (Math.random() * 2);

            // keep regenerating new coordinates and direction if the placement is invalid on the grid
            while (!shipPlacements.isShipValid(computerGrid, r, c, dir, shipLengths[i])) {
                r = (int) (Math.random() * 10);
                c = (int) (Math.random() * 10);
                dir = (int) (Math.random() * 2);
            }
            // mark the positions of the chosen coordinates
            computerShipPos[i][0] = r;
            computerShipPos[i][1] = c;
            computerShipPos[i][2] = dir;
            
            // once valid, place the ship on the array
            shipPlacements.placeShip(computerGrid, r, c, dir, shipLengths[i], i + 1);
        }
    }

    /**
     * This method will write the location of the computer's ships to an answer file
     * 
     * @param arr
     * @throws IOException
     */
    public void writeToFile(int arr[][]) throws IOException {
        PrintWriter write = new PrintWriter("ComputerBoardAns.txt");

        write.println(". --> represents empty square");
        write.println("x --> represents the location of the ships");
        write.println("_ --> represents the border surrounding the ships where other ships can't be placed");
        write.println("\nComputer ship answers :D");
        // write the ans board to file
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (arr[i][j] < 0) {
                    write.print("_ ");
                } else if (arr[i][j] == 0) {
                    write.print(". ");
                } else {
                    write.print("x ");
                }
            }
            write.println();
        }
        write.println("\nOriginal grid values");
        write.println("0 --> empty");
        write.println("-1 to -4 --> borders surrounding ships");
        write.println("1 to 5 --> ships\n");
        // write original grid values to file
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                // different spacing for negative numbers
                if (arr[i][j] < 0) {
                    write.print(arr[i][j] + " ");
                } else {
                    write.print(arr[i][j] + "  ");
                }
            }
            write.println();
        }
        write.close(); // close PrintWriter
    }
}