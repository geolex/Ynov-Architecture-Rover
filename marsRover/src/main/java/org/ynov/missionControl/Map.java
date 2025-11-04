package org.ynov.missionControl;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class Map {

    private int[][] cells;

    public Map(int width, int height) {
        cells = new int[width][height];
        printMap( cells);
    }

    public static void printMap(int mat[][]) {
        for (int[] row : mat)
            System.out.println(Arrays.toString(row));
    }
}
