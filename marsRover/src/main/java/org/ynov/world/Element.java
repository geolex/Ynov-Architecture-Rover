package org.ynov.world;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Element {
    private Vector2 position;
    private TypeElement type;

    public Element(TypeElement type){
        this(type, Vector2.zero);
    }

    public Element(TypeElement typeElement, Vector2 position) {
        this.type = typeElement;
        this.position = position;
    }
}
