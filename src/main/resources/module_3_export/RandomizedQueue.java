

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;  // array to store items
    private int size;      // number of items in the queue

    // Constructor to create an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[2];  // initial capacity of 2
        size = 0;
    }

    // Check if the queue is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // Add the item to the randomized queue
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null item.");
        }
        if (size == items.length) {
            resize(2 * items.length);  // double the array size if full
        }
        items[size++] = item;
    }

    // Remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }
        int randomIndex = getRandomIndex();
        Item item = items[randomIndex];

        // Swap with the last element and remove it
        items[randomIndex] = items[--size];
        items[size] = null;  // avoid loitering

        if (size > 0 && size == items.length / 4) {
            resize(items.length / 2);  // shrink the array when one-quarter full
        }

        return item;
    }

    // Return a random item without removing it
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }
        return items[getRandomIndex()];
    }

    // Helper method to generate a random index
    private int getRandomIndex() {
        return StdRandom.uniformInt(size);
    }

    // Resize the internal array
    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            temp[i] = items[i];
        }
        items = temp;
    }

    // Return an independent iterator over items in random order
    @Override
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // Inner class to implement the iterator
    private class RandomizedQueueIterator implements Iterator<Item> {
        private final Item[] shuffledItems;
        private int current;

        // Constructor for the iterator
        public RandomizedQueueIterator() {
            shuffledItems = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                shuffledItems[i] = items[i];
            }
            shuffleArray(shuffledItems);  // shuffle the array for random order
            current = 0;
        }

        // Check if there are more items to return
        @Override
        public boolean hasNext() {
            return current < size;
        }

        // Return the next item in random order
        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return shuffledItems[current++];
        }

        // Remove operation is unsupported
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        // Shuffle the array (Fisher-Yates shuffle)
        private void shuffleArray(Item[] array) {
            for (int i = size - 1; i > 0; i--) {
                int index = StdRandom.uniformInt(i + 1);
                Item temp = array[index];
                array[index] = array[i];
                array[i] = temp;
            }
        }
    }

    // Unit testing
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();

        // Test enqueue operation
        rq.enqueue(1);
        rq.enqueue(2);
        rq.enqueue(3);
        rq.enqueue(4);
        rq.enqueue(5);

        // Test size and isEmpty
        StdOut.println("Size: " + rq.size());  // Expected: 5
        StdOut.println("Is empty: " + rq.isEmpty());  // Expected: false

        // Test sample without removing
        StdOut.println("Sample: " + rq.sample());  // Random output

        // Test dequeue (remove random items)
        StdOut.println("Dequeue: " + rq.dequeue());  // Random output
        StdOut.println("Dequeue: " + rq.dequeue());  // Random output

        // Test iterator
        StdOut.println("Remaining items:");
        for (Integer item : rq) {
            StdOut.println(item);  // Random order
        }

        // Test edge cases
        try {
            rq.enqueue(null);  // Should throw IllegalArgumentException
        } catch (IllegalArgumentException e) {
            StdOut.println(e.getMessage());  // Expected: "Cannot add null item."
        }

        // Test iterator remove operation
        try {
            Iterator<Integer> it = rq.iterator();
            it.remove();  // Should throw UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            StdOut.println(e.getMessage());  // Expected: no message
        }

        // Empty the queue
        while (!rq.isEmpty()) {
            StdOut.println("Dequeue: " + rq.dequeue());  // Random output
        }

        // Test dequeue and sample on empty queue
        try {
            rq.dequeue();  // Should throw NoSuchElementException
        } catch (NoSuchElementException e) {
            StdOut.println(e.getMessage());  // Expected: "Queue is empty."
        }

        try {
            rq.sample();  // Should throw NoSuchElementException
        } catch (NoSuchElementException e) {
            StdOut.println(e.getMessage());  // Expected: "Queue is empty."
        }
    }
}