//java
package org.ynov.missionControl;

import org.ynov.world.TypeElement;
import org.ynov.world.Vector2;

public class Map {
    private final TypeElement[][] cells; // cells[row][col] => cells[y][x]
    private final int width;
    private final int height;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new TypeElement[height][width]; // height = nb de lignes (y), width = nb de colonnes (x)
    }

    public void setCell(Vector2 pos, TypeElement element) {
        if (pos == null) return;
        int x = pos.x;
        int y = pos.y;
        if (x < 0 || x >= width || y < 0 || y >= height) return;
        cells[y][x] = element; // indexer par [y][x]
    }

    public TypeElement getCell(Vector2 pos) {
        if (pos == null) return null;
        int x = pos.x;
        int y = pos.y;
        if (x < 0 || x >= width || y < 0 || y >= height) return null;
        return cells[y][x];
    }

    public void printMapAscii() {
        for (int row = 0; row < height; row++) {
            StringBuilder sb = new StringBuilder();
            for (int col = 0; col < width; col++) {
                TypeElement e = cells[row][col];
                if (e == null) {
                    sb.append('?');
                } else {
                    switch (e) {
                        case EMPTY -> sb.append(' ');
                        case ROVER -> sb.append('@');
                        case OBSTACLE -> sb.append('#');
                        default -> sb.append('?');
                    }
                }
            }
            System.out.println(sb.toString());
        }
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}