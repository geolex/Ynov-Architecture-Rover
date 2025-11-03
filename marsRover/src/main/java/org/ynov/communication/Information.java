package org.ynov.communication;

import org.ynov.world.OrientationEnum;
import org.ynov.world.Vector2;

public class Information {
    public final Vector2 position;
    public final boolean success;
    public final OrientationEnum orientation;

    public Information(Vector2 position, boolean success, OrientationEnum orientation) {
        this.position = position;
        this.success = success;
        this.orientation = orientation;
    }

    public static String Encode(Information information){
      return "" + information.position + "-" +  information.success + "-" + information.orientation;
    };

    public static Information Decode(String data){
        String[] results = data.split("-");

        Vector2 position = Vector2.fromString(results[0]);
        Boolean success = Boolean.parseBoolean(results[1]);

        return new Information(position, success, OrientationEnum.valueOf(results[2]));
    }
}
