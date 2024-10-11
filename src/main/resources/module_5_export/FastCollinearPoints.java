

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    private final Point[] points;
    private LineSegment[] segments;

    public FastCollinearPoints(Point[] points)     // finds all line segments containing 4 or more points
    {
        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException();
            }
        }

        this.points = Arrays.copyOf(points, points.length);
    }

    public int numberOfSegments()        // the number of line segments
    {
        ArrayList<LineSegment> ls = new ArrayList<LineSegment>();
        int countPointsInSegment;
        double currentSlope;

        // identify segments containing at least 4 points
        for (Point p : points) {
            Point[] sortedPoints = Arrays.copyOf(points, points.length);
            Arrays.sort(sortedPoints, p.slopeOrder());
            currentSlope = p.slopeTo(sortedPoints[1]);
            ArrayList<Point> currentSegment = new ArrayList<Point>();
            currentSegment.add(p);
            currentSegment.add(sortedPoints[1]);
            for (int i = 2; i < sortedPoints.length; i++) {
                if (currentSlope != p.slopeTo(sortedPoints[i])) {
                    if (currentSegment.size() >= 4) {
                        Point[] points_tmp = currentSegment.toArray(new Point[0]);
                        Arrays.sort(points_tmp);
                        ls.add(new LineSegment(points_tmp[0], points_tmp[points_tmp.length - 1]));
                    }
                    currentSegment.clear();
                    currentSegment.add(p);
                    currentSlope = p.slopeTo(sortedPoints[i]);
                }
                currentSegment.add(sortedPoints[i]);
            }
            if (currentSegment.size() >= 4) {
                Point[] points_tmp = currentSegment.toArray(new Point[0]);
                Arrays.sort(points_tmp);
                ls.add(new LineSegment(points_tmp[0], points_tmp[points_tmp.length - 1]));
            }
        }

        // Remove duplicates
        ArrayList<LineSegment> uniqueSegments = new ArrayList<>();
        LineSegment prev = null;
        for (LineSegment segment : ls) {
            if (prev == null || !segment.toString().equals(prev.toString())) {
                uniqueSegments.add(segment);
            }
            prev = segment;
        }

        segments = ls.toArray(new LineSegment[0]);
        return ls.size();
    }

    public LineSegment[] segments()                // the line segments
    {
        return segments;
    }

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