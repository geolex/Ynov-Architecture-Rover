package org.ynov.shared;

public abstract class IntParser {

    public static Integer TryIntParse(String data){
        try {
            return Integer.parseInt(data);
        }catch(NumberFormatException e){
            return null;
        }
    }
}
