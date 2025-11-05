package org.ynov.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.ynov.world.Vector2;
import org.ynov.world.OrientationEnum;

import java.util.List;

public class Information {
    public Vector2 position;
    public boolean success;
    public OrientationEnum orientation;
    public List<Vector2> detectedObstacles; // positions d'obstacles découverts par le rover (nullable)

    public Information() {}

    public Information(Vector2 position, boolean success, OrientationEnum orientation) {
        this(position, success, orientation, null);
    }

    public Information(Vector2 position, boolean success, OrientationEnum orientation, List<Vector2> detectedObstacles) {
        this.position = position;
        this.success = success;
        this.orientation = orientation;
        this.detectedObstacles = detectedObstacles;
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String Encode(Information info) {
        try {
            return mapper.writeValueAsString(info);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Information Decode(String data) {
        try {
            return mapper.readValue(data, Information.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}