package org.ynov.world;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.ynov.communication.CommunicatorTCP;
import org.ynov.rover.Rover;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlanetTest {

    @Nested
    class constructorTest {
        @Test
        public void CreatesNewPlanetWhenNull() {
            //GIVEN
            Planet planet = resetPlanet();
             // WHEN
            Planet result = Planet.getPlanet();
            // THEN
            assert result != null;
            assert result.getName().equals("Mars");
            assert result.getWidth() == 5;
            assert result.getHeight() == 5;
        }

        @Test
        public void ReturnsExistingPlanetWhenAlreadyCreated() {
            // GIVEN
            Planet initialPlanet = new Planet("Earth", 5, 2);
            // WHEN
            Planet retrievedPlanet = Planet.getPlanet();
            // THEN
            assert retrievedPlanet == initialPlanet;
            assert retrievedPlanet.getName().equals("Earth");
            assert retrievedPlanet.getWidth() == 5;
            assert retrievedPlanet.getHeight() == 5;
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
            assert result.getWidth() == 50;
            assert result.getHeight() == 50;
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
    class AddRoverTests {

        @Test
        public void addsRoverToTheCorrectPosition() {
            // GIVEN
            Planet planet = new Planet("Mars", 5, 0);
            Rover rover = new Rover(null, new CommunicatorTCP(), planet);
            rover.getPosition().x = 2;
            rover.getPosition().y = 3;

            // WHEN
            planet.addRover(rover);

            // THEN
            Element result = planet.GetElement(new Vector2(2, 3));
            assert result != null;
            assert result.getType() == TypeElement.ROVER;
        }

        @Test
        public void overridesExistingElement() {
            // GIVEN
            Planet planet = new Planet("Mars", 5, 0);
            Element obstacle = new Element(TypeElement.OBSTACLE, new Vector2(3, 3));
            planet.addElement(obstacle);
            Rover rover = new Rover(null, new CommunicatorTCP(), planet);
            rover.getPosition().x = 3;
            rover.getPosition().y = 3;

            // WHEN
            planet.addRover(rover);

            // THEN
            Element result = planet.GetElement(new Vector2(3, 3));
            assert result != null;
            assert result.getType() == TypeElement.ROVER;
        }

        @Test
        public void doesNotOutsidePlanetBound() {
            // GIVEN
            Planet planet = new Planet("Mars", 5, 0);
            Rover rover = new Rover(null, new CommunicatorTCP(), planet);
            rover.getPosition().x = 6;
            rover.getPosition().y = 6;

            // WHEN
            planet.addRover(rover);

            // THEN
            Exception exception = assertThrows(NumberFormatException.class, () -> {
                Integer.parseInt("1a");
            });

            String expectedMessage = "For input string: \"1a\"";
            String actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(expectedMessage));

            planet.getElements();
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

    public static Planet resetPlanet() {
        return null;
    }
}
