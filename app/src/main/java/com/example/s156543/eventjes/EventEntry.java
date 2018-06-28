package com.example.s156543.eventjes;

/**
 * Class for the event entries
 */

public class EventEntry {

    String organizer;
    String title;
    String time;
    String date;
    java.sql.Date dateTime;
    String imgUrl;
    String eventUrl;
    String description;
    boolean saved;

    public EventEntry(String organizer, String title, String time, String date,
                      java.sql.Date dateTime, String imgUrl, String eventUrl,
                      String description, boolean saved) {

        this.organizer = organizer;
        this.title = title;
        this.time = time;
        this.date = date;
        this.dateTime = dateTime;
        this.imgUrl = imgUrl;
        this.eventUrl = eventUrl;
        this.description = description;
        this.saved = saved;
    }
}
