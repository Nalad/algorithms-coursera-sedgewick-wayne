import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private int N;          // number of items
    private Node first;     // top of deque
    private Node last;      // bottom of deque

    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }
    
    public Deque() {
        first = null;
        last = null;
        N = 0;
    }

    public boolean isEmpty() {
        return (first == null) || (last == null);
    }

    public int size() {
        return N;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException();
        }

        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        first.previous = null;
        /*
        if (oldfirst != null) {
            oldfirst.previous = first;
        }
        */
        // After adding top node we should have at least top fullfiled so if
        // isEmpty still showing true then 'last' must be pointing to null
        if (isEmpty()) {
            last = first;
        } else {
            oldfirst.previous = first;
        }

        N++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException();
        }
        
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.previous = oldlast;
        if (isEmpty()) {
            first = last;
        } else {
            oldlast.next = last;
        }

        N++;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }

        Item item = first.item;

        first = first.next;
        if (first != null) {
            first.previous = null;
        } else {
            last = first;
        }

        N--;

        return item;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }

        Item item = last.item;

        last = last.previous;
        if (last != null) {
            last.next = null;
        } else {
            first = last;
        }

        N--;

        return item;
    }

    public Iterator<Item> iterator() { return new DequeIterator(); }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null; 
        }

        public void remove() { 
            throw new java.lang.UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }

            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Deque<String> test = new Deque<String>();
        StdOut.println(test.isEmpty());
        test.addFirst("Alan");
        test.addLast("Drozd");
        test.addFirst("Jan");
        test.addLast("Mama");
        test.addLast("Sara");
        test.addLast("Babcia");
        StdOut.println(test.size());
        for (String s : test) {
            StdOut.println(s);
        }
        test.removeLast();
        test.removeLast();
        test.removeLast();
        test.removeLast();
        test.removeLast();
        test.removeLast();
        StdOut.println(test.isEmpty());

        StdOut.println();

        
        for (String s : test) {
            StdOut.println(s);
        }
        
    }
}
