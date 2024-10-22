package edu.princeton.cs.module_8.programming_assignments;

import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Solver {

    private final MinPQ<SearchNode> pqManhattan;
    private int movesCount = -1;  // Default is -1 to indicate unsolvable
    private boolean isSolvable = true;
    private final ArrayList<Board> solutionArray;

    // Helper class to encapsulate board and its metadata for A*
    private static class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode previousNode;
        private final int priority;

        public SearchNode(Board board, int moves, SearchNode previousNode) {
            this.board = board;
            this.moves = moves;
            this.previousNode = previousNode;
            this.priority = board.manhattan() + moves;
        }

        @Override
        public int compareTo(SearchNode that) {
            return Integer.compare(this.priority, that.priority);
        }
    }


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        // Initialize the priority queue
        pqManhattan = new MinPQ<>();
        solutionArray = new ArrayList<>();

        // Add initial board to the priority queue
        pqManhattan.insert(new SearchNode(initial, 0, null));

        SearchNode solutionNode = solve();
        if (solutionNode == null) {
            isSolvable = false;
        } else {
            movesCount = solutionNode.moves;
            SearchNode currentNode = solutionNode;
            while (currentNode != null) {
                solutionArray.add(currentNode.board);
                currentNode = currentNode.previousNode;
            }
            Collections.reverse(solutionArray);
        }
    }

    private SearchNode solve() {
        while (!pqManhattan.isEmpty()) {
            SearchNode currentNode = pqManhattan.delMin();

            // Check if the current board is the goal
            if (currentNode.board.isGoal()) {
                return currentNode;
            }

            // Expand neighbors
            for (Board neighbor : currentNode.board.neighbors()) {
                // Don't insert the board that is the same as the previous one
                if (currentNode.previousNode == null || !neighbor.equals(currentNode.previousNode.board)) {
                    pqManhattan.insert(new SearchNode(neighbor, currentNode.moves + 1, currentNode));
                }
            }
        }
        return null;  // Unsolvable case
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return this.movesCount;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return this.isSolvable ? solutionArray : null;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }
}
