import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

import java.util.HashMap;
import java.util.Map;

public class WordNet {
    private Map<Integer, String> synIdToNoun;
    private Map<String, Bag<Integer>> nounToSynId;

    private Digraph G;
    private SAP sapDataType;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if ((synsets == null) || (hypernyms == null)) {
            throw new java.lang.NullPointerException();
        }

        synIdToNoun = new HashMap<>();
        nounToSynId = new HashMap<>();
        createMaps(synsets);
        
        G = new Digraph(synIdToNoun.size());
        createGraph(hypernyms);

        if (!(new Topological(G).hasOrder())) {
            throw new java.lang.IllegalArgumentException();
        }
        int roots = 0;
        for (int i = 0; i < G.V(); i++) {
            if (G.outdegree(i) == 0) {
                roots++;
            }
        }
        if (roots != 1) {
            System.out.println(roots);
            throw new java.lang.IllegalArgumentException();
        }

        sapDataType = new SAP(G);
    }

    private void createMaps(String synsets) {
        In in = new In(synsets);

        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            synIdToNoun.put(id, parts[1]);

            String[] nouns = parts[1].split(" ");
            for (String noun : nouns) {
                if (!nounToSynId.containsKey(noun)) {
                    nounToSynId.put(noun, new Bag<Integer>());
                }

                nounToSynId.get(noun).add(id);
            }
        }
    }

    private void createGraph(String hypernyms) {
        In in = new In(hypernyms);

        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] parts = line.split(",");
            int hypernymId = Integer.parseInt(parts[0]);

            for (int i = 1; i < parts.length; i++) {
                // add arg1->arg2 edge
                G.addEdge(hypernymId, Integer.parseInt(parts[i]));
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToSynId.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new java.lang.NullPointerException();
        }

        return nounToSynId.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if ((nounA == null) || (nounB == null)) {
            throw new java.lang.NullPointerException();
        }
        if (!nounToSynId.containsKey(nounA) || !nounToSynId.containsKey(nounB)) {
            throw new java.lang.IllegalArgumentException();
        }

        return sapDataType.length(nounToSynId.get(nounA), nounToSynId.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of 
    // nounA and nounB
    public String sap(String nounA, String nounB) {
        if ((nounA == null) || (nounB == null)) {
            throw new java.lang.NullPointerException();
        }
        if (!nounToSynId.containsKey(nounA) || !nounToSynId.containsKey(nounB)) {
            throw new java.lang.IllegalArgumentException();
        }

        int commonAncestorId = sapDataType.ancestor(nounToSynId.get(nounA), nounToSynId.get(nounB));
        return synIdToNoun.get(commonAncestorId);
    }

    // do unit testing of this class
    public static void main(String[] args) {
    }
}
