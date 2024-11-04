package edu.princeton.cs.module_5.programming_assignment;

import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final double x;
    private final double y;

    // constructs the point (x, y)
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // draws this point
    public void draw() {
        StdDraw.point(x, y);
    }

    // draws the line segment from this point to that point
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // string representation
    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    // compare two points by y-coordinates, breaking ties by x-coordinates
    public int compareTo(Point that) {
        if (this.y > that.y) return 1;
        else if (this.y == that.y && this.x > that.x) return 1;
        else if (this.y == that.y && this.x == that.x) return 0;
        return -1;
    }

    // the slope between this point and that point
    public double slopeTo(Point that) {
        if (this.compareTo(that) == 0) return .0f;
        if (this.y == that.y) {
            if (this.x > that.x) return -.0f;
            else return .0f;
        }
        if (this.x == that.x) {
            if (this.y > that.y) return Double.POSITIVE_INFINITY;
            else return Double.NEGATIVE_INFINITY;
        }
        return (this.x - that.x) / (this.y - that.y);
    }

    public Comparator<Point> slopeOrder() {
        return new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                double slope1 = Point.this.slopeTo(p1);
                double slope2 = Point.this.slopeTo(p2);
                return Double.compare(slope1, slope2);
            }
        };
    }

    public static void main(String[] args) {

    }
}



