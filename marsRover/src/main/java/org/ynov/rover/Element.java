package org.ynov.rover;

import lombok.Getter;
import lombok.Setter;
import org.ynov.shared.Vector2;

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
