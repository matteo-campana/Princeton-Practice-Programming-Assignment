import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private static class KdTreeNode {
        Point2D point;
        KdTreeNode left;
        KdTreeNode right;
        int depth;

        public KdTreeNode(Point2D point, int depth) {
            this.point = point;
            this.depth = depth;
            this.left = null;
            this.right = null;
        }
    }

    private KdTreeNode root;
    private int size;

    public KdTree() {
        this.root = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        if (!contains(p)) {
            root = insert(root, p, 0);
        }
    }

    private KdTreeNode insert(KdTreeNode node, Point2D p, int depth) {
        if (node == null) {
            size++;
            return new KdTreeNode(p, depth);
        }
        boolean isVertical = depth % 2 == 0;
        if ((isVertical && p.x() < node.point.x()) || (!isVertical && p.y() < node.point.y())) {
            node.left = insert(node.left, p, depth + 1);
        } else {
            node.right = insert(node.right, p, depth + 1);
        }
        return node;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        return search(root, p) != null;
    }

    private KdTreeNode search(KdTreeNode node, Point2D p) {
        if (node == null) return null;
        if (node.point.equals(p)) return node;

        boolean isVertical = node.depth % 2 == 0;
        if ((isVertical && p.x() < node.point.x()) || (!isVertical && p.y() < node.point.y())) {
            return search(node.left, p);
        } else {
            return search(node.right, p);
        }
    }

    public void draw() {
        StdDraw.setScale(0, 1);
        draw(root, new RectHV(0, 0, 1, 1));
    }

    private void draw(KdTreeNode node, RectHV rect) {
        if (node == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(node.point.x(), node.point.y());

        StdDraw.setPenColor(node.depth % 2 == 0 ? StdDraw.RED : StdDraw.BLUE);
        if (node.depth % 2 == 0) {
            StdDraw.line(node.point.x(), rect.ymin(), node.point.x(), rect.ymax());
            draw(node.left, new RectHV(rect.xmin(), rect.ymin(), node.point.x(), rect.ymax()));
            draw(node.right, new RectHV(node.point.x(), rect.ymin(), rect.xmax(), rect.ymax()));
        } else {
            StdDraw.line(rect.xmin(), node.point.y(), rect.xmax(), node.point.y());
            draw(node.left, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.point.y()));
            draw(node.right, new RectHV(rect.xmin(), node.point.y(), rect.xmax(), rect.ymax()));
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Rectangle cannot be null");
        List<Point2D> points = new ArrayList<>();
        range(root, rect, points);
        return points;
    }

    private void range(KdTreeNode node, RectHV rect, List<Point2D> points) {
        if (node == null) return;
        if (rect.contains(node.point)) points.add(node.point);

        boolean isVertical = node.depth % 2 == 0;
        if (isVertical) {
            if (rect.xmin() <= node.point.x() && node.left != null) range(node.left, rect, points);
            if (rect.xmax() >= node.point.x() && node.right != null) range(node.right, rect, points);
        } else {
            if (rect.ymin() <= node.point.y() && node.left != null) range(node.left, rect, points);
            if (rect.ymax() >= node.point.y() && node.right != null) range(node.right, rect, points);
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        if (root == null) return null;
        return nearest(root, p, root.point, root.point.distanceSquaredTo(p));
    }

    private Point2D nearest(KdTreeNode node, Point2D p, Point2D nearest, double nearestDistance) {
        if (node == null) return nearest;

        double distance = p.distanceSquaredTo(node.point);
        if (distance < nearestDistance) {
            nearest = node.point;
            nearestDistance = distance;
        }

        boolean isVertical = node.depth % 2 == 0;
        KdTreeNode first = (isVertical && p.x() < node.point.x()) || (!isVertical && p.y() < node.point.y())
                ? node.left : node.right;
        KdTreeNode second = first == node.left ? node.right : node.left;

        nearest = nearest(first, p, nearest, nearestDistance);
        if (second != null) {
            double axisDistance = isVertical ? Math.pow(p.x() - node.point.x(), 2) : Math.pow(p.y() - node.point.y(), 2);
            if (axisDistance < nearestDistance) {
                nearest = nearest(second, p, nearest, nearestDistance);
            }
        }

        return nearest;
    }

    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        Point2D p1 = new Point2D(0.1, 0.2);
        Point2D p2 = new Point2D(0.3, 0.4);
        Point2D p3 = new Point2D(0.5, 0.6);
        kdTree.insert(p1);
        kdTree.insert(p2);
        kdTree.insert(p3);

        RectHV rect = new RectHV(0.0, 0.0, 0.4, 0.4);
        Iterable<Point2D> pointsInRange = kdTree.range(rect);
        for (Point2D p : pointsInRange) {
            System.out.println(p);
        }

        Point2D queryPoint = new Point2D(0.2, 0.3);
        Point2D nearestPoint = kdTree.nearest(queryPoint);
        System.out.println("Nearest point to " + queryPoint + ": " + nearestPoint);
    }
}
