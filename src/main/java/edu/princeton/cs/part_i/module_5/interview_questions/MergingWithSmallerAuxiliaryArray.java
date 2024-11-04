package edu.princeton.cs.module_5.interview_questions;

import java.util.Arrays;
import java.util.Random;

public class MergingWithSmallerAuxiliaryArray {
    private final int nums[];

    public MergingWithSmallerAuxiliaryArray(int n) {
        nums = new int[n];
        int min = 1;
        int max = 100;

        Random rnd = new Random();
        for (int i = 0; i < n; i++) {
            nums[i] = min + rnd.nextInt(max - min + 1);
        }

        sort(nums, 0, n / 2);
        sort(nums, n / 2, n - 1);
    }

    private void mergeSort(int[] arr) {
        sort(arr, 0, arr.length);
    }

    private void sort(int[] arr, int l, int r) {
        if (l < r) {
            int m = l + (r - l) / 2;
            sort(arr, l, m);
            sort(arr, m + 1, r);
            merge(arr, l, r, m);
        }
    }

    private void merge(int[] arr, int l, int r, int m) {

        int leftSize = m - l + 1;
        int rightSize = r - m;

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
    }


    @Override
    public String toString() {
        return "MergingWithSmallerAuxiliaryArray{" +
                "nums=" + Arrays.toString(nums) +
                '}';
    }

    private void SortPartiallySortedArray() {
        int n = nums.length;
        int mid = n / 2;

        // Temporary array to hold the first half (size n/2)
        int[] tmp = new int[mid];

        // Copy the first half of nums into tmp
        System.arraycopy(nums, 0, tmp, 0, mid);

        int i = 0; // Pointer for the tmp (first half)
        int j = mid; // Pointer for the second half in nums
        int k = 0; // Pointer for the original nums array

        // Merge the two halves: tmp (first half) and nums[mid..n] (second half)
        while (i < mid && j < n) {
            if (tmp[i] <= nums[j]) {
                nums[k++] = tmp[i++];
            } else {
                nums[k++] = nums[j++];
            }
        }

        // Copy any remaining elements from the first half (tmp) if necessary
        while (i < mid) {
            nums[k++] = tmp[i++];
        }

        // No need to copy the second half because it's already in place in nums
    }

    private boolean isSorted() {
        for (int i = 1; i < nums.length; i++) {
            if (nums[i - 1] > nums[i]) {
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) {
        int n = 20;
        MergingWithSmallerAuxiliaryArray mwsaa = new MergingWithSmallerAuxiliaryArray(n);

        System.out.println(mwsaa);

        System.out.println("Is sorted? " + mwsaa.isSorted());

        mwsaa.SortPartiallySortedArray();

        System.out.println(mwsaa);

        System.out.println("Is sorted? " + mwsaa.isSorted());
    }

}
