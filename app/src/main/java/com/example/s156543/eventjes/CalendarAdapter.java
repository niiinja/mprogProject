package com.example.s156543.eventjes;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by s156543 on 7-6-2018.
 */

public class CalendarAdapter extends ArrayAdapter<String>{
    private final ArrayList<String> titles;
    String url;

    ArrayList<String> events;
    @NonNull
    @Override


    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.eventlist_row, parent, false);

        TextView titleView = (TextView) convertView.findViewById(R.id.title);
        String title = titles.get(position);
        titleView.setText(title);

        int index = url.indexOf(".");
        if (index > 0)
            url = url.substring(0, index);

        index = url.indexOf("/");
        if (index > 0 && index < 8)
            url = url.substring(index + 2, url.length());

        url = url.replace("http://", "");



        TextView locView = convertView.findViewById(R.id.location);
        locView.setText(url);

        TextView timeView = convertView.findViewById(R.id.time);
        timeView.setText("00:00");

        if (convertView == null){

        }
        return convertView;
    }

    public CalendarAdapter(@NonNull Context context, int resource,
                           @NonNull ArrayList<String> titles, String url) {

        super(context, resource, titles);


        this.titles = titles;
        this.url = url;

    }
    }//    Scraper scraper;
//
//    public CalendarAdapter(Context context, Cursor cursor){
//        super(context, R.layout.eventlist_row, cursor);
//    }
//
//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
////        scraper = new Scraper();
////        ArrayList<String> titles;
////
////        String url = cursor.getString(cursor.getColumnIndex("url"));
////        titles = scraper.scrapeTitle(url);
//
//        for(String t : titles){
//            TextView locationView = view.findViewById(R.id.location);
//            locationView.setText(url);
//            TextView titleView = view.findViewById(R.id.title);
//            titleView.setText(t);
//            TextView timeView = view.findViewById(R.id.time);
//            timeView.setText("00:00");
//        }
//
//    }
//
//
//}
