package org.ynov.missionControl;

import lombok.Getter;
import org.ynov.world.Obstacle;
import org.ynov.world.Planet;
import org.ynov.rover.Rover;

import java.util.Arrays;
import java.util.List;

@Getter
public class Map {

    private int[][] cells;

    public Map(Planet planet) {
        cells = new int[planet.getSize()][planet.getSize()];
        initMap(planet);
        printMap( cells);
    }

    public void initMap(Planet planet) {
        for (int i = 0; i < planet.getSize(); i++)
            for (int j = 0; j < planet.getSize(); j++)
                if (roverIsHere(planet.getRover(), i, j))
                    cells[i][j] = 1;
                else if (obstacleIsHere(i, j, planet))  cells[i][j] = 2;
                else
                    cells[i][j] = 0;
    }

    public static void printMap(int mat[][]) {
        for (int[] row : mat)
            System.out.println(Arrays.toString(row));
    }

    public boolean roverIsHere(Rover rover, int i, int j) {
        return rover.getPosition().x == i && rover.getPosition().y == j;
    }

    public boolean obstacleIsHere(List<Obstacle> obstacles, int i, int j) {
        for (Obstacle obstacle : obstacles)
            if (obstacle.getPosition().x == i && obstacle.getPosition().y == j)
                return true;

        return false;
    }

    public boolean obstacleIsHere(int i, int j, Planet planet) {
        for (Obstacle obstacle : planet.getObstacles())
            if (obstacle.getPosition().x == i && obstacle.getPosition().y == j)
                return true;
        return false;
    }

}
