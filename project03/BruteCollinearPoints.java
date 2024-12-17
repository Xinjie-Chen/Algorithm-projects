import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class BruteCollinearPoints {
    private List<LineSegment> segments = new ArrayList<LineSegment>();
    private int size;

    public BruteCollinearPoints(Point[] points) {
        check(points);
        size = points.length;
        findSegments(points);
    }    // finds all line segments containing 4 points

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
                    throw new IllegalArgumentException("argument to the constructor is null");
                }
            }
        }
    }

    private void findSegments(Point[] points) {
        int n = points.length;
        int i = 0, j = 0, k = 0, m = 0;
        for (i = 0; i < n - 3; ++i) {
            for (j = i + 1; j < n - 2; ++j) {
                for (k = j + 1; k < n - 1; ++k) {
                    for (m = k + 1; m < n; ++m) {
                        double slope1 = points[i].slopeTo(points[j]);
                        double slope2 = points[i].slopeTo(points[k]);
                        double slope3 = points[i].slopeTo(points[m]);
                        Point minPoint = points[i];
                        Point maxPoint = points[i];
                        if (slope1 == slope2 && slope2 == slope3) {
                            if (points[j].compareTo(minPoint) < 0) {
                                minPoint = points[j];
                            }
                            if (points[j].compareTo(maxPoint) > 0) {
                                maxPoint = points[j];
                            }
                            if (points[k].compareTo(minPoint) < 0) {
                                minPoint = points[k];
                            }
                            if (points[k].compareTo(maxPoint) > 0) {
                                maxPoint = points[k];
                            }
                            if (points[m].compareTo(minPoint) < 0) {
                                minPoint = points[m];
                            }
                            if (points[m].compareTo(maxPoint) > 0) {
                                maxPoint = points[m];
                            }
                            LineSegment segment = new LineSegment(minPoint, maxPoint);
                            segments.add(segment);
                            break;
                        }
                    }
                }
            }
        }
        size = segments.size();
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
