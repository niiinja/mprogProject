package com.example.s156543.eventjes;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by s156543 on 7-6-2018.
 */

public class CalendarAdapter extends ResourceCursorAdapter {
    Scraper scraper;

    public CalendarAdapter(Context context, Cursor cursor){
        super(context, R.layout.eventlist_row, cursor);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        scraper = new Scraper();
        ArrayList<String> titles = new ArrayList<>();

        String url = cursor.getString(cursor.getColumnIndex("url"));
        titles = scraper.scrapeTitle(url);

        for(String t : titles){
            TextView locationView = view.findViewById(R.id.location);
            locationView.setText(url);
            TextView titleView = view.findViewById(R.id.title);
            titleView.setText(t);
            TextView timeView = view.findViewById(R.id.time);
            timeView.setText("00:00");
        }

    }


}
