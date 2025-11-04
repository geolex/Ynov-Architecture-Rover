package org.ynov.world;

import lombok.NoArgsConstructor;
import org.ynov.communication.IntParser;
import java.util.Random;

import java.util.Random;

@NoArgsConstructor
public class Vector2 {
    public static Vector2 zero = new Vector2(0, 0);

    public int x;
    public int y;

    public Vector2(int a, int b){ this.x = a; this.y = b; }

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
}
