import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private int size;
    private Node first;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    // construct an empty deque
    public Deque() {
        size = 0;
        first = new Node();
        first.next = first;
        first.prev = first;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        Node nextNode = first.next;
        Node newNode = new Node();
        newNode.next = nextNode;
        nextNode.prev = newNode;
        first.next = newNode;
        newNode.prev = first;
        newNode.item = item;
        size += 1;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        Node prevNode = first.prev;
        Node newNode = new Node();
        newNode.prev = prevNode;
        prevNode.next = newNode;
        newNode.next = first;
        first.prev = newNode;
        newNode.item = item;
        size += 1;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Deque is empty");
        }
        Node newFirst = first.next.next;
        Node removed = first.next;
        first.next = newFirst;
        newFirst.prev = first;
        size -= 1;
        return removed.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Deque is empty");
        }
        Node newLast = first.prev.prev;
        Node removed = first.prev;
        first.prev = newLast;
        newLast.next = first;
        size -= 1;
        return removed.item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current;

        public DequeIterator() {
            current = first.next;
        }

        public boolean hasNext() {
            return current != first;
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException("No more items to return");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
    }

}