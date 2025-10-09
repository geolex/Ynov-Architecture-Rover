package org.ynov.rover;

import lombok.Getter;
import org.ynov.shared.Vector2;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Planet {

    private String name;
    private int size;
    private boolean complete;
    private int[][] cells;
    private int numberObstacle;
    private List<Obstacle> obstacles;

    public Planet(String name, int size, int numberObstacle) {
        this.name = name;
        this.size = size;
        this.complete = false;
        this.cells = new int[size][size];
        this.numberObstacle = numberObstacle;
        createObstacle();
    }

    private void createObstacle() {
        obstacles = new ArrayList<>();
        for (int i = 0; i < numberObstacle; i++) {
            var obstacle = new Obstacle();
            obstacle.setPosition(addRandomData(obstacle.getPosition()));
            if (obstacleAlreadyExists(obstacle.getPosition().x, obstacle.getPosition().y ))obstacles.add(obstacle);
        }
    }

    private boolean obstacleAlreadyExists(int x, int y){
        for(var obstacle : obstacles){
            if(obstacle.getPosition().x == x && obstacle.getPosition().y == y){
                return true;
            }
        }
        return false;
    }

    Vector2 addRandomData(Vector2 position){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                position.x = (int)(Math.random()*size);
                position.y = (int)(Math.random()*size);
            }
        }
        return position;
    }
}
