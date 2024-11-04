package edu.princeton.cs.module_5.interview_questions;

import java.util.Random;

public class ShufflingALinkedList {

    // Linked list node class
    static class ListNode {
        int val;
        ListNode next;

        public ListNode(int val) {
            this.val = val;
            this.next = null;
        }
    }

    private static final Random random = new Random();

    // Function to shuffle the linked list
    private static ListNode shuffle(ListNode head) {
        // Base case: if the list is empty or has only one node
        if (head == null || head.next == null) {
            return head;
        }

        // Split the list into two halves
        ListNode mid = getMiddle(head);
        ListNode rightHalf = mid.next;
        mid.next = null; // Break the list into two parts

        // Recursively shuffle the two halves
        ListNode left = shuffle(head);
        ListNode right = shuffle(rightHalf);

        // Merge the two halves randomly
        return mergeRandomly(left, right);
    }

    // Function to merge two lists randomly
    private static ListNode mergeRandomly(ListNode left, ListNode right) {
        ListNode dummy = new ListNode(0); // Dummy node to help merge
        ListNode current = dummy;

        // Randomly merge the two lists
        while (left != null && right != null) {
            if (random.nextBoolean()) {
                current.next = left;
                left = left.next;
            } else {
                current.next = right;
                right = right.next;
            }
            current = current.next;
        }

        // Append the remaining elements from left or right (if any)
        if (left != null) {
            current.next = left;
        } else if (right != null) {
            current.next = right;
        }

        return dummy.next;
    }

    // Function to find the middle of the linked list
    private static ListNode getMiddle(ListNode head) {
        if (head == null) return head;

        ListNode slow = head;
        ListNode fast = head.next;

        // Fast-slow pointer technique to find middle
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    // Helper function to print the linked list
    private static void printList(ListNode head) {
        ListNode current = head;
        while (current != null) {
            System.out.print(current.val + " ");
            current = current.next;
        }
        System.out.println();
    }

    // Driver function to test the shuffling algorithm
    public static void main(String[] args) {
        // Create a linked list
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = new ListNode(5);
        head.next.next.next.next.next = new ListNode(6);

        System.out.println("Original list:");
        printList(head);

        // Shuffle the linked list
        head = shuffle(head);

        System.out.println("Shuffled list:");
        printList(head);
    }
}
