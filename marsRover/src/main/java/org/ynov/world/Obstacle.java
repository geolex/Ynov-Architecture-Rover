package org.ynov.world;

import lombok.Getter;
import lombok.Setter;
import org.ynov.world.Vector2;

@Getter
@Setter
public class Obstacle extends Element {
    public Obstacle(){
        super(TypeElement.OBSTACLE);
    }

    public Obstacle(Vector2 position) {
        super(TypeElement.OBSTACLE);
    }


}
