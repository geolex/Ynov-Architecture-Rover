package org.ynov.rover;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.ynov.world.Planet;
import org.ynov.world.TypeElement;

class PlanetTest {

    @Nested
    class constructorTest {
        @Test
        public void CreatesNewPlanetWhenNull() {
            //GIVEN
            Planet.planet = null;

            //WHEN
            Planet result = Planet.getPlanet();

            //THEN
            assert result != null;
            assert result.getName().equals("Mars");
            assert result.getSize() == 100;
        }

        @Test
        public void returnsExistingPlanet() {
            //GIVEN
            Planet expectedPlanet = new Planet("Earth", 50, 10);

            //WHEN
            Planet result = Planet.getPlanet();

            //THEN
            assert result == expectedPlanet;
            assert result.getName().equals("Earth");
            assert result.getSize() == 50;
        }

        @Test
        public void initializesPlanetOnFirstCall() {
            // GIVEN
            Planet.planet = null;

            // WHEN
            Planet firstInstance = Planet.getPlanet();

            // THEN
            assert firstInstance != null;
            assert firstInstance.getName().equals("Mars");
            assert firstInstance.getSize() == 100;
        }

        @Test
        public void returnsSameInstanceOnSubsequentCalls() {
            // GIVEN
            Planet firstInstance = Planet.getPlanet();

            // WHEN
            Planet secondInstance = Planet.getPlanet();

            // THEN
            assert firstInstance == secondInstance;
            assert secondInstance.getName().equals("Mars");
            assert secondInstance.getSize() == 100;
        }
    }

    @Nested
    class RoverInMap {
        @Test
        public void returnsNullIfNoRover() {
            // GIVEN
            Planet planet = new Planet("Mars", 100, 20);

            // WHEN
            Rover result = planet.getRover();

            // THEN
            assert result == null;
        }

        @Test
        public void returnsRoverIfExists() {
            // GIVEN
            Planet planet = new Planet("Mars", 100, 20);
            Rover rover = new Rover(OrientationEnum.North);
            planet.addRover(rover);

            // WHEN
            Rover result = planet.getRover();

            // THEN
            assert result == rover;
        }

        @Test
        public void ignoresNonRoverElements() {
            // GIVEN
            Planet planet = new Planet("Mars", 100, 20);
            Obstacle obstacle = new Obstacle();
            obstacle.setPosition(new Vector2(5, 5));
            planet.getElements().add(obstacle);

            // WHEN
            Rover result = planet.getRover();

            // THEN
            assert result == null;
        }
    }

    @Nested
    class ObstaclesTests {

        @Test
        public void returnsEmptyListIfNoObstacles() {
            // GIVEN
            Planet planet = new Planet("Mars", 100, 0);

            // WHEN
            List<Obstacle> result = planet.getObstacles();

            // THEN
            assert result.isEmpty();
        }

        @Test
        public void returnsOnlyObstacles() {
            // GIVEN
            Planet planet = new Planet("Mars", 100, 0);
            Obstacle obstacle = new Obstacle();
            obstacle.setPosition(new Vector2(3, 3));
            Rover rover = new Rover(OrientationEnum.North);
            planet.getElements().add(obstacle);
            planet.getElements().add(rover);

            // WHEN
            List<Obstacle> result = planet.getObstacles();

            // THEN
            assert result.size() == 1;
            assert result.contains(obstacle);
        }

        @Test
        public void returnsAllObstaclesPresent() {
            // GIVEN
            Planet planet = new Planet("Mars", 100, 0);
            Obstacle obstacle1 = new Obstacle();
            obstacle1.setPosition(new Vector2(1, 1));
            Obstacle obstacle2 = new Obstacle();
            obstacle2.setPosition(new Vector2(2, 2));
            planet.getElements().add(obstacle1);
            planet.getElements().add(obstacle2);

            // WHEN
            List<Obstacle> result = planet.getObstacles();

            // THEN
            assert result.size() == 2;
            assert result.contains(obstacle1);
            assert result.contains(obstacle2);
        }
    }

    @Nested
    class AddRoverTests {

        @Test
        public void addsRoverSuccessfully() {
            // GIVEN
            Planet planet = new Planet("Mars", 100, 0);
            Rover rover = new Rover(OrientationEnum.North);
            rover.getPosition().x = (5);
            rover.getPosition().y = (5);

            // WHEN
            planet.addRover(rover);

            // THEN
            assert planet.getRover() == rover;
        }

        @Test
        public void doesNotAddRoverIfObstacleExists() {
            // GIVEN
            Planet planet = new Planet("Mars", 100, 0);
            Obstacle obstacle = new Obstacle();
            obstacle.getPosition().x = 5;
            obstacle.getPosition().y = 5;
            planet.addObstacle(obstacle);

            Rover rover = new Rover(OrientationEnum.North);
            rover.getPosition().x =5;
            rover.getPosition().y = 5;

            // WHEN
            planet.addRover(rover);

            // THEN
            assert planet.getRover() == null;
        }
    }
}
