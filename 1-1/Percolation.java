import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF grid;
    private boolean[] openSites;
    private boolean[] connectedToBottom;
    private int N;
    private int virtualTop;
    
    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        grid = new WeightedQuickUnionUF(N * N + 1);
        this.N = N;
        
        openSites = new boolean[N * N];
        connectedToBottom = new boolean[N * N + 1];

        virtualTop = N * N;
    }    

    private int xyTo1D(int i, int j) {
        return ((i - 1) * N) + (j - 1);
    }

    public void open(int i, int j) {
        validate(i, j);

        if (isOpen(i, j)) {
            return;
        }

        int number = xyTo1D(i, j);

        openSites[number] = true;
        
        if (number < N) {
            boolean isConnected = connectedToBottom[grid.find(virtualTop)];
            isConnected |= connectedToBottom[grid.find(number)];
            grid.union(number, virtualTop);
            connectedToBottom[grid.find(number)] = isConnected;
        } else {
            if (isOpen(i - 1, j)) {
                boolean isConnected = connectedToBottom[grid.find(xyTo1D(i - 1, j))];
                isConnected |= connectedToBottom[grid.find(number)];
                grid.union(number, number - N);
                connectedToBottom[grid.find(number)] = isConnected;
            }
        }

        if (number >= N * (N - 1)) {
            connectedToBottom[grid.find(number)] = true;
        } else {
            if (isOpen(i + 1, j)) {
                boolean isConnected = connectedToBottom[grid.find(xyTo1D(i + 1, j))];
                isConnected |= connectedToBottom[grid.find(number)];
                grid.union(number, number + N);
                connectedToBottom[grid.find(number)] = isConnected;
            }
        }

        if (number % N != 0) {
            if (isOpen(i, j - 1)) {
                boolean isConnected = connectedToBottom[grid.find(xyTo1D(i, j - 1))];
                isConnected |= connectedToBottom[grid.find(number)];
                grid.union(number, number - 1);
                connectedToBottom[grid.find(number)] = isConnected;
            }
        }

        if ((number + 1) % N != 0) {
            if (isOpen(i, j + 1)) {
                boolean isConnected = connectedToBottom[grid.find(xyTo1D(i, j + 1))];
                isConnected |= connectedToBottom[grid.find(number)];
                grid.union(number, number + 1);
                connectedToBottom[grid.find(number)] = isConnected;
            }
        }
    }
        
    public boolean isOpen(int i, int j) {
        validate(i, j);

        return openSites[xyTo1D(i, j)];
    }

    public boolean isFull(int i, int j) {
        validate(i, j);

        return grid.connected(xyTo1D(i, j), N * N);
    }

    private void validate(int i, int j) {
        if (i < 1 || i > N || j < 1 || j > N) {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    public boolean percolates() {
        return connectedToBottom[grid.find(virtualTop)];
    }
    
    public static void main(String[] args) {
        Percolation perc = new Percolation(4);
        perc.open(1, 1);
        perc.open(1, 3);
        perc.open(2, 3);
        perc.open(2, 4);
        perc.open(3, 3);
        perc.open(3, 4);
        perc.open(4, 2);
        perc.open(4, 4);
        StdOut.println(perc.percolates());
    }
}
