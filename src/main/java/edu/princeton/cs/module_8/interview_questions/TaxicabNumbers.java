package edu.princeton.cs.module_8.interview_questions;

import java.util.PriorityQueue;

class TaxicabNumber {
    // Class to represent a tuple of (sum, a, b)
    static class CubeSum implements Comparable<CubeSum> {
        int sum;
        int a, b;

        CubeSum(int sum, int a, int b) {
            this.sum = sum;
            this.a = a;
            this.b = b;
        }

        // For sorting CubeSum objects by their sum in the priority queue
        @Override
        public int compareTo(CubeSum other) {
            return Integer.compare(this.sum, other.sum);
        }
    }

    public static void findTaxicabNumbers(int n) {
        PriorityQueue<CubeSum> pq = new PriorityQueue<>();

        // Step 1: Insert the first sum for each a, i.e., a^3 + 1^3
        for (int a = 1; a <= n; a++) {
            pq.offer(new CubeSum(a * a * a + 1 * 1 * 1, a, 1));
        }

        // Step 2: Process the priority queue to find taxicab numbers
        CubeSum prev = null;
        while (!pq.isEmpty()) {
            CubeSum curr = pq.poll();

            // Check if the current sum equals the previous sum (i.e., a taxicab number)
            if (prev != null && curr.sum == prev.sum) {
                // Print the taxicab number
                System.out.println("Sum: " + curr.sum + ", Pairs: (" + prev.a + ", " + prev.b + ") and (" + curr.a + ", " + curr.b + ")");
            }

            // Update the previous CubeSum
            prev = curr;

            // Insert the next sum a^3 + (b+1)^3 into the queue, if b+1 <= n
            if (curr.b + 1 <= n) {
                pq.offer(new CubeSum(curr.a * curr.a * curr.a + (curr.b + 1) * (curr.b + 1) * (curr.b + 1), curr.a, curr.b + 1));
            }
        }
    }

    public static void main(String[] args) {
        int n = 12; // Set the limit n
        findTaxicabNumbers(n);
    }
}
