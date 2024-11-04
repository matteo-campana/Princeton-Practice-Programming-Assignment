package edu.princeton.cs.part_i.module_7.interview_questions;

import java.util.ArrayList;
import java.util.Random;

public class SelectionInTwoSortedArrays {


    private static void version1() {
        ArrayList<Integer> a = new ArrayList<Integer>();
        ArrayList<Integer> b = new ArrayList<Integer>();
        Random rnd = new Random();

        int n = 50;

        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                a.add(i);
            } else {
                b.add(i);
            }
        }

        assert a.size() == b.size();

        int k = findKthVersion(a, b, 0, 0, a.size() - 1, b.size() - 1, a.size() + b.size() / 2);
        System.out.println("a[] : " + a);
        System.out.println("b[] : " + b);
        System.out.println("Version 1 output: " + k);
    }

    private static Integer findKthVersion(ArrayList<Integer> a, ArrayList<Integer> b, int startA, int startB, int endA, int endB, int k) {

        if (startA > endA) return a.get(startA + k);
        if (startB > endB) return b.get(startB + k);

        int midA = startA + (endA - startA) / 2;
        int midB = startB + (endB - startB) / 2;

        if (midA + midB < k) {
            if (a.get(midA) < b.get(midB)) {
                return findKthVersion(a, b, startA, startB + 1, endA, endB, k - (midA - startA + 1));
            } else {
                return findKthVersion(a, b, startA, startB + 1, endA, endB, k - (midB - startB + 1));
            }

        } else {
            if (a.get(midA) < b.get(midB)) {
                return findKthVersion(a, b, startA, startB, endA, midB - 1, k);
            } else {
                return findKthVersion(a, b, startA, startB, midA - 1, endB, k);
            }
        }

    }

    private static void version2() {
        ArrayList<Integer> a = new ArrayList<Integer>();
        ArrayList<Integer> b = new ArrayList<Integer>();
        Random rnd = new Random();

        int n = 50;

        for (int i = 0; i < n; i++) {
            if (rnd.nextBoolean()) {
                a.add(i);
            } else {
                b.add(i);
            }
        }
        int kth;
        kth = findKthVersion(a, b, 0, 0, a.size() - 1, b.size() - 1, a.size() + b.size() / 2);


        System.out.println("a[] : " + a);
        System.out.println("b[] : " + b);
        System.out.println("Version 2 output: " + kth);
    }

    private static void version3() {
        ArrayList<Integer> a = new ArrayList<Integer>();
        ArrayList<Integer> b = new ArrayList<Integer>();
        Random rnd = new Random();

        int n = 50;

        for (int i = 0; i < n; i++) {
            if (rnd.nextBoolean()) {
                a.add(i);
            } else {
                b.add(i);
            }
        }
        int k, kth;
        k = rnd.nextInt(a.size() + b.size() - 1);
        kth = findKthVersion(a, b, 0, 0, a.size() - 1, b.size() - 1, k);


        System.out.println("a[] : " + a);
        System.out.println("b[] : " + b);
        System.out.println("k :   " + k);
        System.out.println("Version 3 output: " + kth);
    }


    public static void main(String[] args) {
        version1();
        System.out.println("=".repeat(50));
        version2();
        System.out.println("=".repeat(50));
        version3();
    }
}
