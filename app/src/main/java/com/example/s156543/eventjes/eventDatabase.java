package com.example.s156543.eventjes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by s156543 on 8-6-2018.
 */

public class eventDatabase extends SQLiteOpenHelper {
    private static eventDatabase instance;

    private eventDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        SQLiteDatabase db = sqLiteDatabase;
        String query = "CREATE TABLE events (_id INTEGER PRIMARY KEY, title STRING, location STRING," +
                " date STRING, time STRING, datetime DATETIME, imgurl STRING, type STRING, eventurl STRING," +
                "description STRING, saved BIT);";
        db.execSQL(query);
        query = "CREATE TABLE websites (_id INTEGER PRIMARY KEY, url STRING, type STRING, method STRING);";
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

    public  static eventDatabase getInstance(Context context){
        if(instance == null){
            instance = new eventDatabase(context, null, null, 1);
        }
        return instance;
    }

    // Insert new journal entry in the entries database
    public void insertWebsite(WebsiteEntry websiteEntry){
        SQLiteDatabase entrydb =  this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("url", websiteEntry.url);
        contentValues.put("type", websiteEntry.type);
        contentValues.put("method", websiteEntry.method);

        entrydb.insert("websites", null, contentValues);
    }

    public void insertEvent(EventEntry eventEntry){
        SQLiteDatabase entrydb =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String dateString;
        if(eventEntry.dateTime != null) dateString = eventEntry.dateTime.toString();
        else dateString = "1970-12-31 00:00:00";

        contentValues.put("title", eventEntry.title);
        contentValues.put("location", eventEntry.organizer);
        contentValues.put("date", eventEntry.date);
        contentValues.put("time", eventEntry.time);
        contentValues.put("datetime", dateString);
        contentValues.put("imgurl", eventEntry.imgUrl);
        contentValues.put("type", eventEntry.type);
        contentValues.put("eventurl", eventEntry.eventUrl);
        contentValues.put("description", eventEntry.description);

        if(eventEntry.saved){
            contentValues.put("saved", 1);
        }
        else{ contentValues.put("saved", 0);}

        entrydb.insert("events", null, contentValues);
    }

    // grabs all journal entries
    public Cursor selectAllWebsites(){
        SQLiteDatabase selectAlldb = this.getWritableDatabase();
        Cursor cursor = selectAlldb.rawQuery("SELECT * FROM websites", null);
        return cursor;
    }

    public Cursor selectSavedEvents(){
        SQLiteDatabase selectAlldb = this.getWritableDatabase();
        Cursor cursor = selectAlldb.rawQuery("SELECT * FROM events WHERE saved = '1'", null);
        return cursor;
    }

    public Cursor selectAllEvents(){
        SQLiteDatabase selectAlldb = this.getWritableDatabase();
        Cursor cursor = selectAlldb.rawQuery("SELECT * FROM events ORDER BY datetime ASC", null);
        return cursor;
    }

    public void updateDescription(String d, long id){
        SQLiteDatabase entrydb =  this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("description", d);
        entrydb.update("events", cv,"_id =" + id, null);
    }

    public void saveEntry(long id){
        SQLiteDatabase entrydb =  this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("saved", "1");
        entrydb.update("events", cv, "_id =" + id, null);
    }

    public void deleteWebsite(long id){
        SQLiteDatabase entrydb =  this.getWritableDatabase();
        Cursor c = selectAllWebsites();
        String site = "";
        if( c.moveToNext()){
            if(c.getLong(c.getColumnIndex("_id")) == id){
                site = c.getString(c.getColumnIndex("url"));
            }
        }
        String organizer = site;
        int index = organizer.indexOf(".");
        if (index > 0)
            organizer = organizer.substring(0, index);
        int i = organizer.indexOf("/");
        if (i > 0)
            organizer = organizer.substring(i + 2, organizer.length());

        entrydb.delete("websites", "_id =" + id, null);
        entrydb.delete("events", "location =" + organizer, null);
    }

    public void saveEvent(long id, int bool){
        SQLiteDatabase entrydb =  this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("saved", bool);
        entrydb.update("events", cv, "_id =" + id, null);
    }

}
