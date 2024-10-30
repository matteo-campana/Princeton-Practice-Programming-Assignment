import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KdTree {

    private static class KdTreeNode {
        Point2D point;
        KdTreeNode parent;
        KdTreeNode left;
        KdTreeNode right;
        int depth;

        public KdTreeNode(Point2D point) {
            this.point = point;
            parent = null;
            left = null;
            right = null;
            depth = 1;
        }
    }

    private KdTreeNode root;
    private int size;

    public KdTree() {
        this.root = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public int size() {
        return this.size;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        insert(p, root);
    }

    private KdTreeNode insert(Point2D p, KdTreeNode parent) {
        if (parent == null) {
            KdTreeNode newNode = new KdTreeNode(p);
            this.root = newNode;
            this.size++;
            calculateNodeHeight(newNode);
            return newNode;
        } else if (parent.point.compareTo(p) > 0) {
            if (parent.left == null) {
                KdTreeNode newNode = new KdTreeNode(p);
                newNode.parent = parent;
                parent.left = newNode;
                this.size++;
                calculateNodeHeight(newNode);
                return newNode;
            } else {
                return insert(p, parent.left);
            }
        } else if (parent.point.compareTo(p) < 0) {
            if (parent.right == null) {
                KdTreeNode newNode = new KdTreeNode(p);
                newNode.parent = parent;
                parent.right = newNode;
                this.size++;
                calculateNodeHeight(newNode);
                return newNode;
            } else {
                return insert(p, parent.right);
            }
        }
        return null;
    }

    private void calculateNodeHeight(KdTreeNode node) {
        if (node == null) return;
        node.depth = (node.left != null ? node.left.depth : 0) + (node.right != null ? node.right.depth : 0) + 1;
        calculateNodeHeight(node.parent);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return search(p, this.root) != null;
    }


    private KdTreeNode search(Point2D p, KdTreeNode node) {
        if (node == null) {
            return null;
        }
        if (node.point.equals(p)) {
            return node;
        }

        boolean isVertical = node.depth % 2 == 1;
        if ((isVertical && p.x() < node.point.x()) || (!isVertical && p.y() < node.point.y())) {
            return search(p, node.left);
        } else {
            return search(p, node.right);
        }
    }

    public void draw() {
        draw(this.root);
    }

    private void draw(KdTreeNode node) {
        if (node == null) return;
        StdDraw.point(node.point.x(), node.point.y());
        if (node.depth % 2 == 0) {
            StdDraw.line(Double.NEGATIVE_INFINITY, node.point.y(), Double.POSITIVE_INFINITY, node.point.y());
        } else {
            StdDraw.line(node.point.x(), Double.NEGATIVE_INFINITY, node.point.x(), Double.POSITIVE_INFINITY);
        }
        draw(node.left);
        draw(node.right);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        List<Point2D> points = new ArrayList<>();
        range(this.root, rect, points);
        return points;
    }

    private void range(KdTreeNode node, RectHV rect, List<Point2D> points) {
        if (node == null) return;

        if (rect.contains(node.point)) {
            points.add(node.point);
        }

        boolean isVertical = node.depth % 2 == 1;
        if (isVertical) {
            if (rect.xmin() <= node.point.x() && node.left != null) {
                range(node.left, rect, points);
            }
            if (rect.xmax() >= node.point.x() && node.right != null) {
                range(node.right, rect, points);
            }
        } else {
            if (rect.ymin() <= node.point.y() && node.left != null) {
                range(node.left, rect, points);
            }
            if (rect.ymax() >= node.point.y() && node.right != null) {
                range(node.right, rect, points);
            }
        }
    }


    private boolean doesLineIntersectRectangle(Point2D p, RectHV rect, boolean isHorizontal) {
        RectHV lineRec;
        if (isHorizontal) {
            lineRec = new RectHV(Double.NEGATIVE_INFINITY, p.y(), Double.POSITIVE_INFINITY, p.y());
        } else {
            lineRec = new RectHV(p.x(), Double.NEGATIVE_INFINITY, p.x(), Double.POSITIVE_INFINITY);
        }
        return rect.intersects(lineRec);
    }


    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (this.root == null) return null;
        return nearest(this.root, p, this.root.point, Double.POSITIVE_INFINITY);
    }

    private Point2D nearest(KdTreeNode node, Point2D p, Point2D nearest, double nearestDistance) {
        if (node == null) return nearest;

        double distance = p.distanceSquaredTo(node.point);
        if (distance < nearestDistance) {
            nearest = node.point;
            nearestDistance = distance;
        }

        KdTreeNode first, second;
        boolean isVertical = node.depth % 2 == 1;
        if ((isVertical && p.x() < node.point.x()) || (!isVertical && p.y() < node.point.y())) {
            first = node.left;
            second = node.right;
        } else {
            first = node.right;
            second = node.left;
        }

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

        // Test isEmpty and size
        System.out.println("Is empty: " + kdTree.isEmpty()); // Expected: true
        System.out.println("Size: " + kdTree.size()); // Expected: 0

        // Insert points
        Point2D p1 = new Point2D(0.1, 0.2);
        Point2D p2 = new Point2D(0.3, 0.4);
        Point2D p3 = new Point2D(0.5, 0.6);
        kdTree.insert(p1);
        kdTree.insert(p2);
        kdTree.insert(p3);

        // Test isEmpty and size after insertion
        System.out.println("Is empty: " + kdTree.isEmpty()); // Expected: false
        System.out.println("Size: " + kdTree.size()); // Expected: 3

        // Test contains
        System.out.println("Contains p1: " + kdTree.contains(p1)); // Expected: true
        System.out.println("Contains p2: " + kdTree.contains(p2)); // Expected: true
        System.out.println("Contains new point: " + kdTree.contains(new Point2D(0.7, 0.8))); // Expected: false

        // Test range search
        RectHV rect = new RectHV(0.0, 0.0, 0.4, 0.4);
        Iterable<Point2D> pointsInRange = kdTree.range(rect);
        System.out.println("Points in range:");
        for (Point2D p : pointsInRange) {
            System.out.println(p);
        }

        // Test nearest
        Point2D queryPoint = new Point2D(0.2, 0.3);
        Point2D nearestPoint = kdTree.nearest(queryPoint);
        System.out.println("Nearest point to " + queryPoint + ": " + nearestPoint);
    }
}