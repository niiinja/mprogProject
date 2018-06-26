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
    String date;

    public CalendarAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.eventlist_row, viewGroup, false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView locView = (TextView) view.findViewById(R.id.location);
        TextView timeView = view.findViewById(R.id.time);
        TextView dateView = view.findViewById(R.id.date);


        title = cursor.getString(cursor.getColumnIndex("title"));
        url = cursor.getString(cursor.getColumnIndex("location"));
        time = cursor.getString(cursor.getColumnIndex("time"));
        date = cursor.getString(cursor.getColumnIndex("date"));

        titleView.setText(title);

        locView.setText(url);

        timeView.setText(time);
        dateView.setText(date);

    }
}
