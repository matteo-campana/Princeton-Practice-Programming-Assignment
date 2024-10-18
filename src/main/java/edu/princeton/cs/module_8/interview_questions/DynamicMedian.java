package edu.princeton.cs.module_8.interview_questions;

import java.util.LinkedList;
import java.util.Queue;

public class DynamicMedian {
    private static class MedianBinaryHeap {
        private BinaryHeap maxheap;
        private BinaryHeap minHeap;

        private static class Node {
            private int value;
            private int depth;
            private Node parent;
            private Node leftChild;
            private Node rightChild;

            public Node(int value) {
                this.value = value;
            }

            public Node(int value, int depth, Node parent, Node leftChild, Node rightChild) {
                this.value = value;
                this.depth = depth;
                this.parent = parent;
                this.leftChild = leftChild;
                this.rightChild = rightChild;
            }

            public Node(int value, int depth, Node parent) {
                this.value = value;
                this.depth = depth;
                this.parent = parent;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }

            public int getDepth() {
                return depth;
            }

            public void setDepth(int depth) {
                this.depth = depth;
            }

            public Node getParent() {
                return parent;
            }

            public void setParent(Node parent) {
                this.parent = parent;
            }

            public Node getLeftChild() {
                return leftChild;
            }

            public void setLeftChild(Node leftChild) {
                this.leftChild = leftChild;
            }

            public Node getRightChild() {
                return rightChild;
            }

            public void setRightChild(Node rightChild) {
                this.rightChild = rightChild;
            }
        }

        private static class BinaryHeap {
            private Node head;
            private int height;
            private final boolean isMaxHeap;
            private int size;

            public int getHeight() {
                return height;
            }

            public boolean isMaxHeap() {
                return isMaxHeap;
            }

            public int getSize() {
                return size;
            }

            public Integer peek() {
                if (this.head != null) return this.head.getValue();
                return null;
            }

            public BinaryHeap(boolean isMaxHeap) {
                this.isMaxHeap = isMaxHeap;
                this.head = null;
                this.height = 0;
                this.size = 0;
            }

            // Insert a new value into the heap
            public void insert(int value) {
                Node newNode = new Node(value);
                this.size++;
                if (head == null) {
                    head = newNode;
                } else {
                    // Insert the new node at the correct position
                    insertAtCorrectPosition(head, newNode);
                }
                rebalance(newNode);
            }

            // Helper method to insert the new node at the correct position
            private void insertAtCorrectPosition(Node current, Node newNode) {
                // Use a queue to perform level order insertion
                Queue<Node> queue = new LinkedList<>();
                queue.add(current);
                while (!queue.isEmpty()) {
                    Node temp = queue.poll();
                    if (temp.leftChild == null) {
                        temp.leftChild = newNode;
                        newNode.parent = temp;
                        newNode.setDepth(temp.getDepth() + 1);
                        return;
                    } else {
                        queue.add(temp.leftChild);
                    }
                    if (temp.rightChild == null) {
                        temp.rightChild = newNode;
                        newNode.parent = temp;
                        newNode.setDepth(temp.getDepth() + 1);
                        return;
                    } else {
                        queue.add(temp.rightChild);
                    }
                }
            }

            // Rebalance the heap to maintain the heap property
            private void rebalance(Node node) {
                if (isMaxHeap) {
                    // Max-Heap: Ensure parent is greater than the child
                    while (node.parent != null && node.value > node.parent.value) {
                        swap(node, node.parent);
                        node = node.parent;
                    }
                } else {
                    // Min-Heap: Ensure parent is less than the child
                    while (node.parent != null && node.value < node.parent.value) {
                        swap(node, node.parent);
                        node = node.parent;
                    }
                }
            }

            // Helper method to swap values of two nodes
            private void swap(Node a, Node b) {
                int temp = a.value;
                a.value = b.value;
                b.value = temp;
            }

            public int removeHead() {
                if (this.head == null) {
                    throw new IllegalStateException("Heap is empty");
                }

                int headValue = this.head.getValue();
                if (this.size == 1) {
                    this.head = null;
                } else {
                    Node lastNode = getLastNode();
                    swap(this.head, lastNode);
                    removeLastNode();
                    rebalanceDown(this.head);
                }
                this.size--;
                return headValue;
            }

