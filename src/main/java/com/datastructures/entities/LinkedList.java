package com.datastructures.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Array;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinkedList<T> {
    private Node<T> head;
    private int length = 0;

    public LinkedList(T[] list) {
        this.length = list.length;
        Node<T> currentNode = new Node<>(list[0]);
        this.head = currentNode;

        for (int index = 1; index < this.length; index++) {
            currentNode.setNextNode(new Node<>(list[index]));
            currentNode = currentNode.getNextNode();
        }
    }


    /**
     * Converts the LinkedList to a normal list
     *
     * @return T[] the list that contains all the elements of the LinkedList
     */
    public T[] toList() {
        T[] tempList = (T[]) Array.newInstance(head.getValue().getClass(), length);
        int index = 0;
        for (Node<T> currentNode = head; currentNode != null; currentNode = currentNode.getNextNode()) {
            tempList[index++] = currentNode.getValue();
        }
        return tempList;
    }


    /**
     * The value will be added at the beginning (it will be the new head)
     *
     * @param value The value to be added
     */
    public void add(T value) {
        Node<T> node = new Node<>(value);
        node.setNextNode(this.head);
        this.head = node;
        this.length++;
    }


    /**
     * @param value The value to be searched
     * @return The index of the first occurrence of the value.
     *
     * <p>If the value is not found, the method will return -1.
     */
    public int getIndex(T value) {
        int index = 0;
        Node<T> currentNode = this.head;
        while (currentNode != null) {
            if (currentNode.hasValue(value)) {
                return index;
            }
            index++;
            currentNode = currentNode.getNextNode();
        }
        return -1;
    }


    /**
     * @param targetIndex The index of the node to return
     * @return The node that is in the indicated index. If not found, will return null
     */
    public Node<T> getNodeAtIndex(int targetIndex) {
        if (!isValidIndex(targetIndex)) {
            throw new IllegalArgumentException("Invalid index: " + targetIndex +
                    ". Must be between 0 and the LinkedList lenght");
        }

        int index = 0;
        Node<T> currentNode = this.head;
        while (currentNode != null) {
            if (index == targetIndex) {
                return currentNode;
            }
            currentNode = currentNode.getNextNode();
            index++;
        }
        return null;
    }


    /**
     * @param value The value to be inserted
     * @param index The index where the value should be inserted
     */
    public void insert(T value, int index) {
        if (!isValidIndex(index)) {
            throw new IllegalArgumentException("Invalid index: " + index +
                    ". Must be between 0 and the LinkedList lenght");
        }

        if (index == 0) {
            add(value);
            return;
        }

        Node<T> previousNode = getNodeAtIndex(index - 1);
        previousNode.setNextNode(new Node<>(value));
        this.length++;
    }


    /**
     * This method will remove the first occurrence of the indicated value
     *
     * @param value The value to be deleted
     * @return true if the deletion has been possible, false otherwise.
     */
    public boolean deleteByValue(T value) {
        Node<T> currentNode = this.head;

        if (currentNode.getValue() == value) {
            this.head = currentNode.getNextNode();
            this.length--;
            return true;
        }
        Node<T> nextNode = currentNode.getNextNode();
        while (nextNode != null) {
            if (nextNode.hasValue(value)) {
                currentNode.setNextNode(nextNode.getNextNode());
                this.length--;
                return true;
            }

            currentNode = nextNode;
            nextNode = nextNode.getNextNode();
        }

        return false;
    }


    /**
     * This method will delete the node that is in the specified index.
     *
     * @param index The index where the node to be deleted is located
     * @return true if the deletion has been possible, false otherwise.
     */
    public boolean deleteByIndex(int index) {
        if (!isValidIndex(index)) {
            throw new IllegalArgumentException("Invalid index: " + index +
                    ". Must be between 0 and the LinkedList lenght");
        }

        if (index == 0) {
            this.head = this.head.getNextNode();
            this.length--;
            return true;
        }

        Node<T> prevNode = getNodeAtIndex(index - 1);
        prevNode.setNextNode(prevNode.getNextNode().getNextNode());
        this.length--;
        return true;
    }


    /**
     * @param value The value to be updated
     * @param index The index of the node
     * @return true if the update has been possible, false otherwise
     */
    public boolean updateNode(T value, int index) {
        if (!isValidIndex(index)) {
            throw new IllegalArgumentException("Invalid index: " + index +
                    ". Must be between 0 and the LinkedList lenght");
        }

        Node<T> node = getNodeAtIndex(index);
        node.setValue(value);
        return true;
    }


    /**
     * @param index The index to be evaluated
     * @return True if the index is between 0 and the length of the list, false otherwise
     */
    public boolean isValidIndex(int index) {
        return 0 <= index && index <= this.length;
    }


    @Override
    public String toString() {
        if (this.head == null) {
            return "[]";
        }
        StringBuilder str = new StringBuilder("[");
        Node<T> currentNode = this.head;

        while (currentNode != null) {
            str.append(currentNode.getValue());
            currentNode = currentNode.getNextNode();
            if (currentNode != null) {
                str.append(", ");
            }
        }
        str.append("]");
        return str.toString();
    }


    /**
     * This method verifies if two linkedList have the same elements in the same order
     *
     * @param otherList the list to be compared
     * @return true if both contains the same elements, false otherwise
     */
    public boolean equals(LinkedList<T> otherList) {
        if (this == otherList) {
            return true;
        }

        if (this.length != otherList.getLength()) {
            return false;
        }

        Node<T> thisNode = this.head;
        Node<T> otherNode = otherList.getHead();

        while (thisNode != null && otherNode != null) {
            if (!thisNode.hasValue(otherNode.getValue())) {
                return false;
            }
            thisNode = thisNode.getNextNode();
            otherNode = otherNode.getNextNode();
        }

        return true;
    }

}