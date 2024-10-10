package edu.princeton.cs.module_5;

import java.util.Arrays;
import java.util.Random;

public class CountingInversions {
    private final int[] nums;
    private final int[] unsortedNums;


    public CountingInversions(int n) {
        nums = new int[n];
        unsortedNums = new int[n];

        int min = 1;
        int max = 100;

        Random rnd = new Random();
        for (int i = 0; i < n; i++) {
            nums[i] = min + rnd.nextInt(max + 1 - min);
        }
        System.arraycopy(nums, 0, unsortedNums, 0, n);
    }


    private int sort(int[] arr, int l, int r) {
        int inversion_count = 0;
        if (l < r) {
            int m = l + (r - l) / 2;
            inversion_count += sort(arr, l, m);
            inversion_count += sort(arr, m + 1, r);
            inversion_count += merge(arr, l, r, m);
        }

        return inversion_count;
    }

    private int merge(int[] arr, int l, int r, int m) {

        int leftSize = m - l + 1;
        int rightSize = r - m;
        int inversionsCount = 0;

        int[] left = new int[leftSize];
        int[] right = new int[rightSize];

        System.arraycopy(arr, l, left, 0, leftSize);
        System.arraycopy(arr, m + 1, right, 0, rightSize);

        int k = l, i = 0, j = 0;

        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {  // Use <= to maintain stable sort
                arr[k] = left[i];
                i++;
            } else {
                arr[k] = right[j];
                inversionsCount += (m + 1) - (l + i);
                j++;
            }
            k++;
        }

        while (i < left.length) {
            arr[k] = left[i];
            i++;
            k++;
        }

        while (j < right.length) {
            arr[k] = right[j];
            j++;
            k++;
        }

        return inversionsCount;
    }

    @Override
    public String toString() {
        return "CountingInversions{" +
                "unsortedNums=" + Arrays.toString(unsortedNums) +
                ", nums=" + Arrays.toString(nums) +
                '}';
    }

    public static void main(String[] args) {
        int n = 30;
        CountingInversions ci = new CountingInversions(n);

        System.out.println(ci);

        int inversionsCount = ci.sort(ci.nums, 0, ci.nums.length - 1);

        System.out.println("Inversions Count: " + inversionsCount);
    }
}
