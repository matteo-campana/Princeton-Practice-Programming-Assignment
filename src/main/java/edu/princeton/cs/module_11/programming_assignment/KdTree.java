package edu.princeton.cs.module_11.programming_assignment;

import edu.princeton.cs.algorithms.Queue;
import edu.princeton.cs.algs4.Draw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KdTree {

    private static class KdTreeNode {
        Point2D point;
        KdTreeNode parent, left, right;
        int depth;

        public KdTreeNode(Point2D point) {
            this.point = point;
            parent = left = right = null;
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
        if (node.point.compareTo(p) > 0) {
            return search(p, node.right);
        } else if (node.point.compareTo(p) < 0) {
            return search(p, node.left);
        } else {
            return node;
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
        List<Point2D> points = new ArrayList<>();
        Queue<KdTreeNode> nodes = new Queue<>();
        nodes.enqueue(this.root);

        while (!nodes.isEmpty()) {
            KdTreeNode current = nodes.dequeue();
            boolean isHorizontal = current.depth % 2 == 0;

            if (rect.contains(current.point)) {
                points.add(current.point);
            }

            if (doesLineIntersectRectangle(current.point, rect, isHorizontal)) {
                if (current.left != null) nodes.enqueue(current.left);
                if (current.right != null) nodes.enqueue(current.right);
            } else if (isHorizontal) {
                if (rect.ymin() <= current.point.y() && current.left != null) nodes.enqueue(current.left);
                if (rect.ymax() >= current.point.y() && current.right != null) nodes.enqueue(current.right);
            } else {
                if (rect.xmin() <= current.point.x() && current.left != null) nodes.enqueue(current.left);
                if (rect.xmax() >= current.point.x() && current.right != null) nodes.enqueue(current.right);
            }
        }

        return points;
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

    private static class KdTreeIterator implements Iterator<Point2D> {

        private final Iterator<Point2D> it;

        public KdTreeIterator(KdTreeNode root) {
            ArrayDeque<KdTreeNode> q = new ArrayDeque<>();
            ArrayList<Point2D> l = new ArrayList<>();
            q.add(root);
            while (!q.isEmpty()) {
                KdTreeNode current = q.poll();
                if (current != null) {
                    l.add(current.point);
                }
                if (current != null && current.left != null) {
                    q.add(current.left);
                }
                if (current != null && current.right != null) {
                    q.add(current.right);
                }
            }
            it = l.iterator();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Point2D next() {
            return it.next();
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        KdTreeNode nearest = nearest(p, this.root, this.root, Double.POSITIVE_INFINITY);
        return nearest.point;
    }

    private KdTreeNode nearest(Point2D p, KdTreeNode node, KdTreeNode nearest, double nearestDistance) {
        if (node == null) {
            return nearest;
        }

        double distance = p.distanceSquaredTo(node.point);
        if (distance < nearestDistance) {
            nearest = node;
            nearestDistance = distance;
        }

        KdTreeNode first, second;
        if (p.compareTo(node.point) < 0) {
            first = node.left;
            second = node.right;
        } else {
            first = node.right;
            second = node.left;
        }

        nearest = nearest(p, first, nearest, nearestDistance);
        if (second != null && (nearestDistance > p.distanceSquaredTo(second.point))) {
            nearest = nearest(p, second, nearest, nearestDistance);
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