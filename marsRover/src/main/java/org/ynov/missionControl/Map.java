package org.ynov.missionControl;

import lombok.Getter;
import org.ynov.world.TypeElement;
import org.ynov.world.Vector2;

import java.util.*;

@Getter
public class Map {

    private TypeElement[][] cells;

    public Map(int width, int height) {
        cells = new TypeElement[width][height];
    }

    public static void printMap(int mat[][]) {
        for (int[] row : mat)
            System.out.println(Arrays.toString(row));
    }

    public void setCell(Vector2 position, TypeElement typeElement){
        cells[position.x][position.y] = typeElement;
    }

    public void printMapAscii(){
        boolean isFirstColumn;

        String result = "";
        //result += asciiLine(cells.length-1) + "\n";

        for(TypeElement[] column : cells){
            isFirstColumn = true;
            for(TypeElement element : column){
                if(isFirstColumn){
                    //result += '|';
                    //isFirstColumn = false;
                }
                result += switch (element) {
                    case OBSTACLE -> '#';
                    case ROVER -> '@';
                    case EMPTY -> ' ';
                    case null -> '?';
                };
                //result += '|';
            }
            result += "\n";// + asciiLine(cells.length-1) + "\n";
        }
        System.out.println(result);
    }

    public String asciiLine(int nbElements){
        return "_" + "__".repeat(Math.max(0, nbElements));
    }
}
