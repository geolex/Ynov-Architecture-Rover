package org.ynov.world;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Planet {

    static Planet planet;
    private String name;
    private Vector2 size;
    private Element[][] elements;

    public Planet(String name, int size, int numberElements) {
        this(name, size, generateObstacles(numberElements, new Vector2 (size, size)));
    }

    public Planet(String name, int size, List<Vector2> obstacles) {
        this.name = name;
        this.size = new Vector2(size, size);
        this.elements = new Element[this.size.x][this.size.y];

        for(Vector2 obstacle : obstacles) {
            elements[obstacle.x][obstacle.y] = new Element(TypeElement.OBSTACLE, obstacle);
        }

        planet = this;
    }

    public static Planet getPlanet() {
        if (planet == null) {
            planet = new Planet("Mars", 5, 3);
        }
        return planet;
    }

    public List<Element> findElements(TypeElement typeElement) {
        List<Element> results = new ArrayList<>();
        for(Element[] column : elements){
            for(Element element : column) {
                if(element == null) continue;
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

    public void addObstacle(Element obstacle) {
        if(obstacle.getType() == TypeElement.OBSTACLE)
            addElement(obstacle);
    }

    private static List<Vector2> generateObstacles(int numberObstacles, Vector2 size) {
        List<Vector2> obstacles = new ArrayList<Vector2>();
        for (int i = 0; i < numberObstacles; i++) {
            obstacles.add(Vector2.Random(Vector2.ZERO, size));
        }
        return obstacles;
    }

    private boolean obstacleAlreadyExists(int x, int y) {
        if(elements[x][y] == null) return false;
        return elements[x][y].getType() == TypeElement.OBSTACLE;
    }

    private boolean atZero(int x, int y) {
        return x == 0 && y == 0;
    }

    Vector2 addRandomData(Vector2 position) {
        for (int i = 0; i < size.x; i++) {
            for (int j = 0; j < size.y; j++) {
                position.x = (int) (Math.random() * size.x);
                position.y = (int) (Math.random() * size.y);
            }
        }
        return position;
    }

    public List<Element> getObstacles(){
        List<Element> obstacles = new ArrayList<>();
        for(Element[] column : elements){
            for(Element element : column) {
                if(element == null) continue;
                if (element.getType() == TypeElement.OBSTACLE) {
                    obstacles.add(element);
                }
            }
        }
        return obstacles;
    }
}
