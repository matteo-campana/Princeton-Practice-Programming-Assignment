package edu.princeton.cs.part_i.module_8.interview_questions;


import java.util.Collections;
import java.util.PriorityQueue;

public class DynamicMedianV2 {
    private final PriorityQueue<Integer> minHeap;
    private final PriorityQueue<Integer> maxHeap;

    public DynamicMedianV2() {
        minHeap = new PriorityQueue<Integer>();
        maxHeap = new PriorityQueue<Integer>(Collections.reverseOrder());
    }

    private void insert(Integer value) {
        if (minHeap.isEmpty() && maxHeap.isEmpty()) {
            minHeap.add(value);
            return;
        }

        if (value > this.findMedian()) {
            minHeap.add(value);
        } else {
            maxHeap.add(value);
        }

        this.balance();
    }

    private void balance() {

        while (minHeap.size() > maxHeap.size() + 1) {
            Integer minHeapValue = minHeap.poll();
            maxHeap.add(minHeapValue);
        }

        while (maxHeap.size() > minHeap.size() + 1) {
            Integer maxHeapValue = maxHeap.poll();
            minHeap.add(maxHeapValue);
        }
    }

    private Integer findMedian() {
        if (this.minHeap.isEmpty() && this.maxHeap.isEmpty()) throw new RuntimeException("No items...");
        if (maxHeap.size() > minHeap.size()) return maxHeap.peek();
        return minHeap.peek();
    }

    private Integer removeMedian() {

        Integer median = this.findMedian();

        if (median.equals(minHeap.peek())) {
            minHeap.poll();
        } else {
            maxHeap.poll();
        }

        this.balance();

        return median;
    }


    public static void main(String[] args) {
        DynamicMedianV2 medianHeap = new DynamicMedianV2();

        int[] numbers = {5, 15, 1, 3, 8, 7, 9, 2, 6, 10};

        for (int number : numbers) {
            medianHeap.insert(number);
            System.out.println("Inserted: " + number + ", Current Median: " + medianHeap.findMedian());
        }

        System.out.println("Removing medians:");
        while (!medianHeap.minHeap.isEmpty() || !medianHeap.maxHeap.isEmpty()) {
            System.out.println("Removed Median: " + medianHeap.removeMedian());
        }

    }
}
