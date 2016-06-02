import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null) {
            throw new java.lang.NullPointerException();
        }

        this.wordnet = wordnet;
    }
    
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (nouns == null) {
            throw new java.lang.NullPointerException();
        }

        int[] sumOfDistances = new int[nouns.length];

        for (int i = 0; i < nouns.length; i++) {
            for (String noun : nouns) {
                sumOfDistances[i] += wordnet.distance(nouns[i], noun);
            }
        }

        int maxId = 0;
        for (int i = 1; i < sumOfDistances.length; i++) {
            if (sumOfDistances[i] > sumOfDistances[maxId]) {
                maxId = i;
            }
        }

        return nouns[maxId];
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
