package com.example.al3ra8e.hucalendar.eventPackage;

import com.example.al3ra8e.hucalendar.R;
import com.example.al3ra8e.hucalendar.connection.AppController;
import com.github.sundeepk.compactcalendarview.domain.Event ;
import java.util.Date;


public class CustomEvent extends Event {
    private int listViewIndex;
    private String desc;
    private int id;
    private String coverPicture;
    private Date eventDate;

    public String getCoverPicture() {
        return coverPicture;
    }

    public void setCoverPicture(String coverPicture) {
        this.coverPicture = coverPicture;
    }

    public CustomEvent(Date date) {
        super(AppController.getInstance().getResources().getColor(R.color.colorPrimaryDark), date.getTime(), null);
        this.eventDate = date;
    }

    public CustomEvent(Date date, String desc, int listViewIndex) {
        super(AppController.getInstance().getResources().getColor(R.color.colorPrimaryDark), date.getTime(), null);
        this.listViewIndex = listViewIndex;
        this.desc = desc;
        this.eventDate = date;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        CustomEvent temp = (CustomEvent) o;

        return this.getTimeInMillis() == temp.getTimeInMillis();

    }

    @Override
    public int getColor() {

        return super.getColor();
    }

    @Override
    public String toString() {
        return "CustomEvent{" +
                "id=" + getId() +
                "   listViewIndex=" + listViewIndex +
                ", desc='" + desc + '\'' +
                '}';
    }
}
