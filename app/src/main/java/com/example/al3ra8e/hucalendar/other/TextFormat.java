package com.example.al3ra8e.hucalendar.other;

/**
 * Created by al3ra8e on 8/9/2017.
 */

public abstract class TextFormat {

    public static String firstCharUpper(String text){
        return  (""+text.charAt(0)).toUpperCase()+text.substring(1 , text.length()) ;
    }


}
