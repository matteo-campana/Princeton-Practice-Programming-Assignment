package adt.tree;

public class KdTree<T extends Comparable<T>> {
    private static class KdTreeNode<T extends Comparable<T>> {
        T[] point;
        KdTreeNode<T> left, right;

        public KdTreeNode(T[] point) {
            this.point = point;
            left = right = null;
        }
    }

    private KdTreeNode<T> root;
    private final int k; // number of dimensions

    public KdTree(int k) {
        this.k = k;
        root = null;
    }

    private void insert(T[] point) {
        root = insertRec(root, point, 0);
    }

    private KdTreeNode<T> insertRec(KdTreeNode<T> root, T[] point, int depth) {
        if (root == null) {
            return new KdTreeNode<>(point);
        }

        int cd = depth % k;

        if (point[cd].compareTo(root.point[cd]) < 0) {
            root.left = insertRec(root.left, point, depth + 1);
        } else {
            root.right = insertRec(root.right, point, depth + 1);
        }

        return root;
    }

    public KdTreeNode<T> searchRect(T[] point) {
        return searchRect(this.root, point, 0);
    }

    private KdTreeNode<T> searchRect(KdTreeNode<T> node, T[] point, int depth) {
        if (node == null) {
            return null;
        }

        if (arePointsEqual(node.point, point)) {
            return node;
        }

        int cd = depth % k;

        if (point[cd].compareTo(node.point[cd]) < 0) {
            return searchRect(node.left, point, depth + 1);
        } else {
            return searchRect(node.right, point, depth + 1);
        }
    }

    private boolean arePointsEqual(T[] point1, T[] point2) {
        for (int i = 0; i < this.k; i++) {
            if (!point1[i].equals(point2[i])) {
                return false;
            }
        }
        return true;
    }

    public void delete(T[] point) {
        root = deleteRec(root, point, 0);
    }

    private KdTreeNode<T> deleteRec(KdTreeNode<T> root, T[] point, int depth) {
        if (root == null) {
            return null;
        }

        int cd = depth % k;

        if (arePointsEqual(root.point, point)) {
            if (root.right != null) {
                KdTreeNode<T> min = findMin(root.right, cd, depth + 1);
                root.point = min.point;
                root.right = deleteRec(root.right, point, depth + 1);
            } else if (root.left != null) {
                KdTreeNode<T> min = findMin(root.left, cd, depth + 1);
                root.point = min.point;
                root.left = null;
            } else {
                return null;
            }
        } else if (point[cd].compareTo(root.point[cd]) < 0) {
            root.left = deleteRec(root.left, point, depth + 1);
        } else {
            root.right = deleteRec(root.right, point, depth + 1);
        }
        return root;
    }

    private KdTreeNode<T> findMin(KdTreeNode<T> root, int d, int depth) {
        if (root == null) {
            return null;
        }

        int cd = depth % k;

        if (cd == d) {
            if (root.left == null) {
                return root;
            }
            return findMin(root.left, d, depth + 1);
        }
        return minNode(root,
                findMin(root.left, d, depth + 1),
                findMin(root.right, d, depth + 1),
                d);
    }

    private KdTreeNode<T> minNode(KdTreeNode<T> x, KdTreeNode<T> y, KdTreeNode<T> z, int d) {
        KdTreeNode<T> res = x;
        if (y != null && y.point[d].compareTo(res.point[d]) < 0) {
            res = y;
        }
        if (z != null && z.point[d].compareTo(res.point[d]) < 0) {
            res = z;
        }
        return res;
    }

}
