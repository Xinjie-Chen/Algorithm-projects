import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Solver {
    private SearchNode initial;
    private SearchNode twin;
    private boolean solvable;
    private SearchNode ans;
    private int finalMoves;

    private class SearchNode {
        private final Board board;
        private final int moves;
        private final int manhattanPriority;
        private SearchNode previous;

        public SearchNode(Board board, int moves, SearchNode prevNode) {
            this.board = board;
            this.moves = moves;
            this.previous = prevNode;
            this.manhattanPriority = board.manhattan() + moves;
        }

        public int getManhattanPriority() {
            return this.manhattanPriority;
        }

        public int getMoves() {
            return moves;
        }

        public boolean isGoal() {
            return board.isGoal();
        }

        public Board getBoard() {
            return board;
        }

        public Iterable<Board> neighbors() {
            return board.neighbors();
        }

        public SearchNode getPrevious() {
            return previous;
        }
    }

    private class SearchComparator implements Comparator<SearchNode> {
        public int compare(SearchNode node1, SearchNode node2) {
            return node1.getManhattanPriority() - node2.getManhattanPriority();
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Initial board is null");
        }
        Board twinBoard = initial.twin();
        this.twin = new SearchNode(twinBoard, 0, null);
        this.initial = new SearchNode(initial, 0, null);
        this.finalMoves = -1;
        this.solvable = false;
        rotate();
    }

    private void rotate() {
        MinPQ<SearchNode> PQ = new MinPQ<SearchNode>(new SearchComparator());
        MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>(new SearchComparator());
        PQ.insert(initial);
        twinPQ.insert(twin);
        List<Board> table = new ArrayList<Board>();
        List<Board> twinTable = new ArrayList<Board>();
        while (true) {
            if (solve(PQ, table)) {
                solvable = true;
                break;
            }
            if (solve(twinPQ, twinTable)) {
                finalMoves = -1;
                ans = null;
                break;
            }
        }
    }

    private boolean solve(MinPQ<SearchNode> pq, List<Board> table) {
        SearchNode minNode = pq.delMin();
        if (minNode.isGoal()) {
            finalMoves = minNode.getMoves();
            ans = minNode;
            return true;
        }
        for (Board neighbor : minNode.neighbors()) {
            if (equalToPrevious(neighbor, minNode)) {
                continue;
            }
            table.add(neighbor);
            pq.insert(new SearchNode(neighbor, minNode.moves + 1, minNode));
        }
        return false;
    }

    private boolean equalToPrevious(Board board, SearchNode minNode) {
        SearchNode prevNode = minNode.getPrevious();
        if (prevNode == null) {
            return false;
        }
        Board prevBoard = prevNode.getBoard();
        return board.equals(prevBoard);
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return finalMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        Stack<Board> solution = new Stack<Board>();
        SearchNode curNode = ans;
        while (curNode != null) {
            solution.push(curNode.getBoard());
            curNode = curNode.getPrevious();
        }
        List<Board> boards = new ArrayList<Board>();
        while (!solution.isEmpty()) {
            boards.add(solution.pop());
        }
        return boards;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

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
