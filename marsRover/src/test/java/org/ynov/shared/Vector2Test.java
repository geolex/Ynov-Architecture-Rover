package org.ynov.shared;

import org.junit.jupiter.api.Test;
import org.ynov.world.Vector2;

import static org.junit.jupiter.api.Assertions.*;

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

}
