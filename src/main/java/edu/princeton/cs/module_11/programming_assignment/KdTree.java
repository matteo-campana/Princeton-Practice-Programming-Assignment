package edu.princeton.cs.module_11.programming_assignment;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;

public class KdTree {

    private static class KdTreeNode {
        Point2D point;
        KdTreeNode parent, left, right;

        public KdTreeNode(Point2D point) {
            this.point = point;
        }
    }

    private KdTreeNode root;
    private int size;

    public KdTree()                               // construct an empty set of points
    {
        this.root = null;
        this.size = 0;
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return this.size() == 0;
    }

    public int size()                         // number of points in the set
    {
        return this.size;
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new IllegalArgumentException();
        insert(p, root);
    }

    private KdTreeNode insert(Point2D p, KdTreeNode parent) {
        if (parent == null) {
            KdTreeNode newNode = new KdTreeNode(p);
            this.root = newNode;
            this.size++;
            return newNode;
        } else if (parent.point.compareTo(p) > 0) {
            if (parent.right == null) {
                KdTreeNode newNode = new KdTreeNode(p);
                newNode.parent = parent;
                parent.right = newNode;
                this.size++;
            } else {
                return insert(p, parent.right);
            }
        } else if (parent.point.compareTo(p) < 0) {
            if (parent.right == null) {
                KdTreeNode newNode = new KdTreeNode(p);
                newNode.parent = parent;
                parent.left = newNode;
                this.size++;
            } else {
                return insert(p, parent.left);
            }
        }
        return null;
    }


    public boolean contains(Point2D p)            // does the set contain point p?
    {
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

    public void draw()                         // draw all points to standard draw
    {
    }

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        return null;
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

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
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

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {
    }
}