            private void removeLastNode() {
                Node lastNode = getLastNode();
                if (lastNode == null) return;

                if (lastNode.parent != null) {
                    if (lastNode.parent.leftChild == lastNode) {
                        lastNode.parent.leftChild = null;
                    } else {
                        lastNode.parent.rightChild = null;
                    }
                }
            }

            private Node getLastNode() {
                if (head == null) return null;

                Queue<Node> queue = new LinkedList<>();
                queue.add(head);
                Node lastNode = null;

                while (!queue.isEmpty()) {
                    lastNode = queue.poll();
                    if (lastNode.leftChild != null) queue.add(lastNode.leftChild);
                    if (lastNode.rightChild != null) queue.add(lastNode.rightChild);
                }

                return lastNode;
            }

            // Helper method to rebalance the heap downwards
            private void rebalanceDown(Node node) {
                while (node.leftChild != null) {
                    Node child = node.leftChild;
                    if (node.rightChild != null && ((isMaxHeap && node.rightChild.value > node.leftChild.value) || (!isMaxHeap && node.rightChild.value < node.leftChild.value))) {
                        child = node.rightChild;
                    }
                    if ((isMaxHeap && node.value >= child.value) || (!isMaxHeap && node.value <= child.value)) {
                        break;
                    }
                    swap(node, child);
                    node = child;
                }
            }

        }

        public MedianBinaryHeap() {
            this.maxheap = new BinaryHeap(true);
            this.minHeap = new BinaryHeap(false);
        }

        public void insert(int value) {
            if (this.minHeap.getSize() == 0 && this.maxheap.getSize() == 0) {
                this.minHeap.insert(value);
            } else if (this.minHeap.getSize() >= 1 && this.maxheap.getSize() == 0) {
                if (this.minHeap.peek() < value) {
                    int minHeapHeadValue = this.minHeap.removeHead();
                    this.maxheap.insert(minHeapHeadValue);
                    this.minHeap.insert(value);
                } else {
                    this.maxheap.insert(value);
                }
            } else if (this.minHeap.getSize() == 0 && this.maxheap.getSize() >= 1) {
                if (this.maxheap.peek() > value) {
                    int maxHeapHeadValue = this.maxheap.removeHead();
                    this.minHeap.insert(maxHeapHeadValue);
                    this.maxheap.insert(value);
                } else {
                    this.minHeap.insert(value);
                }
            } else {
                if (value < this.minHeap.peek()) {
                    this.maxheap.insert(value);
                } else {
                    this.minHeap.insert(value);
                }

                // Balance the heaps if necessary
                if (this.minHeap.getSize() > this.maxheap.getSize() + 1) {
                    int minHeapHeadValue = this.minHeap.removeHead();
                    this.maxheap.insert(minHeapHeadValue);
                } else if (this.maxheap.getSize() > this.minHeap.getSize()) {
                    int maxHeapHeadValue = this.maxheap.removeHead();
                    this.minHeap.insert(maxHeapHeadValue);
                }
            }
        }

        public int findMedian() {
            if (this.minHeap.getSize() == 0 && this.maxheap.getSize() == 0) {
                throw new IllegalStateException("Heaps are empty");
            }
            if (this.minHeap.getSize() > this.maxheap.getSize()) {
                return this.minHeap.peek();
            } else {
                return this.maxheap.peek();
            }
        }

        public int removeMedian() {
            if (this.minHeap.getSize() == 0 && this.maxheap.getSize() == 0) {
                throw new IllegalStateException("Heaps are empty");
            }
            if (this.minHeap.getSize() > this.maxheap.getSize()) {
                return this.minHeap.removeHead();
            } else {
                return this.maxheap.removeHead();
            }
        }
    }

    public static void main(String[] args) {
        DynamicMedian.MedianBinaryHeap medianHeap = new DynamicMedian.MedianBinaryHeap();

        int[] numbers = {5, 15, 1, 3, 8, 7, 9, 2, 6, 10};

        for (int number : numbers) {
            medianHeap.insert(number);
            System.out.println("Inserted: " + number + ", Current Median: " + medianHeap.findMedian());
        }

        System.out.println("Removing medians:");
        while (medianHeap.minHeap.getSize() > 0 || medianHeap.maxheap.getSize() > 0) {
            System.out.println("Removed Median: " + medianHeap.removeMedian());
        }
    }


}
