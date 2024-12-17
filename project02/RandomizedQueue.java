import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Array<Item> items;

    private class Array<T> {
        int capacity;
        int size;
        T[] items;

        public Array() {
            capacity = 1;
            size = 0;
            items = (T[]) new Object[capacity];
        }

        private void resize(int newCapacity) {
            T[] newItems = (T[]) new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newItems[i] = items[i];
            }
            items = newItems;
            capacity = newCapacity;
        }

        private void swap(int a) {
            items[a] = items[size - 1];
        }

        public void add(T item) {
            if (item == null) {
                throw new IllegalArgumentException("Item cannot be null");
            }
            if (size == capacity) {
                resize(2 * capacity);
            }
            items[size] = item;
            size += 1;
        }

        public void remove(int index) {
            if (index < 0 || index >= size) {
                throw new IllegalArgumentException("Index out of bounds");
            }
            if (size == capacity / 4) {
                resize(capacity / 2);
            }
            swap(index);
            size -= 1;
        }

        public T get(int index) {
            if (index < 0 || index >= size) {
                throw new IllegalArgumentException("Index out of bounds");
            }
            return items[index];
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int getSize() {
            return size;
        }
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = new Array<Item>();
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return items.isEmpty();
    }

    // return the number of items on the randomized queue
    public int size() {
        return items.getSize();
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        items.add(item);
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("RandomizedQueue is empty");
        }
        int randomIndex = StdRandom.uniform(items.getSize());
        Item item = items.get(randomIndex);
        items.remove(randomIndex);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("RandomizedQueue is empty");
        }
        int randomIndex = StdRandom.uniform(items.getSize());
        return items.get(randomIndex);
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int current;
        private final Item[] shuffledItems;

        public RandomizedQueueIterator() {
            current = 0;
            shuffledItems = (Item[]) new Object[items.getSize()];
            for (int i = 0; i < items.getSize(); i++) {
                shuffledItems[i] = items.get(i);
            }
            StdRandom.shuffle(shuffledItems);
        }

        public boolean hasNext() {
            return current < items.getSize();
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException("No more items to return");
            }
            Item item = shuffledItems[current];
            current += 1;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove operation is not supported");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
    }

}
