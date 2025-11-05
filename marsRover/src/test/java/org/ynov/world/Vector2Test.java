package org.ynov.world;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Vector2Test {

    @Test
    void fromString() {
        Vector2 vector2 = Vector2.fromString("(-1,1)");
        assertEquals(-1, vector2.x);
        assertEquals(1, vector2.y);
    }

    @Test
    void toStringTest(){
        Vector2 vector2 = new Vector2(-1,1);
        assertEquals(vector2.toString(),"(-1,1)");
    }

    @Test
    void Random(){
        randomWithinBounds();
        randomWithinIntegerBounds();
    }

    void randomWithinBounds() {
        Vector2 min = new Vector2(0, 0);
        Vector2 max = new Vector2(10, 10);
        for (int i = 0; i < 100; i++) {
            Vector2 random = Vector2.Random(min, max);
            assertTrue(random.x >= min.x && random.x < max.x, "X should be within bounds");
            assertTrue(random.y >= min.y && random.y < max.y, "Y should be within bounds");
        }
    }

    void randomWithinIntegerBounds() {
        int minX = 0, maxX = 10, minY = -5, maxY = 5;
        for (int i = 0; i < 100; i++) {
            Vector2 random = Vector2.Random(minX, maxX, minY, maxY);
            assertTrue(random.x >= minX && random.x < maxX, "X should be within integer bounds");
            assertTrue(random.y >= minY && random.y < maxY, "Y should be within integer bounds");
        }
    }

}
