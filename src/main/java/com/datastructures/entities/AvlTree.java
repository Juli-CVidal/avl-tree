package com.datastructures.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvlTree<K extends Comparable<K>, V> {

    private AvlNode<K, V> root;


    /**
     * Sets the parent of the given node to the parent of the old root.
     *
     * @param node The node whose parent is being updated.
     * @param root The old root of the tree.
     */
    private void setNewRootParent(AvlNode<K, V> node, AvlNode<K, V> root) {
        node.setParent(root.getParent());

        if (!root.hasParent()) {
            this.root = node;
        } else {
            if (root.isRightChild()) {
                root.getParent().setRightNode(node);
            } else {
                root.getParent().setLeftNode(node);
            }
        }
    }


    /**
     * Perform a right rotation on the given node.
     *
     * @param node The node to rotate.
     */
    private void rightRotate(AvlNode<K, V> node) {
        AvlNode<K, V> newRoot = node.getLeftNode();
        node.setLeftNode(newRoot.getRightNode());

        if (newRoot.hasRightChild()) {
            newRoot.getRightNode().setParent(node);
        }

        setNewRootParent(newRoot, node);

        newRoot.setRightNode(node);
        node.setParent(newRoot);

        newRoot.updateBalanceFactorAndHeight();
        node.updateBalanceFactorAndHeight();
    }


    /**
     * Perform a left rotation on the given node.
     *
     * @param node The node to rotate.
     */
    private void leftRotate(AvlNode<K, V> node) {
        AvlNode<K, V> newRoot = node.getRightNode();
        node.setRightNode(newRoot.getLeftNode());

        if (newRoot.hasLeftChild()) {
            newRoot.getLeftNode().setParent(node);
        }

        setNewRootParent(newRoot, node);

        newRoot.setLeftNode(node);
        node.setParent(newRoot);

        newRoot.updateBalanceFactorAndHeight();
        node.updateBalanceFactorAndHeight();
    }


    /**
     * Perform a right-left rotation on the given node.
     *
     * @param node The node to rotate.
     */
    private void rightLeftRotate(AvlNode<K, V> node) {
        rightRotate(node.getRightNode());
        leftRotate(node);
    }


    /**
     * Perform a left-right rotation on the given node.
     *
     * @param node The node to rotate.
     */
    private void leftRightRotate(AvlNode<K, V> node) {
        leftRotate(node.getLeftNode());
        rightRotate(node);
    }


    /**
     * Apply the appropriate rotation to the indicated node according to its factor balance and that of its children.
     *
     * @param node The node to rebalance.
     */
    private void reBalance(AvlNode<K, V> node) {

        if (node.getBalanceFactor() > 1) { //The left subtree is taller than the right subtree
            AvlNode<K, V> leftNode = node.getLeftNode();
            if (leftNode.getBalanceFactor() > 0) {
                rightRotate(node);
            } else {
                leftRightRotate(node);
            }
            return;
        }

        if (node.getBalanceFactor() < -1) { //The right subtree is taller than the left subtree
            AvlNode<K, V> rightNode = node.getRightNode();
            if (rightNode.getBalanceFactor() < 0) {
                leftRotate(node);
            } else {
                rightLeftRotate(node);
            }
        }

    }


    /**
     * Checks the balance factor of an indicated node and their parents.
     * <p>If one of the nodes has an incorrect balance factor, performs the corresponding rebalance.
     *
     * @param node The node on which the balance factor is checked.
     */
    private void checkBalanceFactor(AvlNode<K, V> node) {
        if (node == null) {
            return;
        }

        node.updateBalanceFactorAndHeight();
        if (!node.hasCorrectBalanceFactor()) {
            reBalance(node);
        }
        checkBalanceFactor(node.getParent());

    }


    /**
     * Recursively searches for the correct place to put the new node in the tree. Once the correct place is found, the new node is added to the tree.
     *
     * @param node    The current node.
     * @param newNode The node to be placed.
     */
    private void recursiveAdd(AvlNode<K, V> node, AvlNode<K, V> newNode) {
        int comparison = newNode.getKey().compareTo(node.getKey());

        if (comparison > 0) {
            if (!node.hasRightChild()) {
                node.setRightNode(newNode);
                newNode.setParent(node);
                newNode.setHeight(node.getHeight() + 1);
            } else {
                recursiveAdd(node.getRightNode(), newNode);
            }
        } else if (comparison < 0) {
            if (!node.hasLeftChild()) {
                node.setLeftNode(newNode);
                newNode.setParent(node);
                newNode.setHeight(node.getHeight() + 1);
            } else {
                recursiveAdd(node.getLeftNode(), newNode);
            }
        }
    }


    /**
     * Adds a new node to the tree.
     *
     * @param key   The key of the new node.
     * @param value The value of the new node.
     */
    public void add(K key, V value) {
        AvlNode<K, V> newNode = new AvlNode<>(key, value);

        if (this.root == null) {
            this.root = newNode;
            newNode.setHeight(0);
            return;
        }

        recursiveAdd(this.root, newNode);
        checkBalanceFactor(newNode);
    }


    /**
     * Recursively searches for the node that has the indicated value.
     * @param current The current node.
     * @param value The indicated value.
     * @return K the key of the node that has the value, or `null` if the value is not found.
     */
    private K searchForKey(AvlNode<K, V> current, V value) {
        if (current == null) {
            return null;
        }

        if (current.getValue().equals(value)) {
            return current.getKey();
        }

        K foundKey = searchForKey(current.getLeftNode(), value);

        return foundKey != null ? foundKey : searchForKey(current.getRightNode(), value);
    }


    /**
     * Returns the key of the node that has the indicated value.
     * @param value The indicated value.
     * @return K the key of the node that has the value, or `null` if the value is not found.
     */
    public K getKey(V value) {
        return this.root == null ? null : searchForKey(this.root, value);
    }
}
