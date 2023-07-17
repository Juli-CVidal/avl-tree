package com.datastructures;

import com.datastructures.entities.AvlTree;

public class Main {
    public static void main(String[] args) {
        AvlTree<Integer, Integer> tree = new AvlTree<>();

        Integer[] keys = new Integer[]{43, 18, 22, 9, 21, 6, 8, 20, 63, 50, 62, 51};

        //String[] values = new String[]{"h", "c", "n", "a", "f", "l", "b", "e", "g", "j", "d", "i", "k"};

        for (Integer key : keys) {
            tree.add(key, key);
        }

        tree.deleteNode(tree.getRoot().getKey());

    }
}
