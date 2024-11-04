package edu.princeton.cs.part_i.interview_questions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class NutsAndBolts {

    // Main method to test the algorithm
    public static void main(String[] args) {

        int n = 10;

        // Sample nuts and bolts represented by integers for simplicity
        ArrayList<Integer> nuts = new ArrayList<Integer>();
        ArrayList<Integer> bolts = new ArrayList<Integer>();

        for (int i = 0; i < n; i++) {
            nuts.add(i);
            bolts.add(i);
        }

        Collections.shuffle(nuts);
        Collections.shuffle(bolts);

        System.out.println("Unsorted nuts:\t" + nuts);
        System.out.println("Unsorted bolts:\t" + bolts);

        // Perform matching
        matchPairs(nuts, bolts, 0, nuts.size() - 1);

        // Display matched nuts and bolts
        System.out.println("=".repeat(50));
        System.out.println("Matched nuts:\t" + nuts);
        System.out.println("Matched bolts:\t" + bolts);
    }

    // Function to match pairs of nuts and bolts
    static void matchPairs(ArrayList<Integer> nuts, ArrayList<Integer> bolts, int low, int high) {
        if (low < high) {
            // Choose the first nut as the pivot for bolts partitioning
            int pivotIndex = partition(bolts, low, high, nuts.get(low));

            // Use the corresponding bolt to partition the nuts
            partition(nuts, low, high, bolts.get(pivotIndex));

            // Recursively match the left and right partitions
            matchPairs(nuts, bolts, low, pivotIndex - 1);
            matchPairs(nuts, bolts, pivotIndex + 1, high);
        }
    }

    // Partition the array such that elements smaller than the pivot come before
    // the pivot and elements greater than the pivot come after it.
    static int partition(ArrayList<Integer> arr, int low, int high, Integer pivot) {
        int i = low;
        for (int j = low; j < high; j++) {
            if (arr.get(j) < pivot) {
                swap(arr, i, j);
                i++;
            } else if (arr.get(j).equals(pivot)) {
                swap(arr, j, high);  // Move pivot element to the end
                j--;
            }
        }
        swap(arr, i, high);  // Move pivot to its final position
        return i;
    }

    // Utility function to swap elements
    static void swap(ArrayList<Integer> arr, int i, int j) {
        Integer temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }
}
