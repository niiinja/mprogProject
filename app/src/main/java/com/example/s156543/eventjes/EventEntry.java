package com.example.s156543.eventjes;

/**
 * Created by s156543 on 13-6-2018.
 */

public class EventEntry {

    String organizer;
    String title;
    String time;
    String date;
    String price;
    String type;
    String imgUrl;
    public EventEntry(String organizer, String title, String time, String date, String price,
                      String imgUrl, String type) {

        this.organizer = organizer;
        this.title = title;
        this.time = time;
        this.date = date;
        this.price = price;
        this.type = type;
        this.imgUrl = imgUrl;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
