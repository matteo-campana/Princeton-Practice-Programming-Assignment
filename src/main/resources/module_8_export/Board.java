

import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    private final int[][] tiles;


    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        // Constructor implementation
        // this.tiles = Arrays.copyOf(tiles, tiles.length);
        this.tiles = new int[tiles.length][];
        for (int i = 0; i < tiles.length; i++) {
            this.tiles[i] = Arrays.copyOf(tiles[i], tiles[i].length);
        }
    }


    // string representation of this board
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        tmp.append(this.dimension());
        for (int[] tile : this.tiles) {
            tmp.append("\n").append(Arrays.toString(tile).replace("[", "").replace("]", "").replace(",", ""));
        }
        return tmp.toString();
    }


    // board dimension n
    public int dimension() {
        return this.tiles.length;
    }


    // Number of tiles out of place (Hamming distance)
    public int hamming() {
        int hammingDistance = 0;
        int n = this.dimension();  // Get the size of the board (n x n)

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int tile = this.tiles[i][j];

                // Skip the blank tile (usually represented by 0)
                if (tile == 0) continue;

                // Expected tile number at (i, j) is i * n + j + 1
                int expectedTile = i * n + j + 1;

                // Count it as out of place if it doesn't match the expected value
                if (tile != expectedTile) {
                    hammingDistance++;
                }
            }
        }

        return hammingDistance;
    }


    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int distance = 0;
        int n = this.dimension();  // Get the size of the board (n x n)

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int tile = this.tiles[i][j];

                // Skip the blank tile (usually represented by 0)
                if (tile == 0) continue;

                // Expected position in the goal state (1-based index)
                int expectedRow = (tile - 1) / n;  // Expected row for the tile
                int expectedCol = (tile - 1) % n;  // Expected column for the tile

                // Manhattan distance for the current tile
                int distanceY = Math.abs(i - expectedRow);
                int distanceX = Math.abs(j - expectedCol);

                distance += distanceY + distanceX;
            }
        }
        return distance;
    }


    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0;
    }


    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (!(this.getClass() == y.getClass())) return false;
        Board ob = (Board) y;
        if (this.dimension() != ob.dimension()) return false;
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                if (this.tiles[i][j] != ob.tiles[i][j]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> boards = new ArrayList<Board>();
        int[] zeroPos = findZero();

        Board newBoard;

        // build the top
        if (zeroPos[0] > 0 && this.tiles.length > 1) {
            newBoard = new Board(this.tiles);
            newBoard.swapTiles(zeroPos[0], zeroPos[1], zeroPos[0] - 1, zeroPos[1]);
            boards.add(newBoard);
        }

        // build the bottom
        if (zeroPos[0] < this.dimension() - 1 && this.tiles.length > 1) {
            newBoard = new Board(this.tiles);
            newBoard.swapTiles(zeroPos[0], zeroPos[1], zeroPos[0] + 1, zeroPos[1]);
            boards.add(newBoard);
        }

        // build right
        if (zeroPos[1] < this.dimension() - 1 && this.tiles.length > 1) {
            newBoard = new Board(this.tiles);
            newBoard.swapTiles(zeroPos[0], zeroPos[1], zeroPos[0], zeroPos[1] + 1);
            boards.add(newBoard);
        }

        // build left
        if (zeroPos[1] > 0 && this.tiles.length > 1) {
            newBoard = new Board(this.tiles);
            newBoard.swapTiles(zeroPos[0], zeroPos[1], zeroPos[0], zeroPos[1] - 1);
            boards.add(newBoard);
        }

        return boards;
    }

    private int[] findZero() {
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                if (this.tiles[i][j] == 0) return new int[]{i, j};
            }
        }
        throw new RuntimeException("Zero not present in tiles set");
    }

    //    private int[] findZeroPos() {
    //        int[][] indexArr = new int[this.dimension() * this.dimension()][2];
    //
    //        for (int i = 0; i < this.dimension(); i++) {
    //            for (int j = 0; j < this.dimension(); j++) {
    //                indexArr[i * j][0] = i;
    //                indexArr[i * j][1] = j;
    //            }
    //        }
    //
    //        int[] result = quickSelect(this.tiles, indexArr, 0, indexArr.length, 0);
    //
    //        if (this.tiles[result[0]][result[1]] == 0) return result;
    //        throw new RuntimeException("Zero not in the tiles set");
    //    }

    private int[] quickSelect(int[][] arr, int[][] indexArr, int left, int right, int target) {
        if (left == right) return indexArr[left]; // there is only one element

        int pivotIndex = partition(arr, indexArr, left, right);
        int pivotValue = arr[indexArr[pivotIndex][0]][indexArr[pivotIndex][1]];

        if (pivotValue == target) {
            return indexArr[pivotIndex]; // if the pivot value is zero return it
        } else if (pivotValue < target) {
            return quickSelect(arr, indexArr, pivotIndex + 1, right, target); // search the right part
        } else {
            return quickSelect(arr, indexArr, 0, pivotIndex - 1, target); // search the left part
        }

    }

    private int partition(int[][] arr, int[][] indexArr, int left, int right) {
        int pivotValue = arr[indexArr[right][0]][indexArr[right][1]];
        int storeIndex = left;
        int row, col;
        for (int i = 0; i < right; i++) {
            row = indexArr[i][0];
            col = indexArr[i][1];

            if (arr[row][col] < pivotValue) {
                swap(indexArr, storeIndex, i);
                storeIndex++;
            }
        }

        swap(indexArr, storeIndex, right); // move the pivot to its final place
        return storeIndex;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int rndY1, rndX1, rndY2, rndX2;
        do {
            rndY1 = StdRandom.uniformInt(0, dimension());
            rndX1 = StdRandom.uniformInt(0, dimension());
            rndY2 = StdRandom.uniformInt(0, dimension());
            rndX2 = StdRandom.uniformInt(0, dimension());
        } while (rndY1 == rndY2 && rndX1 == rndX2);

        Board twinBoard = new Board(this.tiles);
        twinBoard.swapTiles(rndY1, rndX1, rndY2, rndX2);

        return twinBoard;
    }

    private void swapTiles(int i, int j, int a, int b) {
        int tmp = this.tiles[i][j];
        this.tiles[i][j] = this.tiles[a][b];
        this.tiles[a][b] = tmp;
    }

    //    private void swap(int[][] m, int i, int j, int a, int b) {
    //        int tmp = m[i][j];
    //        m[i][j] = m[a][b];
    //        m[a][b] = tmp;
    //    }

    // Swap two elements in the index array
    private static void swap(int[][] indexArray, int i, int j) {
        int[] temp = indexArray[i];
        indexArray[i] = indexArray[j];
        indexArray[j] = temp;
    }


    // unit testing (not graded)
    public static void main(String[] args) {
        // Example tiles for testing
        int[][] tiles = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        };

        // Create a board instance
        Board board = new Board(tiles);

        // Test toString method
        System.out.println("Board:");
        System.out.println(board);

        // Test dimension method
        System.out.println("Dimension: " + board.dimension());

        // Test hamming method
        System.out.println("Hamming: " + board.hamming());

        // Test manhattan method
        System.out.println("Manhattan: " + board.manhattan());

        // Test isGoal method
        System.out.println("Is goal: " + board.isGoal());

        // Test equals method
        Board sameBoard = new Board(tiles);
        System.out.println("Equals same board: " + board.equals(sameBoard));

        // Test neighbors method
        System.out.println("Neighbors:");
        for (Board neighbor : board.neighbors()) {
            System.out.println(neighbor);
        }

        // Test twin method
        System.out.println("Twin board:");
        System.out.println(board.twin());
    }


}