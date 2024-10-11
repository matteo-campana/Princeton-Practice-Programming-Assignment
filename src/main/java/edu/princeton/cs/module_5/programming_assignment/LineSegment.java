package edu.princeton.cs.module_5.programming_assignment;

public class LineSegment {
    private final Point p;
    private final Point q;

    public LineSegment(Point p, Point q)        // constructs the line segment between points p and q
    {
        this.p = p;
        this.q = q;
    }

    public void draw()                        // draws this line segment
    {
        p.drawTo(q);
    }
    // string representation

    @Override
    public String toString() {
        return p + " -> " + q;
    }

}
