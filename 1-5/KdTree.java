import edu.princeton.cs.algs4.Point2D;   
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private static final boolean VERTICAL = false;
    private static final boolean HORIZONTAL = true;
    private Node root = null;
    private int size = 0;
    private Point2D champion;

    private static class Node {
        // the point
        private Point2D p;
        // the axis-aligned rectangle corresponding to this node
        private RectHV rect;
        // the left/bottom subtree
       private Node lb;
        // the right/top subtree
        private Node rt;
        
        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }

        /*
        public boolean equals(Object y) {
            if (y == this) return true;
            if (y == null) return false;
            if (y.getClass() != this.getClass()) return false;
            Node that = (Node) y;
            if (!this.p.equals(that.p)) return false;
            return true;
        }
        */

    }

    // construct an empty set of points
    public KdTree() {
        root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        if (root == null) {
            return true;
        }

        return false;
    }
    
    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        root = insert(root, p, VERTICAL, 0.0, 0.0, 1.0, 1.0);
    }

    private Node insert(Node x, Point2D p, boolean orientation,
                        double xmin, double ymin, double xmax, double ymax) {
        if (x == null) {
            size++;
            return new Node(p, new RectHV(xmin, ymin, xmax, ymax));
        }

        if (p.equals(x.p)) {
            return x;
        }

        if (orientation == VERTICAL) {
            int cmp = p.X_ORDER.compare(p, x.p);
            if (cmp < 0) x.lb = insert(x.lb, p, HORIZONTAL,
                    xmin, ymin, x.p.x(), ymax);
            else if (cmp >= 0) x.rt = insert(x.rt, p, HORIZONTAL,
                    x.p.x(), ymin, xmax, ymax);
        } else if (orientation == HORIZONTAL) {
            int cmp = p.Y_ORDER.compare(p, x.p);
            if (cmp < 0) x.lb = insert(x.lb, p, VERTICAL,
                    xmin, ymin, xmax, x.p.y());
            else if (cmp >= 0) x.rt = insert(x.rt, p, VERTICAL,
                    xmin, x.p.y(), xmax, ymax);
        }

        return x;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (get(root, p, VERTICAL) != null) {
            return true;
        }

        return false;
    }

    private Node get(Node x, Point2D p, boolean orientation) {
        if (x == null) return null;

        if (p.equals(x.p)) {
            return x;
        }

        if (orientation == VERTICAL) {
            int cmp = p.X_ORDER.compare(p, x.p);
            if (cmp < 0) return get(x.lb, p, HORIZONTAL);
            else return get(x.rt, p, HORIZONTAL);
        } else if (orientation == HORIZONTAL) {
            int cmp = p.Y_ORDER.compare(p, x.p);
            if (cmp < 0) return get(x.lb, p, VERTICAL);
            else return get(x.rt, p, VERTICAL);
        }

        return x;
    }
    
    // draw all points to standard draw
    public void draw() {
        draw(root, VERTICAL, 0.0, 1.0); 
    }

    private void draw(Node x, boolean orientation, double xy1, double xy2) {
        if (x == null) return;
        if (orientation == VERTICAL) {
            draw(x.lb, HORIZONTAL, 0.0, x.p.x());

            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(x.p.x(), xy1, x.p.x(), xy2);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            x.p.draw();

            draw(x.rt, HORIZONTAL, x.p.x(), 1.0);
        } else if (orientation == HORIZONTAL) {
            draw(x.lb, VERTICAL, 0.0, x.p.y());

            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(xy1, x.p.y(), xy2, x.p.y());
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            x.p.draw();

            draw(x.rt, VERTICAL, x.p.y(), 1.0);
        }
    }

    
    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> pointsInRect = new Queue<Point2D>();
        range(root, rect, pointsInRect);
        return pointsInRect;
    }

    private void range(Node x, RectHV r, Queue<Point2D> pointsInRect) {
        if (x == null) return;
        if (x.lb != null
            && x.lb.rect.intersects(r)) {
            range(x.lb, r, pointsInRect);
        }
        if (r.contains(x.p)) {
            pointsInRect.enqueue(x.p);
        }
        if (x.rt != null
            && x.rt.rect.intersects(r)) {
            range(x.rt, r, pointsInRect);
        }
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (root == null) return null;
        champion = root.p;
        p = nearest(root, p, VERTICAL);
        return p;
    }

    private Point2D nearest(Node x, Point2D p, boolean orientation) {
        if (x == null) return champion;

        if (champion.distanceTo(p) < x.rect.distanceTo(p)) {
            return champion;
        }

        double distance = x.p.distanceTo(p);
        if (distance < champion.distanceTo(p)) {
            champion = x.p;
        }

        if (orientation == VERTICAL) {
            if (p.x() <= x.p.x()) {
                nearest(x.lb, p, HORIZONTAL);
                nearest(x.rt, p, HORIZONTAL);
            } else if (p.x() > x.p.x()) {
                nearest(x.rt, p, HORIZONTAL);
                nearest(x.lb, p, HORIZONTAL);
            }
        } else if (orientation == HORIZONTAL) {
            if (p.y() <= x.p.y()) {
                nearest(x.lb, p, VERTICAL);
                nearest(x.rt, p, VERTICAL);
            } else if (p.y() > x.p.y()) {
                nearest(x.rt, p, VERTICAL);
                nearest(x.lb, p, VERTICAL);
            }
        }

        return champion;
    }

    // unit testing of the methods(optional)
    public static void main(String[] args) {
        KdTree test = new KdTree();

        StdOut.println(test.isEmpty() + ", should be true");
        StdOut.println(test.size() + ", should be 0");

        test.insert(new Point2D(0.7, 0.2));
        test.insert(new Point2D(0.5, 0.4));
        test.insert(new Point2D(0.2, 0.3));
        test.insert(new Point2D(0.4, 0.7));
        test.insert(new Point2D(0.9, 0.6));
        test.insert(new Point2D(0.95, 0.7));
        test.insert(new Point2D(0.95, 0.7));
        test.draw();

        StdOut.println(test.isEmpty() + ", should be false");
        StdOut.println(test.size() + ", should be 6");

        StdOut.println(test.contains(new Point2D(0.4, 0.7)) + ", should be true");
        StdOut.println(test.contains(new Point2D(0.5, 0.2)) + ", should be false");

        test = new KdTree();
        test.insert(new Point2D(0.206107, 0.095492));
        test.insert(new Point2D(0.975528, 0.654508));
        test.insert(new Point2D(0.024472, 0.345492));
        test.insert(new Point2D(0.793893, 0.095492));
        test.insert(new Point2D(0.793893, 0.904508));
        test.insert(new Point2D(0.975528, 0.345492));
        test.insert(new Point2D(0.206107, 0.904508));
        test.insert(new Point2D(0.500000, 0.000000));
        test.insert(new Point2D(0.024472, 0.654508));
        test.insert(new Point2D(0.500000, 1.000000));
        //test.draw();
        
        Iterable<Point2D> p = test.range(new RectHV(0.0, 0.0, 0.5, 1.0));
        for (Point2D pointInRange : p) {
            StdOut.println(pointInRange);
        }

        StdOut.println(test.nearest(new Point2D(0.206107, 0.095492)));
    }
}
