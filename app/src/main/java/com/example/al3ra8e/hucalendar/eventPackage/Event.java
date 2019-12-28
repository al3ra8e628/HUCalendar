package com.example.al3ra8e.hucalendar.eventPackage;


import com.example.al3ra8e.hucalendar.R;
import com.example.al3ra8e.hucalendar.connection.AppController;
import com.example.al3ra8e.hucalendar.other.DateFormat;

import java.text.ParseException;
import java.util.Date;

public class Event {

    private int eventId ;
    private String eventTitle ;
    private String eventImage ;
    private Date eventDate ;
    private String faculty ;
    private String category ;

    public Event() {
    }

    public Event(int eventId, String eventTitle, String eventImage, Date eventDate, String faculty, String category) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventImage = eventImage;
        this.eventDate = eventDate;
        this.faculty = faculty;
        this.category = category;
    }

    public int getEventId() {
        return eventId;
    }

    public Event setEventId(int eventId) {
        this.eventId = eventId;
        return this ;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public Event setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
        return this ;
    }

    public String getEventImage() {
        return eventImage;
    }

    public Event setEventImage(String eventImage) {
        this.eventImage = eventImage;
        return this ;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public Event setEventDate(Date eventDate) {
        this.eventDate = eventDate;
        return this ;
    }
    public Event setEventDate(String eventDate){
        try {
            this.eventDate = DateFormat.makeDate("yyyy-mm-dd" , eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return this ;
    }

    public String getFaculty() {
        return faculty;
    }

    public Event setFaculty(int facultyId) {
        this.faculty = (AppController.getInstance().getResources().getStringArray(R.array.faculties))[facultyId];
        return this ;
    }

    public Event setFaculty(String faculty) {
        this.faculty = faculty;
        return this ;
    }

    public String getCategory() {
        return category;
    }

    public Event setCategory(int categoryId) {
        this.category = (AppController.getInstance().getResources().getStringArray(R.array.categories))[categoryId];
        return this ;
    }


    public Event setCategory(String category) {
        this.category = category;
        return this ;
    }


    public Event build(){
        return this ;
    }


    public String getInfoToNotification(){
           String info = "" +
                   "Title :  "+eventTitle+"\n" +
                   "Date :  "+new DateFormat(eventDate).getDefaultFormat()+"\n"+
                   "Faculty :  "+faculty+"\n" +
                   "Category :  "+category+"\n" ;
        return info ;
    }


}
