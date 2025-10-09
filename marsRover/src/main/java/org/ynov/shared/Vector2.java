package org.ynov.shared;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Vector2 {
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
}
