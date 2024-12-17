/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;

public class KdTree {
    private Node root;
    private int size;

    private static class Node {
        private Point2D p;
        private Node left;
        private Node right;
        private boolean isVertical;

        private RectHV rect;

        public Node(Point2D p, boolean isVertical, double x0, double x1, double y0, double y1) {
            this.p = p;
            this.left = null;
            this.right = null;
            this.isVertical = isVertical;
            this.rect = new RectHV(x0, y0, x1, y1);
        }

        public boolean isEqual(Point2D pointer) {
            return this.p.equals(pointer);
        }

        public void setPoint(Point2D pointer) {
            this.p = pointer;
        }

        public Point2D getPoint() {
            return p;
        }

        public boolean isBigger(Point2D pointer) {
            if (isVertical) {
                return this.p.x() > pointer.x();
            }
            else {
                return this.p.y() > pointer.y();
            }
        }

        public boolean getVertical() {
            return isVertical;
        }

        public double distanceRect(Point2D pointer) {
            return this.rect.distanceSquaredTo(pointer);
        }

        public String toString() {
            return p.toString() + " " + rect.toString();
        }
    }

    public KdTree() {
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public void insert(Point2D p) {
        validateNotNull(p, "p");
        root = insertHelper(root, p, true, 0, 1, 0, 1);
    }

    private Node insertHelper(Node node, Point2D p, boolean isVertical, double x0, double x1,
                              double y0, double y1) {
        if (node == null) {
            this.size += 1;
            return new Node(p, isVertical, x0, x1, y0, y1);
        }
        if (node.isEqual(p)) {
            node.setPoint(p);
            return node;
        }
        if (node.isBigger(p)) {
            if (node.getVertical()) {
                x1 = node.getPoint().x();
            }
            else {
                y1 = node.getPoint().y();
            }
            node.left = insertHelper(node.left, p, !isVertical, x0, x1, y0, y1);
        }
        else {
            if (node.getVertical()) {
                x0 = node.getPoint().x();
            }
            else {
                y0 = node.getPoint().y();
            }
            node.right = insertHelper(node.right, p, !isVertical, x0, x1, y0, y1);
        }
        return node;
    }

    public boolean contains(Point2D pointer) {
        validateNotNull(pointer, "pointer");
        return search(root, pointer) != null;
    }

    public void draw() {
        drawHelper(root, 0, 1, 0, 1);
    }

    private void drawHelper(Node node, double x0, double x1, double y0, double y1) {
        if (node == null) {
            return;
        }
        StdDraw.setPenRadius();
        if (node.getVertical()) {
            drawVLine(node.getPoint().x(), node.getPoint().x(), y0, y1);
            drawNode(node);
            drawHelper(node.left, x0, node.getPoint().x(), y0, y1);
            drawHelper(node.right, node.getPoint().x(), x1, y0, y1);
        }
        else {
            drawHLine(x0, x1, node.getPoint().y(), node.getPoint().y());
            drawNode(node);
            drawHelper(node.left, x0, x1, y0, node.getPoint().y());
            drawHelper(node.right, x0, x1, node.getPoint().y(), y1);
        }
    }

    private void drawNode(Node node) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.getPoint().draw();
    }

    private void drawVLine(double x0, double x1, double y0, double y1) {
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius();
        StdDraw.line(x0, y0, x1, y1);
    }

    private void drawHLine(double x0, double x1, double y0, double y1) {
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius();
        StdDraw.line(x0, y0, x1, y1);
    }

