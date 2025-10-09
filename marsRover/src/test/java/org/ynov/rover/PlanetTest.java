package org.ynov.rover;

import org.junit.jupiter.api.Test;

class PlanetTest {

    @Test
    public void testConstructor() {
        Planet planet = new Planet("Mars", 10, 5);
        assert planet.getName().equals("Mars");
        assert planet.getObstacles().size() == 5;
        assert planet.getObstacles().get(0).getType() == TypeElement.OBSTACLE;
    }
}
