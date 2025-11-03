package org.ynov.world;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Obstacle extends Element {
    public Obstacle(){
        super(TypeElement.OBSTACLE);
    }


}
