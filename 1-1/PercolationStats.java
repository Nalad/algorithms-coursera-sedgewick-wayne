import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int T;
    private double[] sums;

    public PercolationStats(int N, int T) 
    {
        if ((N <= 0) || (T <= 0)) {
            throw new java.lang.IllegalArgumentException();
        }

        this.T = T;
        sums = new double[T];

        for (int i = 0; i < T; i++) {
            Percolation perc = new Percolation(N);
            int opened = 0;
            sums[i] = 0.0;

            while (!perc.percolates()) {
                int random1 = StdRandom.uniform(1, N + 1);
                int random2 = StdRandom.uniform(1, N + 1);

                if (!perc.isOpen(random1, random2)) {
                    perc.open(random1, random2);
                    opened++;
                }
            }

            sums[i] = opened / (double) (N * N);  
        }
    }

    public double mean()
    {
        return StdStats.mean(sums);
    }

    public double stddev() 
    {
        if (T == 1) {
            return Double.NaN;
        }

        return StdStats.stddev(sums);
    }

    public double confidenceLo()
    {
        return mean() - ((1.96 * stddev()) / Math.sqrt(T));
    }

    public double confidenceHi() 
    {
        return mean() + ((1.96 * stddev()) / Math.sqrt(T));
    }

    public static void main(String[] args)
    {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(N, T);

        StdOut.println(ps.mean());
        StdOut.println(ps.stddev());
        StdOut.println(ps.confidenceLo());
        StdOut.println(ps.confidenceHi());
    }
}
