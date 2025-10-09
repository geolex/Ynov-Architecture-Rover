package org.ynov.rover;

import lombok.Getter;
import lombok.Setter;
import org.ynov.shared.Vector2;

@Getter
@Setter
public class Obstacle {
    private Vector2 position;
    private TypeObstacle type;

    public Obstacle() {
        this.position = new Vector2();
        this.type = TypeObstacle.UNKNOW;
    }
}
