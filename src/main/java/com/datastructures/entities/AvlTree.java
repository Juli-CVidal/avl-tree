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

    private void rightRotate(AvlNode<K, V> root) {
        AvlNode<K, V> newRoot = root.getLeftNode();
        root.setLeftNode(newRoot.getRightNode());

        if (newRoot.hasRightChild()) {
            newRoot.getRightNode().setParent(root);
        }

        setNewRootParent(newRoot, root);

        newRoot.setRightNode(root);
        root.setParent(newRoot);

        newRoot.updateBalanceFactorAndHeight();
        root.updateBalanceFactorAndHeight();
    }

    private void leftRotate(AvlNode<K, V> root) {
        AvlNode<K, V> newRoot = root.getRightNode();
        root.setRightNode(newRoot.getLeftNode());

        if (newRoot.hasLeftChild()) {
            newRoot.getLeftNode().setParent(root);
        }

        setNewRootParent(newRoot, root);

        newRoot.setLeftNode(root);
        root.setParent(newRoot);

        newRoot.updateBalanceFactorAndHeight();
        root.updateBalanceFactorAndHeight();
    }


    private void rightLeftRotate(AvlNode<K, V> node) {
        rightRotate(node.getRightNode());
        leftRotate(node);
    }

    private void leftRightRotate(AvlNode<K, V> node) {
        leftRotate(node.getLeftNode());
        rightRotate(node);
    }

    private void reBalance(AvlNode<K, V> node) {

        if (node.getBalanceFactor() > 1) {
            AvlNode<K, V> leftNode = node.getLeftNode();
            if (leftNode.getBalanceFactor() > 0) {
                rightRotate(node);
            } else {
                leftRightRotate(node);
            }
            return;
        }

        if (node.getBalanceFactor() < -1) {
            AvlNode<K, V> rightNode = node.getRightNode();
            if (rightNode.getBalanceFactor() < 0) {
                leftRotate(node);
            } else {
                rightLeftRotate(node);
            }
        }

    }


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

    private void recursiveAdd(AvlNode<K, V> parent, AvlNode<K, V> newNode) {
        int comparison = newNode.getKey().compareTo(parent.getKey());

        if (comparison > 0) {
            if (!parent.hasRightChild()) {
                parent.setRightNode(newNode);
                newNode.setParent(parent);
                newNode.setHeight(parent.getHeight() + 1);
            } else {
                recursiveAdd(parent.getRightNode(), newNode);
            }
        } else if (comparison < 0) {
            if (!parent.hasLeftChild()) {
                parent.setLeftNode(newNode);
                newNode.setParent(parent);
                newNode.setHeight(parent.getHeight() + 1);
            } else {
                recursiveAdd(parent.getLeftNode(), newNode);
            }
        }


    }

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

}
