package com.example.al3ra8e.hucalendar.other;

import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.example.al3ra8e.hucalendar.connection.AppController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// class date format for calendar usage @_@

public class DateFormat {

    Date current;

    public DateFormat(long time) {
        current = new Date(time);
    }

    public DateFormat(Date date) {
        current = date;

    }

    public DateFormat(String date, String format) {
        try {
            current = new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getDate() {
        return current;
    }

    public int getDay() {

        String day = new SimpleDateFormat("dd").format(current);
        return Integer.parseInt(day);

    }


    public int getMonthAsNumber() {

        String month = new SimpleDateFormat("MM").format(current);
        return Integer.parseInt(month);

    }

    public String getMonthAndYear() {

        return new SimpleDateFormat("MMMM - yyyy").format(current);

    }


    public String getMonthAsWord() {

        return new SimpleDateFormat("MMMM").format(current);

    }

    public int getYear() {

        String year = new SimpleDateFormat("yyyy").format(current) ;
        return Integer.parseInt(year);

    }

    public int getHour() {

        String year = new SimpleDateFormat("HH").format(current) ;
        return Integer.parseInt(year);

    }

    public int getMinute() {

        String year = new SimpleDateFormat("mm").format(current) ;
        return Integer.parseInt(year);

    }





    public String getDefaultFormat(){
        return new SimpleDateFormat("yyyy-MM-dd").format(current) ;
    }

    public static Date makeDate(String format, String date) throws ParseException {

        return new SimpleDateFormat(format).parse(date);

    }

    public static Date makeDate(int day , int month , int year) throws ParseException {
        String MM = ((month+"").length()<2)? "0"+month : ""+month ;
        String date = year+"-"+MM+"-"+day ;
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);

    }


    public static String getDate(Long time){

       return new SimpleDateFormat("yyyy-MM-dd").format(new Date(time)) ;

    }
    public static String getDate(Date date){

        return new SimpleDateFormat("yyyy-MM-dd").format(date) ;

    }


    public String getTime() {

        return new SimpleDateFormat("hh:mm a").format(current);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static boolean checkOnDate(String date , String time){
        Calendar calendar = Calendar.getInstance();
        DateFormat selected = null;

        try {
            selected = new DateFormat(DateFormat.makeDate("yyyy-MM-dd/HH:mm" , date+"/"+time)) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat dateFormat = new DateFormat(calendar.getTimeInMillis()) ;

        if(selected.getDate().getTime() > dateFormat.getDate().getTime()){
            return true;
        }

        return false ;

    }





    @Override
    public String toString() {
        return new SimpleDateFormat("yyyy-MM-dd").format(current) ;
    }


}
