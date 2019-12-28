package com.example.al3ra8e.hucalendar.eventPackage;

import com.example.al3ra8e.hucalendar.studentPackage.Student;

@SuppressWarnings("serial") // with this annotation we hide the compiler warnings
public class Comment{
    private int commentId ;
    private String time;
    private String comment;
    private Student student;
    private int eventId ;

    public Comment() {
    }

    public Comment(String time, String comment, Student student) {
        this.time = time;
        this.comment = comment;
        this.student = student;
    }

    public int getEventId() {
        return eventId;
    }

    public Comment setEventId(int eventId) {
        this.eventId = eventId;
        return this ;
    }

    public String getTime() {
        return time;
    }

    public Comment setTime(String time) {
        this.time = time;
        return this ;
    }

    public int getCommentId() {
        return commentId;
    }

    public Comment setCommentId(int commentId) {
        this.commentId = commentId;
        return this ;
    }

    public Student getStudent() {
        return student;
    }

    public Comment setStudent(Student student) {
        this.student = student;
        return this ;
    }

    public String getComment() {
        return comment;
    }

    public Comment setComment(String comment) {
        this.comment = comment;
        return this ;
    }
}