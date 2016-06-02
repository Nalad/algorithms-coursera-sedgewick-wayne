/*************************************************************************
 * Name:
 * Email:
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    // YOUR DEFINITION HERE
    public final Comparator<Point> SLOPE_ORDER = new SlopeOrder();       

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    private class SlopeOrder implements Comparator<Point>
    {
        public int compare(Point v, Point w)
        {
            double slopeV = slopeTo(v);
            double slopeW = slopeTo(w);

            if (slopeV < slopeW) return -1;
            if (slopeV > slopeW) return +1;
            return 0;
        }
    }


    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        if ((that.x == this.x) && (that.y == this.y)) {
            return Double.NEGATIVE_INFINITY;
        }
        if (that.x == this.x) {
            return Double.POSITIVE_INFINITY;
        }
        if (that.y == this.y) {
            return +0;
        }

        return ((double) (that.y - this.y)) / (that.x - this.x);
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        if (this.y < that.y) return -1;
        if ((this.y == that.y) && (this.x < that.x)) return -1;
        if ((this.y == that.y) && (this.x == that.x)) return 0;
        return +1;
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    // unit test
    public static void main(String[] args) {
        /* YOUR CODE HERE */
    }
}
