package edu.princeton.cs.module_1;

//Using algs4.jar. Under construction. Write a program RandomWord.java that reads a sequence of words from standard input and
// prints one of those words uniformly at random. Do not store the words in an array or list. Instead, use Knuth’s method:
// when reading the ith word, select it with probability 1/i
//to be the champion, replacing the previous champion. After reading all of the words, print the surviving champion.
//~/Desktop/hello> javac-algs4 RandomWord.java
//
//~/Desktop/hello> java-algs4 RandomWord
//heads tails
//tails
//
//~/Desktop/hello> java-algs4 RandomWord
//heads tails
//heads
//
//~/Desktop/hello> more animals8.txt
//ant bear cat dog
//emu fox goat horse
//
//~/Desktop/hello> java-algs4 RandomWord < animals8.txt
//        emu
//
//~/Desktop/hello> java-algs4 RandomWord < animals8.txt
//        bear

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {

        String ans = null;
        double count = 0;

        while (!StdIn.isEmpty()) {
            count++;
            String word = StdIn.readString();
            if (StdRandom.bernoulli(1 / count)) {
                ans = word;
            }
        }

        if (ans != null) {
            StdOut.println(ans);
        }
    }
}
