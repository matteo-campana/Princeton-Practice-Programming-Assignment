package adt.tree;

public class RBTree<T extends Comparable<T>> {

    private static class RBTreeNode<T extends Comparable<T>> {
        T data;
        RBTreeNode<T> parent;
        RBTreeNode<T> left;
        RBTreeNode<T> right;

        private enum Color {
            RED, BLACK
        }

        Color color;

        public RBTreeNode(T data, Color color, RBTreeNode<T> parent) {
            this.data = data;
            this.color = color;
            this.parent = parent;
            this.left = null;
            this.right = null;
        }

        public RBTreeNode(T data) {
            this.data = data;
            this.parent = null;
            this.left = null;
            this.right = null;
            this.color = Color.RED;
        }
    }

    private RBTreeNode<T> root;

    public RBTree() {
        this.root = null;
    }

    public void insert(T data) {
        RBTreeNode<T> newNode = new RBTreeNode<T>(data);
        if (root == null) {
            root = newNode;
            root.color = RBTreeNode.Color.BLACK;
        } else {
            RBTreeNode<T> current = root;
            RBTreeNode<T> parent = null;
            while (current != null) {
                parent = current;
                if (data.compareTo(current.data) < 0) {
                    current = current.left;
                } else {
                    current = current.right;
                }
            }

            newNode.parent = parent;

            if (data.compareTo(parent.data) < 0) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }

            rebalanceInsert(newNode);
        }
    }

    private void rebalanceInsert(RBTreeNode<T> node) {
        while (node != root && node.parent.color == RBTreeNode.Color.RED) {
            if (node.parent == node.parent.parent.left) {
                RBTreeNode<T> uncle = node.parent.parent.right;
                if (uncle != null && uncle.color == RBTreeNode.Color.RED) {
                    node.parent.color = RBTreeNode.Color.BLACK;
                    uncle.color = RBTreeNode.Color.BLACK;
                    node.parent.parent.color = RBTreeNode.Color.RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.right) {
                        node = node.parent;
                        rotateLeft(node);
                    }
                    node.parent.color = RBTreeNode.Color.BLACK;
                    node.parent.parent.color = RBTreeNode.Color.RED;
                    rotateRight(node.parent.parent);
                }
            } else {
                RBTreeNode<T> uncle = node.parent.parent.left;
                if (uncle != null && uncle.color == RBTreeNode.Color.RED) {
                    node.parent.color = RBTreeNode.Color.BLACK;
                    uncle.color = RBTreeNode.Color.BLACK;
                    node.parent.parent.color = RBTreeNode.Color.RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left) {
                        node = node.parent;
                        rotateRight(node);
                    }
                    node.parent.color = RBTreeNode.Color.BLACK;
                    node.parent.parent.color = RBTreeNode.Color.RED;
                    rotateLeft(node);
                }
            }
        }
        root.color = RBTreeNode.Color.BLACK;
    }

    private void rotateRight(RBTreeNode<T> node) {
        RBTreeNode<T> leftChild = node.left;
        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }
        leftChild.parent = node.parent;
        if (node.parent == null) {
            root = leftChild;
        } else if (node == node.parent.right) {
            node.parent.right = leftChild;
        } else {
            node.parent.left = leftChild;
        }
        leftChild.right = node;
        node.parent = leftChild;
    }

    private void rotateLeft(RBTreeNode<T> node) {
        RBTreeNode<T> rightChild = node.right;
        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }
        rightChild.parent = node.parent;
        if (node.parent == null) {
            root = rightChild;
        } else if (node == node.parent.left) {
            node.parent.left = rightChild;
        } else {
            node.parent.right = rightChild;
        }
        rightChild.left = node;
        node.parent = rightChild;
    }

    public void delete(T data) {
        RBTreeNode<T> node = search(root, data);
        if (node == null) return;

        RBTreeNode<T> y = node;
        RBTreeNode<T> x;
        RBTreeNode.Color originalColor = y.color;

        if (node.left == null) {
            x = node.right;
            transplant(node, node.right);
        } else if (node.right == null) {
            x = node.left;
            transplant(node, node.left);
        } else {
            y = minimum(node.right);
            originalColor = y.color;
            x = y.right;
            if (y.parent == node) {
                if (x != null) x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = node.right;
                y.right.parent = y;
            }
            transplant(node, y);
            y.left = node.left;
            y.left.parent = y;
            y.color = node.color;
        }

        if (originalColor == RBTreeNode.Color.BLACK) {
            rebalanceDelete(x);
        }
    }

    private void rebalanceDelete(RBTreeNode<T> x) {
        while (x != root && (x == null || x.color == RBTreeNode.Color.BLACK)) {
            if (x == x.parent.left) {
                RBTreeNode<T> w = x.parent.right;
                if (w.color == RBTreeNode.Color.RED) {
                    w.color = RBTreeNode.Color.BLACK;
                    x.parent.color = RBTreeNode.Color.RED;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if ((w.left == null || w.left.color == RBTreeNode.Color.BLACK) &&
                        (w.right == null || w.right.color == RBTreeNode.Color.BLACK)) {
                    w.color = RBTreeNode.Color.RED;
                    x = x.parent;
                } else {
                    if (w.right == null || w.right.color == RBTreeNode.Color.BLACK) {
                        if (w.left != null) w.left.color = RBTreeNode.Color.BLACK;
                        w.color = RBTreeNode.Color.RED;
                        rotateRight(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = RBTreeNode.Color.BLACK;
                    if (w.right != null) w.right.color = RBTreeNode.Color.BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                RBTreeNode<T> w = x.parent.left;
                if (w.color == RBTreeNode.Color.RED) {
                    w.color = RBTreeNode.Color.BLACK;
                    x.parent.color = RBTreeNode.Color.RED;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if ((w.right == null || w.right.color == RBTreeNode.Color.BLACK) &&
                        (w.left == null || w.left.color == RBTreeNode.Color.BLACK)) {
                    w.color = RBTreeNode.Color.RED;
                    x = x.parent;
                } else {
                    if (w.left == null || w.left.color == RBTreeNode.Color.BLACK) {
                        if (w.right != null) w.right.color = RBTreeNode.Color.BLACK;
                        w.color = RBTreeNode.Color.RED;
                        rotateLeft(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = RBTreeNode.Color.BLACK;
                    if (w.left != null) w.left.color = RBTreeNode.Color.BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        if (x != null) x.color = RBTreeNode.Color.BLACK;
    }

    private void transplant(RBTreeNode<T> u, RBTreeNode<T> v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }

    private RBTreeNode<T> minimum(RBTreeNode<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private RBTreeNode<T> search(RBTreeNode<T> node, T data) {
        while (node != null && data.compareTo(node.data) != 0) {
            if (data.compareTo(node.data) < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return node;
    }
}