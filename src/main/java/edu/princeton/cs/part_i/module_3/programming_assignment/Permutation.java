package edu.princeton.cs.part_i.module_3.programming_assignment;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        RandomizedQueue<String> rqueue = new RandomizedQueue<String>();
        for (int i = 0; i < n; i++) {
            rqueue.enqueue(StdIn.readString());
        }
        for (int i = 0; i < n; i++) {
            StdOut.println(rqueue.dequeue());
        }

    }
}