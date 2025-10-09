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
    private int numberElements;
    private List<Element> elements;

    public Planet(String name, int size, int numberElements) {
        this.name = name;
        this.size = size;
        this.complete = false;
        this.numberElements = numberElements;
        createObstacle();

    }

    public void addRover(Rover rover) {
        if (rover != null && obstacleAlreadyExists(rover.getPosition().x, rover.getPosition().y)) {
            elements.add(rover);
        }
    }

    private void createObstacle() {
        elements = new ArrayList<>();
        for (int i = 0; i < numberElements; i++) {
            var obstacle = new Obstacle();
            obstacle.setPosition(addRandomData(obstacle.getPosition()));
            if (!obstacleAlreadyExists(obstacle.getPosition().x, obstacle.getPosition().y) && !atZero(obstacle.getPosition().x, obstacle.getPosition().y))
                elements.add(obstacle);
        }
    }

    private boolean obstacleAlreadyExists(int x, int y) {
        for (var obstacle : elements) {
            if (obstacle.getPosition().x == x && obstacle.getPosition().y == y) {
                return true;
            }
        }
        return false;
    }

    private boolean atZero(int x, int y) {
        return x == 0 && y == 0;
    }

    Vector2 addRandomData(Vector2 position) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                position.x = (int) (Math.random() * size);
                position.y = (int) (Math.random() * size);
            }
        }
        return position;
    }

    public List<Obstacle> getObstacles(){
        List<Obstacle> obstacles = new ArrayList<>();
        for(Element element : elements){
            if(element instanceof Obstacle){
                obstacles.add((Obstacle) element);
            }
        }
        return obstacles;
    }
}
