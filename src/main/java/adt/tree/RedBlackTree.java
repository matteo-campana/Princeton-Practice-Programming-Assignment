package adt.tree;

public class RedBlackTree<T extends Comparable<T>> {

    private static class RedBlackTreeNode<T extends Comparable<T>> {
        T data;
        RedBlackTreeNode<T> parent;
        RedBlackTreeNode<T> left;
        RedBlackTreeNode<T> right;

        private enum Color {
            RED, BLACK
        }

        Color color;

        public RedBlackTreeNode(T data, Color color, RedBlackTreeNode<T> parent) {
            this.data = data;
            this.color = color;
            this.parent = parent;
            this.left = null;
            this.right = null;
        }

        public RedBlackTreeNode(T data) {
            this.data = data;
            this.parent = null;
            this.left = null;
            this.right = null;
            this.color = Color.RED;
        }
    }

    private RedBlackTreeNode<T> root;

    public RedBlackTree() {
        this.root = null;
    }

    public void insert(T data) {
        RedBlackTreeNode<T> newNode = new RedBlackTreeNode<T>(data);
        if (root == null) {
            root = newNode;
            root.color = RedBlackTreeNode.Color.BLACK;
        } else {
            RedBlackTreeNode<T> current = root;
            RedBlackTreeNode<T> parent = null;
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

    private void rebalanceInsert(RedBlackTreeNode<T> node) {
        while (node != root && node.parent.color == RedBlackTreeNode.Color.RED) {
            if (node.parent == node.parent.parent.left) {
                RedBlackTreeNode<T> uncle = node.parent.parent.right;
                if (uncle != null && uncle.color == RedBlackTreeNode.Color.RED) {
                    node.parent.color = RedBlackTreeNode.Color.BLACK;
                    uncle.color = RedBlackTreeNode.Color.BLACK;
                    node.parent.parent.color = RedBlackTreeNode.Color.RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.right) {
                        node = node.parent;
                        rotateLeft(node);
                    }
                    node.parent.color = RedBlackTreeNode.Color.BLACK;
                    node.parent.parent.color = RedBlackTreeNode.Color.RED;
                    rotateRight(node.parent.parent);
                }
            } else {
                RedBlackTreeNode<T> uncle = node.parent.parent.left;
                if (uncle != null && uncle.color == RedBlackTreeNode.Color.RED) {
                    node.parent.color = RedBlackTreeNode.Color.BLACK;
                    uncle.color = RedBlackTreeNode.Color.BLACK;
                    node.parent.parent.color = RedBlackTreeNode.Color.RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left) {
                        node = node.parent;
                        rotateRight(node);
                    }
                    node.parent.color = RedBlackTreeNode.Color.BLACK;
                    node.parent.parent.color = RedBlackTreeNode.Color.RED;
                    rotateLeft(node);
                }
            }
        }
        root.color = RedBlackTreeNode.Color.BLACK;
    }

    private void rotateRight(RedBlackTreeNode<T> node) {
        RedBlackTreeNode<T> leftChild = node.left;
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

    private void rotateLeft(RedBlackTreeNode<T> node) {
        RedBlackTreeNode<T> rightChild = node.right;
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
        RedBlackTreeNode<T> node = search(root, data);
        if (node == null) return;

        RedBlackTreeNode<T> y = node;
        RedBlackTreeNode<T> x;
        RedBlackTreeNode.Color originalColor = y.color;

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

        if (originalColor == RedBlackTreeNode.Color.BLACK) {
            rebalanceDelete(x);
        }
    }

    private void rebalanceDelete(RedBlackTreeNode<T> x) {
        while (x != root && (x == null || x.color == RedBlackTreeNode.Color.BLACK)) {
            if (x == x.parent.left) {
                RedBlackTreeNode<T> w = x.parent.right;
                if (w.color == RedBlackTreeNode.Color.RED) {
                    w.color = RedBlackTreeNode.Color.BLACK;
                    x.parent.color = RedBlackTreeNode.Color.RED;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if ((w.left == null || w.left.color == RedBlackTreeNode.Color.BLACK) &&
                        (w.right == null || w.right.color == RedBlackTreeNode.Color.BLACK)) {
                    w.color = RedBlackTreeNode.Color.RED;
                    x = x.parent;
                } else {
                    if (w.right == null || w.right.color == RedBlackTreeNode.Color.BLACK) {
                        if (w.left != null) w.left.color = RedBlackTreeNode.Color.BLACK;
                        w.color = RedBlackTreeNode.Color.RED;
                        rotateRight(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = RedBlackTreeNode.Color.BLACK;
                    if (w.right != null) w.right.color = RedBlackTreeNode.Color.BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                RedBlackTreeNode<T> w = x.parent.left;
                if (w.color == RedBlackTreeNode.Color.RED) {
                    w.color = RedBlackTreeNode.Color.BLACK;
                    x.parent.color = RedBlackTreeNode.Color.RED;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if ((w.right == null || w.right.color == RedBlackTreeNode.Color.BLACK) &&
                        (w.left == null || w.left.color == RedBlackTreeNode.Color.BLACK)) {
                    w.color = RedBlackTreeNode.Color.RED;
                    x = x.parent;
                } else {
                    if (w.left == null || w.left.color == RedBlackTreeNode.Color.BLACK) {
                        if (w.right != null) w.right.color = RedBlackTreeNode.Color.BLACK;
                        w.color = RedBlackTreeNode.Color.RED;
                        rotateLeft(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = RedBlackTreeNode.Color.BLACK;
                    if (w.left != null) w.left.color = RedBlackTreeNode.Color.BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        if (x != null) x.color = RedBlackTreeNode.Color.BLACK;
    }

    private void transplant(RedBlackTreeNode<T> u, RedBlackTreeNode<T> v) {
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

    private RedBlackTreeNode<T> minimum(RedBlackTreeNode<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private RedBlackTreeNode<T> search(RedBlackTreeNode<T> node, T data) {
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