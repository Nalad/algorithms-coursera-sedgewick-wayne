import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;

public class Board {
    private final int N;
    private final char[][] blocks;
    private int zeroRow;
    private int zeroCol;

    public Board(char[][] blocks) {                  // construct a board from an N-by-N array of blocks
                                                    // where blocks[i][j] = block in row i, column j)
        this.N = blocks.length;
        this.blocks = new char[N][N];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                this.blocks[i][j] = blocks[i][j];

                if (blocks[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                }
            }
        }
    }

    public int dimension() {                        // board dimension N
        return N;
    }

    public int hamming() {                          // number of blocks out of place
        int wrongPosition = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (isInWrongPosition(i, j)) {
                    wrongPosition++;
                }
            }
        }

        return wrongPosition;
    }

    private boolean isInWrongPosition(int i, int j) {
        // Second conditional is to check whether there is '0'
        if (blocks[i][j] == i * N + j + 1 
            || blocks[i][j] == 0) {
            return false;
        }

        return true;
    }

    public int manhattan() {                        // sum of Manhattan distances between blocks and goal
        int distances = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (isInWrongPosition(i, j)) {
                    // It's like calculating 2D vector length
                    int goalRow = (blocks[i][j] - 1) / N;
                    int goalColumn = blocks[i][j] - goalRow * N - 1;

                    int rowDistance = goalRow - i;
                    if (rowDistance < 0) {
                        rowDistance *= -1;
                    }

                    int columnDistance = goalColumn - j;
                    if (columnDistance < 0) {
                        columnDistance *= -1;
                    }

                    distances += rowDistance + columnDistance;
                }
            }
        }

        return distances;
    }

    public boolean isGoal() {                       // is this board the goal board?
        return hamming() == 0;
    }
    
    public Board twin() {                           // a board that is obtained by exchanging two adjacent blocks in the same row
        char[][] twinBoard = new char[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                twinBoard[i][j] = blocks[i][j];
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (j > 0
                    && twinBoard[i][j - 1] != 0
                    && twinBoard[i][j] != 0) {
                    char swap = twinBoard[i][j - 1];
                    twinBoard[i][j - 1] = twinBoard[i][j];
                    twinBoard[i][j] = swap;
                    
                    return new Board(twinBoard);
                }
            }
        }

        return new Board(new char[][] {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});
    }

    public boolean equals(Object y) {                // does this board equal y?
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.N != that.N) return false;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.blocks[i][j] != that.blocks[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    
    public Iterable<Board> neighbors() {            // all neighboring boards
        char[][] tempBoard = new char[N][N];
        for (int i = 0; i < tempBoard.length; i++) {
            for (int j = 0; j < tempBoard.length; j++) {
                tempBoard[i][j] = blocks[i][j];
                }
            }
        

        Stack<Board> boards = new Stack<Board>();

        if (zeroRow != 0) {
            swap(tempBoard, zeroRow - 1, zeroCol, zeroRow, zeroCol);

            boards.push(new Board(tempBoard));

            swap(tempBoard, zeroRow - 1, zeroCol, zeroRow, zeroCol);
        }
        if (zeroRow != tempBoard.length - 1) {
            swap(tempBoard, zeroRow + 1, zeroCol, zeroRow, zeroCol);

            boards.push(new Board(tempBoard));

            swap(tempBoard, zeroRow + 1, zeroCol, zeroRow, zeroCol);
        }
        if (zeroCol != 0) {
            swap(tempBoard, zeroRow, zeroCol - 1, zeroRow, zeroCol);

            boards.push(new Board(tempBoard));

            swap(tempBoard, zeroRow, zeroCol - 1, zeroRow, zeroCol);
        }
        if (zeroCol != tempBoard.length - 1) {
            swap(tempBoard, zeroRow, zeroCol + 1, zeroRow, zeroCol);

            boards.push(new Board(tempBoard));

            swap(tempBoard, zeroRow, zeroCol + 1, zeroRow, zeroCol);
        }

        return boards;
    }

    private void swap(char[][] board, int i1, int j1, int i2, int j2) {
        char temp = board[i1][j1];
        board[i1][j1] = board[i2][j2];
        board[i2][j2] = temp;
    }


    public String toString() {                     // string representation of this board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }

        return s.toString();
    }
   
    public static void main(String[] args) {        // unit tests (not graded)
        char[][] test = new char[][]{
            {1, 2, 3},
            {4, 0, 5},
            {6, 7, 8}
        };
        Board board = new Board(test);

        StdOut.println(board.dimension());


        test = new char[][] {
            {4, 10, 1, 3},
            {15, 6, 9, 8},
            {5, 12, 0, 7},
            {13, 14, 2, 11}
        };
        board = new Board(test);

        StdOut.println(board.hamming());

        test = new char[][] {
            {5, 8, 7},
            {1, 4, 6},
            {3, 0, 2}
        };
        board = new Board(test);
        StdOut.println("tutaj " + board.manhattan());

        test = new char[][] {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
        };
        board = new Board(test);
        StdOut.println(board.isGoal());
        test = new char[][] {
            {1, 0, 3},
            {4, 5, 6},
            {7, 8, 2}
        };
        board = new Board(test);
        StdOut.println(board.isGoal());

        StdOut.print(board.toString());
        StdOut.print(board.twin().toString());

        test = new char[][] {
            {3, 5, 2},
            {4, 1, 6},
            {0, 8, 7}
        };
        Board secondBoard = new Board(test);
        StdOut.println(board.equals(secondBoard));


        StdOut.println();
        Iterable<Board> b = secondBoard.neighbors();
        for (Board bn : b) {
            StdOut.println(bn.toString());
        }
    }
}
