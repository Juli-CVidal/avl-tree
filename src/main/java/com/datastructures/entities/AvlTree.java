package com.datastructures.entities;

import com.datastructures.exception.NodeNotFoundException;
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
        if (!node.hasParent()) {

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
                //System.out.println("Right rotate on node" + node);
                rightRotate(node);
            } else {
                //System.out.println("Left right rotate on node" + node);
                leftRightRotate(node);
            }
            return;
        }

        if (node.getBalanceFactor() < -1) { //The right subtree is taller than the left subtree
            AvlNode<K, V> rightNode = node.getRightNode();
            if (rightNode.getBalanceFactor() < 0) {
                //System.out.println("Left rotate on node" + node);
                leftRotate(node);
            } else {
                //System.out.println("Right left rotate on node" + node);
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
     *
     * @param current The current node.
     * @param value   The indicated value.
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
     *
     * @param value The indicated value.
     * @return K the key of the node that has the value, or `null` if the value is not found.
     */
    public K getKey(V value) {
        return this.root == null ? null : searchForKey(this.root, value);
    }


    /**
     * Recursively searches for the node that has the indicated key.
     *
     * @param current The current node.
     * @param key     The key of the node.
     * @return The node that has the key if it's found, null otherwise.
     */
    private AvlNode<K, V> searchForNode(AvlNode<K, V> current, K key) {
        if (current == null) {
            return null;
        }

        int comparison = current.getKey().compareTo(key);

        if (comparison == 0) return current;

        return comparison < 0 ? searchForNode(current.getRightNode(), key) : searchForNode(current.getLeftNode(), key);
    }

    /**
     * Searches and return the node that has the indicated key.
     *
     * @param key The key of the node.
     * @return The node that has the key if it's found, null otherwise.
     */
    public AvlNode<K, V> getNode(K key) {
        return key == null ? null : searchForNode(this.root, key);
    }

    /**
     * Updates the value of the node that has the indicated key.
     *
     * @param key   The key of the node.
     * @param value The new value of the node.
     * @throws com.datastructures.exception.NodeNotFoundException if the node was not found.
     */
    public void updateNodeValue(K key, V value) throws NodeNotFoundException {
        AvlNode<K, V> node = searchForNode(this.root, key);
        if (node == null) throw new NodeNotFoundException("There is no node with the key " + key);

        node.setValue(value);
    }


    /**
     * Searches for the greater node on a subtree (The node with the highest key)
     *
     * @param node The root of the subtree
     * @return The node if found, null otherwise.
     */
    private AvlNode<K, V> getGreaterNode(AvlNode<K, V> node) {
        if (null == node) return null;

        return !node.hasRightChild() ? node : getGreaterNode(node.getRightNode());
    }

    /**
     * Searches for the letter node on a subtree (The node with the lowest key)
     *
     * @param node The root of the subtree
     * @return The node if found, null otherwise.
     */
    private AvlNode<K, V> getLesserNode(AvlNode<K, V> node) {
        if (null == node) return null;

        return !node.hasLeftChild() ? node : getLesserNode(node.getLeftNode());
    }


    /**
     * Vinculates the new parent with the new child
     *
     * @param parent The parent.
     * @param child  The child.
     */
    private void vinculateNodes(AvlNode<K, V> parent, AvlNode<K, V> child) {
        if (parent == null || child == null) {
            return;
        }

        if (child.isRightChild()) {
            parent.setRightNode(child);
        } else {
            parent.setLeftNode(child);
        }

        child.setParent(parent);
    }


    /**
     * Deletes a node without childs
     * <p>In this case, verifies whether it is a left or right child, in order to unlink it from the parent.
     *
     * @param node The node to be deleted.
     */
    private void deleteNodeWithoutChilds(AvlNode<K, V> node) {
        AvlNode<K, V> parent = node.getParent();
        if (node.isLeftChild()) {
            parent.setLeftNode(null);
        } else {
            parent.setRightNode(null);
        }
        checkBalanceFactor(parent);
    }


    /**
     * Deletes a node with one child.
     * <p>In this case, verifies wheter it has a left or right child, in order to link it with the node's parent.
     *
     * @param node The node to be deleted.
     */
    private void deleteNodeWithOneChild(AvlNode<K, V> node) {
        AvlNode<K, V> parent = node.getParent();
        if (node.hasLeftChild()) {
            AvlNode<K, V> leftNode = node.getLeftNode();
            vinculateNodes(parent, leftNode);
            checkBalanceFactor(leftNode);
            return;
        }

        if (node.hasRightChild()) {
            AvlNode<K, V> rightNode = node.getRightNode();
            vinculateNodes(parent, rightNode);
            checkBalanceFactor(rightNode);
        }
    }


    /**
     * Looks for a successor of a node
     * <p>The successor of a node it's the node with the highest key of the left subtree or the lowest key of the right subtree
     *
     * @param node The node to be replaced
     * @return The successor of the node.
     */
    private AvlNode<K, V> findSuccessor(AvlNode<K, V> node) {
        AvlNode<K, V> successor = getLesserNode(node.getRightNode()), parent;

        if (successor == null) {
            successor = getGreaterNode(node.getLeftNode());
        }

        parent = successor.getParent();
        performDeletion(successor);
        checkBalanceFactor(successor.getParent());
        return successor;
    }

    /**
     * Deletes a node with both childs.
     * <p> Due to the rebalancing, the root will always contain both childs.
     * <p> In this case, the method replaces the node to be deleted with a successor,
     * can be the node with the highest key of the left subtree or the node with the lowest key of the right subtree.
     *
     * @param node The node to be deleted.
     */
    private void deleteNodeWithBothChilds(AvlNode<K, V> node) {
        AvlNode<K, V> successor = findSuccessor(node), leftNode = node.getLeftNode(), rightNode = node.getRightNode();
        successor.setParent(node.getParent());
        if (!successor.hasParent()) {
            this.root = successor;
        }

        vinculateNodes(successor, leftNode);
        vinculateNodes(successor, rightNode);


        checkBalanceFactor(leftNode);
        checkBalanceFactor(rightNode);
    }


    /**
     * Deleted the indicated node.
     *
     * @param node The node to be deleted.
     */
    private void performDeletion(AvlNode<K, V> node) {

        //If the node is the root of the tree or if it has both childs
        if (this.root == node || node.hasBothChilds()) {
            deleteNodeWithBothChilds(node);
            return;
        }

        //If the node only has one child
        if (node.hasLeftChild() || node.hasRightChild()) {

            deleteNodeWithOneChild(node);
            return;
        }

        //If the node doesn't have childs
        if (node.isLeaf()) {
            deleteNodeWithoutChilds(node);
        }
    }


    public void deleteNode(K key) throws NodeNotFoundException {
        AvlNode<K, V> node = searchForNode(this.root, key);
        if (node == null) throw new NodeNotFoundException("There is no node with the key " + key);
        performDeletion(node);
    }
}
