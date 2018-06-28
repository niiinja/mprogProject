package com.example.s156543.eventjes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class provides all the functionality of the SQL databases
 * for both the website and the event tables.
 */

public class EventDatabase extends SQLiteOpenHelper {
    private static EventDatabase instance;

    private EventDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // Creates a table for event entries and a table for website entries
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        SQLiteDatabase db = sqLiteDatabase;
        String query = "CREATE TABLE events (_id INTEGER PRIMARY KEY, title STRING, organizer STRING," +
                " date STRING, time STRING, datetime DATETIME, imgurl STRING, eventurl STRING," +
                "description STRING, saved BIT);";
        db.execSQL(query);
        query = "CREATE TABLE websites (_id INTEGER PRIMARY KEY, url STRING);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        SQLiteDatabase db = sqLiteDatabase;
        String drop = "DROP DATABASE IF EXISTS events";
        db.execSQL(drop);
        drop = "DROP DATABASE IF EXISTS websites";
        db.execSQL(drop);
        onCreate(db);
    }

    public  static EventDatabase getInstance(Context context){
        if(instance == null){
            instance = new EventDatabase(context, null, null, 1);
        }
        return instance;
    }

    // Insert new website entry in the website table
    public void insertWebsite(WebsiteEntry websiteEntry){
        SQLiteDatabase entrydb =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("url", websiteEntry.url);

        entrydb.insert("websites", null, contentValues);
    }

    // Insert new event entry in the event table
    public void insertEvent(EventEntry eventEntry){
        SQLiteDatabase entrydb =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String dateString;

        // In case of an event with dateTime = "null",
        // assigning it 2018-12-31 will place it at the end of the calendar
        if(eventEntry.dateTime != null) dateString = eventEntry.dateTime.toString();
        else dateString = "2018-12-31 00:00:00";

        contentValues.put("title", eventEntry.title);
        contentValues.put("organizer", eventEntry.organizer);
        contentValues.put("date", eventEntry.date);
        contentValues.put("time", eventEntry.time);
        contentValues.put("datetime", dateString);
        contentValues.put("imgurl", eventEntry.imgUrl);
        contentValues.put("eventurl", eventEntry.eventUrl);
        contentValues.put("description", eventEntry.description);

        entrydb.insert("events", null, contentValues);
    }

    // Selects all websites in he database
    public Cursor selectAllWebsites(){
        SQLiteDatabase selectAlldb = this.getWritableDatabase();
        Cursor cursor = selectAlldb.rawQuery("SELECT * FROM websites", null);
        return cursor;
    }

    // Selects all events in the database
    public Cursor selectAllEvents(){
        SQLiteDatabase selectAlldb = this.getWritableDatabase();
        Cursor cursor = selectAlldb.rawQuery("SELECT * FROM events ORDER BY datetime ASC", null);
        return cursor;
    }

    // Selects all saved events in the database
    public Cursor selectSavedEvents(){
        SQLiteDatabase selectAlldb = this.getWritableDatabase();
        Cursor cursor = selectAlldb.rawQuery("SELECT * FROM events WHERE saved = '1'", null);
        return cursor;
    }

    // Updates an event's description
    public void updateDescription(String d, long id){
        SQLiteDatabase entrydb =  this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("description", d);
        entrydb.update("events", cv,"_id =" + id, null);
    }

    // Removes a website and all its events from the database
    public void deleteWebsite(String title){
        SQLiteDatabase entrydb =  this.getWritableDatabase();
        String organizer = title;

        // Determines the organizer name to filter events on that to delete them
        int index = organizer.indexOf(".");
        if (index > 0)
            organizer = organizer.substring(0, index);

        int i = organizer.indexOf("/");
        if (i > 0)
            organizer = organizer.substring(i + 2, organizer.length());

        entrydb.delete("websites", "url = ?", new String[]{title});
        entrydb.delete("events", "organizer = ?", new String[]{organizer});
    }

    // Marks an event as "saved"
    public void saveEvent(long id, int bool){
        SQLiteDatabase entrydb =  this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("saved", bool);
        entrydb.update("events", cv, "_id =" + id, null);
    }
}
