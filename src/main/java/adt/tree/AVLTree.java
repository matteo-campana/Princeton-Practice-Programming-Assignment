package adt.tree;

public class AVLTree<T extends Comparable<T>> {
    private static class AVLTreeNode<T extends Comparable<T>> {
        T data;
        AVLTreeNode<T> parent;
        AVLTreeNode<T> left;
        AVLTreeNode<T> right;
        int height;

        public AVLTreeNode(T data) {
            this.data = data;
            parent = null;
            left = null;
            right = null;
            height = 1;
        }

        public AVLTreeNode(T data, AVLTreeNode<T> parent) {
            this.parent = parent;
            this.data = data;
            left = null;
            right = null;
            height = 1;
        }
    }

    private AVLTreeNode<T> root;

    public AVLTree() {
        this.root = null;
    }

    public void insert(T data) {
        root = insert(root, data, null);
    }

    private AVLTreeNode<T> insert(AVLTreeNode<T> node, T data, AVLTreeNode<T> parent) {
        if (node == null) {
            return new AVLTreeNode<>(data, parent);
        }

        if (data.compareTo(node.data) < 0) {
            node.left = insert(node.left, data, node);
        } else if (data.compareTo(node.data) > 0) {
            node.right = insert(node.right, data, node);
        } else {
            return node;
        }

        updateHeight(node);
        return rebalance(node);
    }

    private int height(AVLTreeNode<T> node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(AVLTreeNode<T> node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int getBalance(AVLTreeNode<T> node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private AVLTreeNode<T> rebalance(AVLTreeNode<T> node) {
        int balance = getBalance(node);

        // left heavy
        if (balance > 1) {
            if (getBalance(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }

        // right heavy
        if (balance < -1) {
            if (getBalance(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        return node;
    }

    private AVLTreeNode<T> rotateLeft(AVLTreeNode<T> node) {
        AVLTreeNode<T> newRoot = node.right;
        node.right = newRoot.left;
        if (newRoot.left != null) {
            newRoot.left.parent = node;
        }
        newRoot.left = node;

        newRoot.parent = node.parent;
        node.parent = newRoot;

        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }

    private AVLTreeNode<T> rotateRight(AVLTreeNode<T> node) {
        AVLTreeNode<T> newRoot = node.left;
        node.left = newRoot.right;
        if (newRoot.right != null) {
            newRoot.right.parent = node;
        }
        newRoot.right = node;

        newRoot.parent = node.parent;
        node.parent = newRoot;

        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }

    public void delete(T data) {
        root = delete(this.root, data);
    }

    private AVLTreeNode<T> delete(AVLTreeNode<T> node, T data) {
        if (data == null) {
            return null;
        }

        if (data.compareTo(node.data) < 0) {
            node.left = delete(node.left, data);
        } else if (data.compareTo(node.data) > 0) {
            node.right = delete(node.right, data);
        } else {
            // node with only one child or no children
            if (node.left == null) {
                AVLTreeNode<T> temp = node.right;
                if (temp != null) {
                    temp.parent = node.parent;
                }
                node = temp;
            } else if (node.right == null) {
                AVLTreeNode<T> temp = node.left;
                if (temp != null) {
                    temp.parent = node.parent;
                }
                node = temp;
            } else {
                // node with two children: get the inorder successor (smallest in the right subtree)
                AVLTreeNode<T> temp = minValueNode(node.right);
                node.data = temp.data;
                node.right = delete(node.right, temp.data);
            }
        }

        if (node == null) {
            return null;
        }
        updateHeight(node);
        return rebalance(node);
    }

    private AVLTreeNode<T> minValueNode(AVLTreeNode<T> node) {
        if (node.left != null) {
            return minValueNode(node.left);
        } else {
            return node;
        }
    }

    private AVLTreeNode<T> search(T data) {
        return search(this.root, data);
    }

    private AVLTreeNode<T> search(AVLTreeNode<T> node, T data) {
        if (node == null || data.equals(node.data)) {
            return node;
        }
        if (data.compareTo(node.data) < 0) {
            return search(node.left, data);
        } else {
            return search(node.right, data);
        }
    }

    public void inOrder() {
        inOrder(root);
        System.out.println();
    }

    private void inOrder(AVLTreeNode<T> node) {
        if (node != null) {
            inOrder(node.left);
            System.out.println(node.data + " ");
            inOrder(node.right);
        }
    }
}
