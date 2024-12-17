import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private final int n;
    private int row0, col0;
    private int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        row0 = 0;
        col0 = 0;
        this.tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            this.tiles[i] = Arrays.copyOf(tiles[i], n);
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result.append(tiles[i][j] + " ");
            }
            if (i < n - 1) {
                result.append("\n");
            }
        }
        return result.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!isAtPos(i, j)) {
                    count++;
                }
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int count = 0;
        int goalRow, goalCol;
        int row, col;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int goal = i * n + j + 1;
                if (tiles[i][j] != goal && tiles[i][j] != 0) {
                    goalRow = (tiles[i][j] - 1) / n;
                    goalCol = (tiles[i][j] - 1) % n;
                    row = i;
                    col = j;
                    count += Math.abs(goalRow - row) + Math.abs(goalCol - col);
                }
            }
        }
        return count;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (y == this) {
            return true;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (that.dimension() != this.dimension()) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (that.tiles[i][j] != this.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        getZeroPos();
        List<Board> neighbors = new ArrayList<>();
        if (row0 > 0) {
            neighbors.add(new Board(getNeighbor(row0 - 1, col0)));
        }
        if (row0 < n - 1) {
            neighbors.add(new Board(getNeighbor(row0 + 1, col0)));
        }
        if (col0 > 0) {
            neighbors.add(new Board(getNeighbor(row0, col0 - 1)));
        }
        if (col0 < n - 1) {
            neighbors.add(new Board(getNeighbor(row0, col0 + 1)));
        }
        return neighbors;
    }

    private int[][] copyTile() {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            copy[i] = Arrays.copyOf(tiles[i], n);
        }
        return copy;
    }

    private int[][] getNeighbor(int row1, int col1) {
        int[][] copy = copyTile();
        copy[row0][col0] = copy[row1][col1];
        copy[row1][col1] = 0;
        return copy;
    }

    private void getZeroPos() {
        int i = 0, j = 0;
        for (i = 0; i < n; ++i) {
            for (j = 0; j < n; ++j) {
                if (tiles[i][j] == 0) {
                    row0 = i;
                    col0 = j;
                    return;
                }
            }
        }
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] copy = copyTile();
        int row1 = 0, row2 = 1, col1 = 0, col2 = 0;
        int temp;
        while (!check(row1, row2, col1, col2)) {
            col1 = 1;
            col2 = 1;
        }

        temp = copy[row1][col1];
        copy[row1][col1] = copy[row2][col2];
        copy[row2][col2] = temp;

        return new Board(copy);
    }

    private boolean check(int row1, int row2, int col1, int col2) {
        if (row1 == row2 && col1 == col2) {
            return false;
        }
        if (tiles[row1][col1] == 0 || tiles[row2][col2] == 0) {
            return false;
        }
        return true;
    }

    private boolean isAtPos(int i, int j) {
        int goal = i * n + j + 1;
        if (tiles[i][j] != goal && tiles[i][j] != 0) {
            return false;
        }
        return true;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        Board board = new Board(new int[][] { { 1, 0 }, { 2, 3 } });
        System.out.println(board.twin());
    }
}
