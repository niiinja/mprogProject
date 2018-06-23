package com.example.s156543.eventjes;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by s156543 on 7-6-2018.
 */

public class CalendarAdapter extends CursorAdapter{

    String url;
    String title;
    String time;
    Cursor cursor;

    public CalendarAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

//    @NonNull
//    @Override
//
//
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        convertView = LayoutInflater.from(getContext()).inflate(R.layout.eventlist_row, parent, false);
//
//        TextView titleView = (TextView) convertView.findViewById(R.id.title);
//        TextView locView = (TextView) convertView.findViewById(R.id.location);
//        TextView timeView = convertView.findViewById(R.id.time);
//
//        if(cursor.move(position)){
//            title = cursor.getString(cursor.getColumnIndex("title"));
//            url = cursor.getString(cursor.getColumnIndex("eventurl"));
//            time = cursor.getString(cursor.getColumnIndex("time"));
//
//        }
//        titleView.setText(title);
//
//        int index = url.indexOf(".");
//        if (index > 0)
//            url = url.substring(0, index);
//
//        index = url.indexOf("/");
//        if (index > 0 && index < 8)
//            url = url.substring(index + 2, url.length());
//
//        url = url.replace("http://", "");
//        locView.setText(url);
//
//        timeView.setText(time);
//
//        if (convertView == null){
//
//        }
//        return convertView;
//    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.eventlist_row, viewGroup, false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView locView = (TextView) view.findViewById(R.id.location);
        TextView timeView = view.findViewById(R.id.time);


        title = cursor.getString(cursor.getColumnIndex("title"));
        url = cursor.getString(cursor.getColumnIndex("eventurl"));
        time = cursor.getString(cursor.getColumnIndex("time"));

        titleView.setText(title);

        int index = url.indexOf(".");
        if (index > 0)
            url = url.substring(0, index);

        index = url.indexOf("/");
        if (index > 0 && index < 8)
            url = url.substring(index + 2, url.length());

        url = url.replace("http://", "");
        locView.setText(url);

        timeView.setText(time);

    }
}
