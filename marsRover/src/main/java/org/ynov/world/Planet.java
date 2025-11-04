package org.ynov.world;

import lombok.Getter;
import org.ynov.rover.Rover;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Planet {

    static Planet planet;
    private String name;
    private int width;
    private int height;
    private Element[][] elements;
    //private List<Element> elements;

    public Planet(String name, int size, int numberElements) {
        this.name = name;
        this.width = size;
        this.height = size;
        this.elements = new Element[width][height];
        generateObstacles(numberElements);
        planet = this;
    }

    public static Planet getPlanet() {
        if (planet == null) {
            planet = new Planet("Mars", 4, 3);
        }
        return planet;
    }

    public List<Element> findElements(TypeElement typeElement) {
        List<Element> results = new ArrayList<>();
        for(Element[] column : elements){
            for(Element element : column) {
                if (element.getType() == typeElement) {
                    results.add(element);
                }
            }
        }
        return results;
    }

    public void addElement(Element element){
        addElement(element, false);
    }

    public void addElement(Element element, boolean replaceIfExist){
        if(elements[element.getPosition().x][element.getPosition().y] == null || replaceIfExist)
            elements[element.getPosition().x][element.getPosition().y] = element;
    }

    public Element GetElement(Vector2 position){
        return elements[position.x][position.y];
    }

    public void addRover(Rover rover) {
        elements[rover.getPosition().x][rover.getPosition().y] = new Element(TypeElement.ROVER, rover.getPosition());
    }

    public void addObstacle(Element obstacle) {
        if(obstacle.getType() == TypeElement.OBSTACLE)
            addElement(obstacle);
    }

    private void generateObstacles(int numberObstacles) {
        for (int i = 0; i < numberObstacles; i++) {
            Element obstacle = new Element(TypeElement.OBSTACLE);
            obstacle.setPosition(addRandomData(obstacle.getPosition()));
            addElement(obstacle);
        }
    }

    private boolean obstacleAlreadyExists(int x, int y) {
        if(elements[x][y] == null) return false;
        return elements[x][y].getType() == TypeElement.OBSTACLE;
    }

    private boolean atZero(int x, int y) {
        return x == 0 && y == 0;
    }

    Vector2 addRandomData(Vector2 position) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                position.x = (int) (Math.random() * width);
                position.y = (int) (Math.random() * height);
            }
        }
        return position;
    }

    public List<Element> getObstacles(){
        List<Element> obstacles = new ArrayList<>();
        for(Element[] column : elements){
            for(Element element : column) {
                if (element.getType() == TypeElement.OBSTACLE) {
                    obstacles.add(element);
                }
            }
        }
        return obstacles;
    }
}