package com.datastructures.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Node<T> {
    private T value;
    private Node<T> nextNode;


    public Node(T value) {
        this.value = value;
    }


    /**
     * @param value The value to be compared
     * @return True if the node contains the indicated value, otherwise false
     */
    public boolean hasValue(T value) {
        return this.value.equals(value);
    }


    @Override
    public String toString() {
        return "" + this.value;
    }
}