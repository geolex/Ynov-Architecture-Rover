package org.ynov.world;

import lombok.NoArgsConstructor;
import org.ynov.communication.IntParser;
import java.util.Random;

@NoArgsConstructor
public class Vector2 {
    public static final Vector2 ZERO = new Vector2(0, 0);
    public static final Vector2 UP = new Vector2(0, 1);
    public static final Vector2 RIGHT = new Vector2(1, 0);
    public static final Vector2 DOWN = new Vector2(0, -1);
    public static final Vector2 LEFT = new Vector2(-1, 0);

    public int x;
    public int y;

    public Vector2(int a, int b){ this.x = a; this.y = b; }
    public Vector2(Vector2 v){ this.x = v.x; this.y = v.y; }

    public String toString(){
        return "("+x+","+y+")";
    }

    public static Vector2 fromString(String data){
        data = data.substring(1, data.length()-1);
        String[] results = data.split(",");

        Integer a = IntParser.TryIntParse(results[0]);
        Integer b = IntParser.TryIntParse(results[1]);

        if(a==null || b==null){return null;}
        return new Vector2(a,b);
    }

    public static Vector2 Random(Vector2 min, Vector2 max){
        return Vector2.Random(min.x, max.x ,min.y , max.y);
    }

    public static Vector2 Random(int minX, int maxX, int minY, int maxY){
        Random rand = new Random();
        int x = rand.nextInt(minX, maxX);
        int y = rand.nextInt(minY, maxY);
        return new Vector2(x, y);
    }

    public Vector2 add(Vector2 vector){
        this.x += vector.x;
        this.y += vector.y;
        return this;
    }

    public Vector2 modulo(int maxX, int maxY){
        this.x = (this.x + maxX) % maxX;
        this.y = (this.y + maxY) % maxY;
        return this;
    }

    public static Vector2 add(Vector2 vector1, Vector2 vector2){
        Vector2 results = new Vector2(vector1);
        return results.add(vector2);
    }
}
