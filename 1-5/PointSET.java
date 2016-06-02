import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
import java.util.Iterator;

public class PointSET {
    private SET<Point2D> setOfPoints;

    // construct an empty set of points
    public PointSET() {                             
        setOfPoints = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {                      
        return setOfPoints.isEmpty();
    }

    // number of points in the set
    public int size() {                             
        return setOfPoints.size();
    }

    // add the point to the set (if is is not already in the set)
    public void insert(Point2D p) {                 
        if (p == null) {
            throw new java.lang.NullPointerException();
        }

        setOfPoints.add(p);
    }

    // does the set containt point p?
    public boolean contains(Point2D p) {            
        if (p == null) {
            throw new java.lang.NullPointerException();
        }

        return setOfPoints.contains(p);
    }

    // draw all points to standard draw
    public void draw() {                            
        Iterator<Point2D> points = setOfPoints.iterator();

        while (points.hasNext()) {
            Point2D p = points.next();
            p.draw();
        }
    }

    // all points thar are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {   
        if (rect == null) {
            throw new java.lang.NullPointerException();
        }

        Queue<Point2D> pointsInRange = new Queue<Point2D>();

        Iterator<Point2D> points = setOfPoints.iterator();
        while (points.hasNext()) {
            Point2D point = points.next();

            if (rect.contains(point)) {
                pointsInRange.enqueue(point);
            }
        }

        return pointsInRange;
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {             
        if (p == null) {
            throw new java.lang.NullPointerException();
        }

        if (size() == 0) {
            return null;
        }

        Point2D neighbor = null;

        for (Point2D point : setOfPoints) {
            if (neighbor == null) {
                neighbor = point;
            }

            if (point.distanceSquaredTo(p)
                < neighbor.distanceSquaredTo(p)) {
                neighbor = point;
            }
        }

        return neighbor;
    }
    
    // unit testing fo the methods (optional)
    public static void main(String[] args) {        
        PointSET test = new PointSET();
        StdOut.println(test.isEmpty() + ", should be true");

        StdOut.println(test.size() + ", should be 0");

        test.insert(new Point2D(0.1, 0.1));
        StdOut.println(test.size() + ", should be 1");
        test.insert(new Point2D(0.1, 0.1));
        StdOut.println(test.size() + ", should be 1");
        test.insert(new Point2D(0.1, 0.2));
        StdOut.println(test.size() + ", should be 2");

        StdOut.println(test.contains(new Point2D(0.1, 0.1)) + ", should be true");
        StdOut.println(test.contains(new Point2D(0.1, 0.5)) + ", should be false");

        test.insert(new Point2D(0.5, 0.6));
        test.insert(new Point2D(0.7, 0.8));
        test.insert(new Point2D(0.4, 0.1));
        test.draw();

        test = new PointSET();
        test.insert(new Point2D(0.1, 0.1));
        test.insert(new Point2D(0.1, 0.2));
        test.insert(new Point2D(0.2, 0.3));
        test.insert(new Point2D(0.1, 0.4));

        Iterable<Point2D> p = test.range(new RectHV(0.1, 0.1, 0.3, 0.2));
        for (Point2D pointInRange : p) {
            StdOut.println(pointInRange);
        }

        StdOut.println(test.nearest(new Point2D(0.1, 0.2)) + 
                ",should be (0.1, 0.1)");

    }
}
