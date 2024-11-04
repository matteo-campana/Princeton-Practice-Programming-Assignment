package edu.princeton.cs.part_i.module_11.programming_assignment;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> treeSet;

    public PointSET()                               // construct an empty set of points
    {
        this.treeSet = new TreeSet<>();
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return this.treeSet.isEmpty();
    }

    public int size()                         // number of points in the set
    {
        return this.treeSet.size();
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new IllegalArgumentException();
        this.treeSet.add(p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) throw new IllegalArgumentException();
        return this.treeSet.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        for (Point2D p : this.treeSet) {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new IllegalArgumentException();
        TreeSet<Point2D> rangeSet = new TreeSet<>();
        for (Point2D p : this.treeSet) {
            if (rect.contains(p)) {
                rangeSet.add(p);
            }
        }
        return rangeSet;
    }

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        Point2D nearest = null;
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (Point2D point : this.treeSet) {
            double distance = p.distanceSquaredTo(point);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearest = point;
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
        PointSET pointSet = new PointSET();

        // Test isEmpty and size
        System.out.println("Is empty: " + pointSet.isEmpty()); // Expected: true
        System.out.println("Size: " + pointSet.size()); // Expected: 0

        // Insert points
        Point2D p1 = new Point2D(0.1, 0.2);
        Point2D p2 = new Point2D(0.3, 0.4);
        Point2D p3 = new Point2D(0.5, 0.6);
        pointSet.insert(p1);
        pointSet.insert(p2);
        pointSet.insert(p3);

        // Test isEmpty and size after insertion
        System.out.println("Is empty: " + pointSet.isEmpty()); // Expected: false
        System.out.println("Size: " + pointSet.size()); // Expected: 3

        // Test contains
        System.out.println("Contains p1: " + pointSet.contains(p1)); // Expected: true
        System.out.println("Contains p2: " + pointSet.contains(p2)); // Expected: true
        System.out.println("Contains new point: " + pointSet.contains(new Point2D(0.7, 0.8))); // Expected: false

        // Test range search
        RectHV rect = new RectHV(0.0, 0.0, 0.4, 0.4);
        Iterable<Point2D> pointsInRange = pointSet.range(rect);
        System.out.println("Points in range:");
        for (Point2D p : pointsInRange) {
            System.out.println(p);
        }

        // Test nearest
        Point2D queryPoint = new Point2D(0.2, 0.3);
        Point2D nearestPoint = pointSet.nearest(queryPoint);
        System.out.println("Nearest point to " + queryPoint + ": " + nearestPoint);
    }
}