    private Node search(Node node, Point2D p) {
        if (node == null) {
            return null;
        }
        if (node.isEqual(p)) {
            return node;
        }
        if (node.isBigger(p)) {
            return search(node.left, p);
        }
        else {
            return search(node.right, p);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        validateNotNull(rect, "rect");
        if (size == 0) {
            return new LinkedList<>();
        }
        return rangeHelper(root, rect);
    }

    private Iterable<Point2D> rangeHelper(Node cur, RectHV rect) {
        List<Point2D> points = new LinkedList<>();
        if (cur == null) {
            return points;
        }
        if (between(cur, rect)) {
            if (rect.contains(cur.getPoint())) {
                points.add(cur.getPoint());
            }
            List<Point2D> left = (List<Point2D>) rangeHelper(cur.left, rect);
            List<Point2D> right = (List<Point2D>) rangeHelper(cur.right, rect);
            points.addAll(left);
            points.addAll(right);
            return points;
        }
        if (cur.getVertical()) {
            if (rect.xmax() < cur.getPoint().x()) {
                return rangeHelper(cur.left, rect);
            }
            if (rect.xmin() > cur.getPoint().x()) {
                return rangeHelper(cur.right, rect);
            }
        }
        else {
            if (rect.ymax() < cur.getPoint().y()) {
                return rangeHelper(cur.left, rect);
            }
            if (rect.ymin() > cur.getPoint().y()) {
                return rangeHelper(cur.right, rect);
            }
        }
        return points;
    }

    private boolean between(Node cur, RectHV rect) {
        if (cur.getVertical()) {
            return rect.xmin() <= cur.getPoint().x() && rect.xmax() >= cur.getPoint().x();
        }
        return rect.ymin() <= cur.getPoint().y() && rect.ymax() >= cur.getPoint().y();
    }

    public Point2D nearest(Point2D p) {
        validateNotNull(p, "p");
        if (size == 0) {
            return null;
        }
        double champion = Double.POSITIVE_INFINITY;
        return nearestHelper(root, p, champion).getPoint();
    }
    
    private static Node nearestHelper(Node cur, Point2D p, double champion) {
        if (cur == null) {
            return null;
        }
        Node ansNode;
        double curDist = distance(cur, p);
        if (curDist < champion) {
            champion = curDist;
            ansNode = cur;
        }
        else {
            ansNode = null;
        }
        boolean atLeft = atLeft(cur, p);
        if (atLeft) {
            Node ret = searchJudge(cur.left, cur.right, p, champion);
            if (ret != null) {
                ansNode = ret;
            }
        }
        else {
            Node ret = searchJudge(cur.right, cur.left, p, champion);
            if (ret != null) {
                ansNode = ret;
            }
        }
        return ansNode;
    }


    private static Node searchJudge(Node first, Node second, Point2D p, double champion) {
        Node ansNode = null, right;
        if (first != null) {
            ansNode = nearestHelper(first, p, champion);
        }
        if (ansNode != null) {
            champion = distance(ansNode, p);
        }
        if (second != null && second.distanceRect(p) <= champion) {
            right = nearestHelper(second, p, champion);
        }
        else {
            right = null;
        }
        if (right != null) {
            ansNode = right;
        }
        return ansNode;
    }

    private static boolean atLeft(Node cur, Point2D p) {
        if (cur.getVertical()) {
            return p.x() < cur.getPoint().x();
        }
        return p.y() < cur.getPoint().y();
    }

    private static double distance(Node cur, Point2D p2) {
        return cur.getPoint().distanceSquaredTo(p2);
    }

    private void validateNotNull(Object obj, String name) {
        if (obj == null) {
            throw new IllegalArgumentException(name + " is null");
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int level = 0;
        toStringHelper(root, sb, level);
        return sb.toString();
    }

    private void toStringHelper(Node node, StringBuilder sb, int level) {
        if (node == null) {
            return;
        }
        for (int i = 0; i < level; i++) {
            sb.append("  ");
        }
        sb.append(node.toString());
        sb.append("\n");
        if (node.left == null && node.right != null) {
            for (int i = 0; i < level + 1; i++) {
                sb.append("  ");
            }
            sb.append("left is null\n");
        }
        toStringHelper(node.left, sb, level + 1);
        toStringHelper(node.right, sb, level + 1);
        if (node.left != null && node.right == null) {
            for (int i = 0; i < level + 1; i++) {
                sb.append("  ");
            }
            sb.append("right is null\n");
        }
    }

    public static void main(String[] args) {
        // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }

        // process nearest neighbor queries
        StdDraw.enableDoubleBuffering();
        while (true) {

            // the location (x, y) of the mouse
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();
            Point2D query = new Point2D(x, y);

            // draw all of the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            brute.draw();

            // draw in red the nearest neighbor (using brute-force algorithm)
            StdDraw.setPenRadius(0.03);
            StdDraw.setPenColor(StdDraw.RED);
            brute.nearest(query).draw();
            StdDraw.setPenRadius(0.02);

            // draw in blue the nearest neighbor (using kd-tree algorithm)
            StdDraw.setPenColor(StdDraw.BLUE);
            kdtree.nearest(query).draw();
            StdDraw.show();
            StdDraw.pause(40);
        }
    }
}