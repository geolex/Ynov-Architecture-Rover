package org.ynov.world;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PlanetTest {

    @Nested
    class constructorTest {
        @Test
        public void CreatesNewPlanetWhenNull() {
            // GIVEN
            Planet.planet = null;

            // WHEN
            Planet planet = Planet.getPlanet();

            // THEN
            assert planet != null;
            assert planet.getName().equals("Mars");
            assert planet.getSize().x == 5;
            assert planet.getSize().y == 5;
        }

        @Test
        public void ReturnsExistingPlanetWhenAlreadyCreated() {
            // GIVEN
            Planet initialPlanet = new Planet("Earth", 5, 2);
            // WHEN
            Planet planet = Planet.getPlanet();
            // THEN
            assert planet == initialPlanet;
            assert planet.getName().equals("Earth");
            Vector2 size = planet.getSize();
            assert size.x == 5;
            assert size.y == 5;
        }

        @Test
        public void ReturnsExistingPlanetWhenPreviouslyCreated() {
            // GIVEN
            Planet initialPlanet = new Planet("Earth", 6, 2);

            // WHEN
            Planet planet = Planet.getPlanet();

            // THEN
            assert planet != null;
            assert planet == initialPlanet;
            assert planet.getName().equals("Earth");
            assert planet.getSize().x == 6;
            assert planet.getSize().y == 6;
        }
    }

    @Nested
    class ObstaclesTests {

        @Test
        public void returnsAllElementsOfAType() {
            // GIVEN
            Planet planet = new Planet("TestPlanet", 5, 0);
            Element obstacle = new Element(TypeElement.OBSTACLE, new Vector2(1, 1));
            Element rover = new Element(TypeElement.ROVER, new Vector2(2, 2));
            planet.addElement(obstacle);
            planet.addElement(rover);

            // WHEN
            var obstacles = planet.findElements(TypeElement.OBSTACLE);

            // THEN
            assert obstacles.size() == 1;
            assert obstacles.get(0) == obstacle;
        }

        @Test
        public void returnsEmptyListIfNoElementsOfTypeExist() {
            // GIVEN
            Planet planet = new Planet("TestPlanet", 5, 0);

            // WHEN
            var rovers = planet.findElements(TypeElement.ROVER);

            // THEN
            assert rovers.isEmpty();
        }

        @Test
        public void doesNotReturnElementsOfOtherTypes() {
            // GIVEN
            Planet planet = new Planet("TestPlanet", 5, 0);
            Element obstacle = new Element(TypeElement.OBSTACLE, new Vector2(3, 3));
            planet.addElement(obstacle);

            // WHEN
            var rovers = planet.findElements(TypeElement.ROVER);

            // THEN
            assert rovers.isEmpty();
        }

        @Test
        public void getObstaclesReturnsAllObstacles() {
            // GIVEN
            Planet planet = new Planet("TestPlanet", 5, 0);
            Element obstacle1 = new Element(TypeElement.OBSTACLE, new Vector2(0, 0));
            Element obstacle2 = new Element(TypeElement.OBSTACLE, new Vector2(1, 1));
            planet.addElement(obstacle1);
            planet.addElement(obstacle2);

            // WHEN
            var obstacles = planet.getObstacles();

            // THEN
            assert obstacles.size() == 2;
            assert obstacles.contains(obstacle1);
            assert obstacles.contains(obstacle2);
        }

        @Test
        public void getAndReturnsEmptyListWhenNoObstacles() {
            // GIVEN
            Planet planet = new Planet("EmptyPlanet", 3, 0);

            // WHEN
            var obstacles = planet.getObstacles();

            // THEN
            assert obstacles.isEmpty();
        }
    }

    @Nested
    class GetElementTests {

        @Test
        public void returnsElementAtSpecifiedPosition() {
            // GIVEN
            Planet planet = new Planet("TestPlanet", 5, 0);
            Vector2 position = new Vector2(2, 3);
            Element element = new Element(TypeElement.OBSTACLE, position);
            planet.addElement(element);

            // WHEN
            Element result = planet.GetElement(position);

            // THEN
            assert result != null;
            assert result == element;
        }

        @Test
        public void returnsNullForEmptyPosition() {
            // GIVEN
            Planet planet = new Planet("TestPlanet", 5, 0);
            Vector2 position = new Vector2(0, 1);

            // WHEN
            Element result = planet.GetElement(position);

            // THEN
            assert result == null;
        }
    }
}
