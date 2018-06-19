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
                " date STRING, time STRING, price STRING, imgurl STRING, type STRING, eventurl STRING," +
                "description STRING);";
        db.execSQL(query);
        query = "CREATE TABLE websites (_id INTEGER PRIMARY KEY, url STRING, type STRING, method STRING);";
        db.execSQL(query);

//        //String samples = "INSERT INTO events (title, location, date, time, price, type) VALUES " +
//                "('odorama', 'spannend', 'VANDAAG', '19:15', 'free', 'workshop');";
//        db.execSQL(samples);

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

    // insert new journal entry in the entries database
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

        contentValues.put("title", eventEntry.title);
        contentValues.put("location", eventEntry.organizer);
        contentValues.put("date", eventEntry.date);
        contentValues.put("time", eventEntry.time);
        contentValues.put("price", eventEntry.price);
        contentValues.put("imgurl", eventEntry.imgUrl);
        contentValues.put("type", eventEntry.type);
        contentValues.put("eventurl", eventEntry.eventUrl);
        contentValues.put("description", eventEntry.description);

        entrydb.insert("events", null, contentValues);

    }

    // grabs all journal entries
    public Cursor selectAll(){
        SQLiteDatabase selectAlldb = this.getWritableDatabase();
        Cursor cursor = selectAlldb.rawQuery("SELECT * FROM websites", null);
        return cursor;
    }

    public Cursor selectAllEvents(){
        SQLiteDatabase selectAlldb = this.getWritableDatabase();
        Cursor cursor = selectAlldb.rawQuery("SELECT * FROM events", null);
        return cursor;
    }

    public void updateDescription(String d, long id){
        SQLiteDatabase entrydb =  this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("description", "d");
        entrydb.update("events", cv,"_id =" + id, null);
    }
}
