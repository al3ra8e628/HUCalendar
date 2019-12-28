package com.example.al3ra8e.hucalendar.servicesPackage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.al3ra8e.hucalendar.eventPackage.Comment;
import com.example.al3ra8e.hucalendar.eventPackage.Event;
import com.example.al3ra8e.hucalendar.other.Keys;
import com.example.al3ra8e.hucalendar.studentPackage.Student;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();

        if (intent.getAction().equalsIgnoreCase("com.example.Broadcast")) {
            if (bundle.getInt(Keys.SERVICES_STATE_KEY) == Keys.NEW_COMMENT_SERVICE) {
                CommentNotificationBuilder notification = new CommentNotificationBuilder();

                Student student = new Student()
                        .setPersonId(bundle.getInt("person_id"))
                        .setImage(bundle.getString("profile_picture"))
                        .setFirstName(bundle.getString("first_name"))
                        .setLastName(bundle.getString("last_name"));

                Comment comment = new Comment()
                        .setComment(bundle.getString("msg"))
                        .setEventId(bundle.getInt("event_id"))
                        .setCommentId(bundle.getInt("noteNum"))
                        .setStudent(student);

                notification.notify(context, comment);

            } else if (bundle.getInt(Keys.SERVICES_STATE_KEY) == Keys.NEW_EVENT_SERVICE) {
                EventNotificationBuilder notification = new EventNotificationBuilder();
                Event event = new Event()
                        .setEventId(bundle.getInt("event_id"))
                        .setEventDate(bundle.getString("event_date"))
                        .setEventTitle(bundle.getString("event_title"))
                        .setFaculty(bundle.getString("event_faculty"))
                        .setCategory(bundle.getString("event_category"))
                        .setEventImage(bundle.getString("event_image"))
                        .build();

                Long personId = bundle.getLong("person_id") ;
                notification.notify(context , event ,  personId);

            } else if (bundle.getInt(Keys.SERVICES_STATE_KEY) == Keys.EVENT_REQUEST_SERVICE) {

                EventRequestNotificationBuilder notification = new EventRequestNotificationBuilder();
                Event event = new Event()
                        .setEventId(bundle.getInt("event_id"))
                        .setEventDate(bundle.getString("event_date"))
                        .setEventTitle(bundle.getString("event_title"))
                        .setFaculty(bundle.getString("event_faculty"))
                        .setCategory(bundle.getString("event_category"))
                        .setEventImage(bundle.getString("event_image"))
                        .build();

                int personId = bundle.getInt("person_id") ;
                notification.notify(context , event ,  personId);

            }
        }
    }
}