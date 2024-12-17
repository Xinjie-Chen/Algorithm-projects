import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;
import java.util.List;

public class PointSET {
    private SET<Point2D> set;

    public PointSET() {
        set = new SET<Point2D>();

    }                               // construct an empty set of points

    public boolean isEmpty() {
        return set.isEmpty();
    }                      // is the set empty?

    public int size() {
        return set.size();
    }                         // number of points in the set

    public void insert(Point2D p) {
        validateNotNull(p, "p");
        set.add(p);
    }              // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        validateNotNull(p, "p");
        return set.contains(p);
    }            // does the set contain point p?

    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }                        // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        validateNotNull(rect, "rect");
        List<Point2D> list = new ArrayList<>();
        for (Point2D p : set) {
            if (rect.contains(p)) {
                list.add(p);
            }
        }
        return list;
    }             // all points that are inside the rectangle (or on the boundary)

    public Point2D nearest(Point2D p) {
        validateNotNull(p, "p");
        double nearestDistance = Double.POSITIVE_INFINITY;
        Point2D nearestPoint = null;
        for (Point2D point : set) {
            double distance = p.distanceSquaredTo(point);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }            // a nearest neighbor in the set to point p; null if the set is empty

    private void validateNotNull(Object obj, String name) {
        if (obj == null) {
            throw new IllegalArgumentException(name + " is null");
        }
    }

    public static void main(String[] args) {
    }                 // unit testing of the methods (optional)
}
