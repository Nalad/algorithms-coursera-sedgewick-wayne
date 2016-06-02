import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Subset {
    public static void main(String[] args)
    {
        RandomizedQueue<String> ss = new RandomizedQueue<String>();
        int k = Integer.parseInt(args[0]);

        for (int i = 0; i < k; i++) {
            ss.enqueue(StdIn.readString());
        }

        for (int i = k; !StdIn.isEmpty(); i++) {
            String item = StdIn.readString();
            int j = StdRandom.uniform(i + 1);
            if (j < k) {
                ss.dequeue();
                ss.enqueue(item);
            }
        }

        for (int i = 0; !ss.isEmpty(); i++) {
            StdOut.println(ss.dequeue());
        }
    }
}
