import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FastCollinearPoints {
    private List<LineSegment> segments = new ArrayList<>();
    private Point[] aux;
    private int size;

    public FastCollinearPoints(Point[] points) {
        check(points);
        aux = Arrays.copyOf(points, points.length);
        findSegments(points);
    }     // finds all line segments containing 4 or more points

    private void check(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("argument to the constructor is null");
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("argument to the constructor is null");
            }
        }
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Contain two same points");
                }
            }
        }
    }

    private void findSegments(Point[] points) {
        int i = 0, j = 0, k = 0;
        int n = points.length;
        for (i = 0; i < n; ++i) {
            Point standardPoint = points[i], startPoint = null, endPoint = null;
            Comparator<Point> comparator = standardPoint.slopeOrder();
            Arrays.sort(aux);
            Arrays.sort(aux, comparator);
            int count = 1;
            double slope = 0;
            for (j = 0; j < n - 2; ++j) {
                startPoint = aux[j];
                slope = standardPoint.slopeTo(aux[j]);
                count = 1;
                while (j < n - 1 && slope == standardPoint.slopeTo(aux[j + 1])) {
                    count += 1;
                    j += 1;
                }
                endPoint = aux[j];
                if (count >= 3) {
                    if (standardPoint.compareTo(startPoint) < 0) {
                        startPoint = standardPoint;
                    }
                    if (standardPoint == startPoint) {
                        segments.add(new LineSegment(startPoint, endPoint));
                    }
                }
            }
        }
        size = segments.size();
        System.out.println("size: " + size);
    }

    public int numberOfSegments() {
        return size;
    }        // the number of line segments

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[size]);
    }                // the line segments

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
