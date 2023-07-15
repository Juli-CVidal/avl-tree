package com.datastructures.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvlNode<K, V> {

    private static final Integer MIN_BALANCE_FACTOR = -2, MAX_BALANCE_FACTOR = 2;

    private AvlNode<K, V> parent;

    private AvlNode<K, V> leftNode;

    private AvlNode<K, V> rightNode;

    private K key;

    private V value;

    private Integer height;

    private Integer balanceFactor;


    public AvlNode(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Verifies if the node has the indicated key
     *
     * @param key The key to be compared
     * @return true If both keys are equal, false otherwise
     */
    public boolean hasKey(K key) {
        return this.key.equals(key);
    }

    /**
     * Verifies if the node has the indicated value
     *
     * @param value The value to be compared
     * @return true If both values are equal, false otherwise
     */
    public boolean hasValue(V value) {
        return this.value.equals(value);
    }

    /**
     * Updates the node height based on the following:
     * <p>If the node has no parent, then the node is the root, and its height is 0.
     * <p>If the parent's height is not set, then the parent's height is set to 0.
     * <p>The node's height is set to the parent's height plus 1.
     */
    public void updateHeight() {
        if (this.parent == null) {
            this.height = 0;
            return;
        }
        if (this.parent.height == null) {
            this.parent.height = 0;
        }
        this.height = this.parent.height + 1;

    }


    /**
     * Calculates the maximum depth of a node in an AVL tree.
     * The maximum depth of a node is the length of the longest path from the node to a leaf.
     *
     * @param node The node to calculate the maximum depth for
     * @param <K>  The type of the key in the node.
     * @param <V>  The type of the value in the node.
     * @return The maximum depth of the node.
     */
    private static <K, V> int getMaxDepth(AvlNode<K, V> node) {
        return node == null ? 0 : Math.max(getMaxDepth(node.getLeftNode()), getMaxDepth(node.getRightNode())) + 1;
    }

    /**
     * Updates the balance factor of the node.
     * <p>
     * The balance factor of a node is the difference between the heights of the left and right subtrees of the node
     */
    private void updateBalanceFactor() {
        this.balanceFactor = getMaxDepth(this.leftNode) - getMaxDepth(this.rightNode);
    }


    /**
     * Checks if the node has a correct balance factor.
     * <p>
     * The balance factor of a node is the difference between the heights of the left and right subtrees of the node.
     * This difference must be between -1 and 1 (both inclusive), to be considered balanced.
     */
    public boolean hasCorrectBalanceFactor() {
        return MIN_BALANCE_FACTOR < this.balanceFactor && this.balanceFactor < MAX_BALANCE_FACTOR;
    }


    public void updateBalanceFactorAndHeight() {
        updateBalanceFactor();
        updateHeight();
    }


    /**
     * Checks if the node has a left child
     *
     * @return true If the node has a left child, false otherwise
     */
    public boolean hasLeftChild() {
        return this.leftNode != null;
    }


    /**
     * Checks if the node has a right child
     *
     * @return true If the node has a right child, false otherwise
     */
    public boolean hasRightChild() {
        return this.rightNode != null;
    }

    /**
     * Checks if the node has both, left and right, childs
     *
     * @return true If the node has a left child and a right child, false otherwise
     */
    public boolean hasBothChilds() {
        return hasLeftChild() && hasRightChild();
    }



    /**
     * Checks if the node is left child of his parent
     *
     * @return true If the node left child, false otherwise
     */
    public boolean isLeftChild() {
        return this.parent.leftNode.equals(this);
    }



    /**
     * Checks if the node is right child of his parent
     *
     * @return true If the node right child, false otherwise
     */
    public boolean isRightChild() {
        return this.parent.rightNode.equals(this);
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    @Override
    public String toString() {
        return "Key " + key + ", value " + value;
    }


}
