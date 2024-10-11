


import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private final Point[] points;
    private LineSegment[] segments;


    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        this.points = Arrays.copyOf(points, points.length);

        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    public int numberOfSegments()        // the number of line segments
    {

        ArrayList<LineSegment> ls = new ArrayList<LineSegment>();

        // create a list with all LineSegments that contains at least 4 points
        for (int i = 0; i < points.length; i++) {
            for (int j = i; j < points.length; j++) {
                for (int k = j; k < points.length; k++) {
                    if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[k])) {
                        break;
                    }
                    for (int l = k; l < points.length; l++) {
                        if (points[i].slopeTo(points[j]) == points[k].slopeTo(points[l])) {
                            ls.add(new LineSegment(points[i], points[l]));
                        }
                    }
                }
            }
        }

        this.segments = ls.toArray(new LineSegment[0]);

        return ls.size();
    }

    public LineSegment[] segments()                // the line segments
    {
        return segments;
    }


}
