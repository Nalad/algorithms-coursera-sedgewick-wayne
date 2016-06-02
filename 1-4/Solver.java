import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;


public class Solver {
    private SearchNode goal = null;
    private boolean solvable = false;

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves;
        private SearchNode previousNode;

        public SearchNode(Board board, int moves, SearchNode previousNode) {
            this.board = board;
            this.moves = moves;
            this.previousNode = previousNode;
        }

        public int compareTo(SearchNode that) {
            return (this.board.manhattan() + this.moves)
                   - (that.board.manhattan() + that.moves);
        }
    }

    public Solver(Board initial) {             // find a solution to the initial board (using the A* algorithm)
        if (initial == null) {
            throw new java.lang.NullPointerException();
        }

        MinPQ<SearchNode> aSearch = new MinPQ<SearchNode>();
        MinPQ<SearchNode> aSearchTwin = new MinPQ<SearchNode>();
        aSearch.insert(new SearchNode(initial, 0, null));
        aSearchTwin.insert(new SearchNode(initial.twin(), 0, null));

        for (;;) {
            SearchNode node = aSearch.delMin();
            SearchNode nodeTwin = aSearchTwin.delMin();
            if (node.board.hamming() == 0) {
                goal = node;
                solvable = true;
                break;
            }
            if (nodeTwin.board.hamming() == 0) {
                solvable = false;
                break;
            }
                
            Iterable<Board> boardsStack = node.board.neighbors();
            Iterable<Board> boardsStackTwin = nodeTwin.board.neighbors();
            for (Board neighborBoard : boardsStack) {
                if (node.previousNode == null) {
                    aSearch.insert(new SearchNode(neighborBoard, node.moves + 1, node));
                } else {
                    if (neighborBoard.equals(node.previousNode.board)) {
                        continue;
                    } else {
                        aSearch.insert(new SearchNode(neighborBoard, node.moves + 1, node));
                    }
                }
            }
            for (Board neighborBoard : boardsStackTwin) {
                if (nodeTwin.previousNode == null) {
                    aSearchTwin.insert(new SearchNode(neighborBoard, nodeTwin.moves + 1, nodeTwin));
                } else {
                    if (neighborBoard.equals(nodeTwin.previousNode.board)) {
                        continue;
                    } else {
                        aSearchTwin.insert(new SearchNode(neighborBoard, nodeTwin.moves + 1, nodeTwin));
                    }
                }
            }
        }
    }



    public boolean isSolvable() {               // is the initial board solvable?
        return solvable;
    }

    public int moves() {                       // min number of moves to solve intial board; -1 if unsolvable
        if (!isSolvable()) {
            return -1;
        }

        return goal.moves;
    }

    public Iterable<Board> solution() {         // sequence of boards in a shortest solution; null if unsolvable
        if (goal == null) {
            return null;
        }

        Stack<Board> stack = new Stack<Board>();

        SearchNode temp = goal;
        do {
            stack.push(temp.board);
            temp = temp.previousNode;
        } while (temp != null);

        return stack;
    }

    public static void main(String[] args) { // solve a slider puzzle (given below)
        In in = new In(args[0]);
        int N = in.readInt();
        char[][] blocks = new char[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = (char) in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

