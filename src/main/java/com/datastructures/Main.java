package com.datastructures;

import com.datastructures.entities.AvlTree;

public class Main {
    public static void main(String[] args) {
        AvlTree<Integer, String> tree = new AvlTree<>();

        Integer[] keys = new Integer[]{60, 41, 74, 16, 53, 65, 25, 46, 55, 63, 42, 62, 64};

        String[] values = new String[]{"h", "c", "n", "a", "f", "l", "b", "e", "g", "j", "d", "i", "k"};

        for (int index = 0; index < keys.length; index++) {
            tree.add(keys[index], values[index]);
        }

    }
}
