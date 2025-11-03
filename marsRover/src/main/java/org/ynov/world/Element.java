package org.ynov.world;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Element {
    private Vector2 position;
    private TypeElement type;

    public Element(TypeElement typeElement) {
        this.type = typeElement;
        this.position = new Vector2();
    }
}
