

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Item[] items;  // array to store items
    private int activeItems;
    private int firstActiveItemIndex;
    private int lastActiveItemIndex;

    // construct an empty deque
    public Deque() {
        items = (Item[]) new Object[2];
        activeItems = 0;
        firstActiveItemIndex = 0;
        lastActiveItemIndex = 0;
    }

    private void shrink() {
        if (activeItems > 0 && activeItems == items.length / 4) {
            resize(items.length / 2);
        }
    }

    private void resize(int capacity) {
        Item[] tmp = (Item[]) new Object[capacity];
        int start = (capacity - activeItems) / 2;
        System.arraycopy(items, firstActiveItemIndex, tmp, start, activeItems);
        firstActiveItemIndex = start;
        lastActiveItemIndex = firstActiveItemIndex + activeItems - 1;
        items = tmp;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return activeItems == 0;
    }

    // return the number of items on the deque
    public int size() {
        return activeItems;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null item.");
        }
        if (activeItems == items.length) {
            resize(items.length * 2);
        }
        if (firstActiveItemIndex == 0) {
            resize(items.length * 2);
        }
        items[--firstActiveItemIndex] = item;
        activeItems++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null item.");
        }
        if (activeItems == items.length) {
            resize(items.length * 2);
        }
        if (lastActiveItemIndex == items.length - 1) {
            resize(items.length * 2);
        }
        items[++lastActiveItemIndex] = item;
        activeItems++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty.");
        }
        Item item = items[firstActiveItemIndex];
        items[firstActiveItemIndex++] = null;  // avoid loitering
        activeItems--;
        shrink();
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty.");
        }
        Item item = items[lastActiveItemIndex];
        items[lastActiveItemIndex--] = null;  // avoid loitering
        activeItems--;
        shrink();
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private int current = firstActiveItemIndex;

        @Override
        public boolean hasNext() {
            return current <= lastActiveItemIndex;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return items[current++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();

        // Test isEmpty and size methods
        StdOut.println("Is deque empty? " + deque.isEmpty()); // Expected: true
        StdOut.println("Size of deque: " + deque.size()); // Expected: 0

        // Test addFirst and addLast methods
        deque.addFirst(1);
        deque.addLast(2);
        deque.addFirst(0);
        deque.addLast(3);

        // Test isEmpty and size methods again
        StdOut.println("Is deque empty? " + deque.isEmpty()); // Expected: false
        StdOut.println("Size of deque: " + deque.size()); // Expected: 4

        // Test removeFirst and removeLast methods
        StdOut.println("Remove first: " + deque.removeFirst()); // Expected: 0
        StdOut.println("Remove last: " + deque.removeLast()); // Expected: 3
        StdOut.println("Remove first: " + deque.removeFirst()); // Expected: 1
        StdOut.println("Remove last: " + deque.removeLast()); // Expected: 2

        // Test isEmpty and size methods again
        StdOut.println("Is deque empty? " + deque.isEmpty()); // Expected: true
        StdOut.println("Size of deque: " + deque.size()); // Expected: 0

        // Test iterator
        deque.addFirst(1);
        deque.addLast(2);
        deque.addFirst(0);
        deque.addLast(3);

        StdOut.print("Deque elements: ");
        for (int item : deque) {
            StdOut.print(item + " "); // Expected: 0 1 2 3
        }
    }
}