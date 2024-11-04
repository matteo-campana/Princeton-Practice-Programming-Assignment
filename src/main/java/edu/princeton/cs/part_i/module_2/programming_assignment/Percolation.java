package edu.princeton.cs.part_i.module_2.programming_assignment;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private final int n;                           // grid dimension
    private boolean[][] grid;                      // to keep track of open sites
    private int openSites;                         // number of open sites
    private final WeightedQuickUnionUF ufPercolation;  // union-find for percolation
    private final WeightedQuickUnionUF ufFullness;     // union-find for fullness check
    private final int topVirtualSite;              // virtual top site
    private final int bottomVirtualSite;           // virtual bottom site (only for ufPercolation)

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }
        this.n = n;
        this.grid = new boolean[n][n];
        this.openSites = 0;

        // Union-Find with n*n sites + 2 virtual sites for percolation check
        this.ufPercolation = new WeightedQuickUnionUF(n * n + 2);
        // Union-Find with only 1 virtual top site for fullness check (no virtual bottom)
        this.ufFullness = new WeightedQuickUnionUF(n * n + 1);

        this.topVirtualSite = 0;
        this.bottomVirtualSite = n * n + 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        if (isOpen(row, col)) {
            return;  // already open
        }

        grid[row - 1][col - 1] = true;  // open site
        openSites++;                    // increment open site count

        int currentSite = xyTo1D(row, col);

        // Connect to adjacent open sites (up, down, left, right)
        if (row == 1) {
            ufPercolation.union(currentSite, topVirtualSite);  // connect top row to virtual top site
            ufFullness.union(currentSite, topVirtualSite);     // also connect in ufFullness
        }
        if (row == n) {
            ufPercolation.union(currentSite, bottomVirtualSite);  // connect bottom row to virtual bottom site
        }

        // Connect to neighboring open sites in both uf structures
        if (row > 1 && isOpen(row - 1, col)) {
            ufPercolation.union(currentSite, xyTo1D(row - 1, col));  // connect to site above
            ufFullness.union(currentSite, xyTo1D(row - 1, col));     // connect in ufFullness
        }
        if (row < n && isOpen(row + 1, col)) {
            ufPercolation.union(currentSite, xyTo1D(row + 1, col));  // connect to site below
            ufFullness.union(currentSite, xyTo1D(row + 1, col));     // connect in ufFullness
        }
        if (col > 1 && isOpen(row, col - 1)) {
            ufPercolation.union(currentSite, xyTo1D(row, col - 1));  // connect to site on the left
            ufFullness.union(currentSite, xyTo1D(row, col - 1));     // connect in ufFullness
        }
        if (col < n && isOpen(row, col + 1)) {
            ufPercolation.union(currentSite, xyTo1D(row, col + 1));  // connect to site on the right
            ufFullness.union(currentSite, xyTo1D(row, col + 1));     // connect in ufFullness
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        return ufFullness.find(xyTo1D(row, col)) == ufFullness.find(topVirtualSite);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return ufPercolation.find(topVirtualSite) == ufPercolation.find(bottomVirtualSite);
    }

    // convert (row, col) to 1D index
    private int xyTo1D(int row, int col) {
        return (row - 1) * n + (col - 1) + 1;
    }

    // validate that (row, col) is a valid site
    private void validate(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException("row or col is out of bounds");
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = 5;
        Percolation perc = new Percolation(n);
        perc.open(1, 1);
        perc.open(2, 1);
        perc.open(3, 1);
        perc.open(4, 1);
        perc.open(5, 1);

        System.out.println("Percolates: " + perc.percolates());
        System.out.println("Number of open sites: " + perc.numberOfOpenSites());
    }
}
