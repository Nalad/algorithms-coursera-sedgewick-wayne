import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;   // array of items
    private int N;      // number of items

    public RandomizedQueue() 
    {
        a = (Item[]) new Object[2];
    }

    public boolean isEmpty() 
    {
        return N == 0;
    }
    
    public int size() 
    {
        return N;
    }
    
    public void enqueue(Item item) 
    {
        if (item == null) {
            throw new java.lang.NullPointerException();
        }

        if (N == a.length) resize(2 * a.length);
        a[N++] = item;
    }

    public Item dequeue() 
    {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }

        int index = StdRandom.uniform(N);
        Item item = a[index];
        
        if (index == 0) {
            a[index] = a[N - 1];
        } else if (0 < index && index < (N - 1)) {
            a[index] = a[N - 1];
        } 

        a[N - 1] = null;
        N--;

        if (N > 0 && N == a.length/4) resize(a.length/2);
        return item;
    }

    public Item sample() 
    {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }

        return a[StdRandom.uniform(N)];
    }

    public Iterator<Item> iterator() 
    {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int[] order;
        private int index;

        public RandomizedQueueIterator() {
            order = new int[N];
            index = 0;

            for (int i = 0; i < N; i++) {
                order[i] = i;
            }

            StdRandom.shuffle(order);
        }

        public boolean hasNext() {
            return index < order.length;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            return a[order[index++]];
        }
    }


    
    private void resize(int capacity)
    {
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    public static void main(String[] args) 
    {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();

        StdOut.println(rq.isEmpty());
        rq.enqueue(10);
        rq.enqueue(10);
        rq.enqueue(20);
        rq.enqueue(30);
        /*
        StdOut.println(rq.Length());
        rq.enqueue(10);
        rq.enqueue(10);
        rq.enqueue(20);
        rq.enqueue(30);
        StdOut.println(rq.Length());
        rq.enqueue(10);
        rq.enqueue(10);
        rq.enqueue(10);
        rq.enqueue(20);
        rq.enqueue(30);
        rq.enqueue(10);
        rq.enqueue(20);
        rq.enqueue(30);
        StdOut.println(rq.Length());
        rq.dequeue();
        rq.dequeue();
        rq.dequeue();
        rq.dequeue();
        rq.dequeue();
        rq.dequeue();
        rq.dequeue();
        rq.dequeue();
        StdOut.println(rq.Length());
        rq.dequeue();
        rq.dequeue();
        rq.dequeue();
        rq.dequeue();
        StdOut.println(rq.Length());
        StdOut.println(rq.size());
        */

        for (int inte : rq) {
            StdOut.println(inte);
        }
        for (int inte : rq) {
            StdOut.println(inte);
        }

    }
}
