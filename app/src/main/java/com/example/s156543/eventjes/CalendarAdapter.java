package com.example.s156543.eventjes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by s156543 on 7-6-2018.
 */

public class CalendarAdapter extends ArrayAdapter<String> {
    ArrayList<String> events;
    @NonNull
    @Override


    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.eventlist_row, parent, false);

        TextView titleView = (TextView) convertView.findViewById(R.id.title);
        String event = events.get(position);
        titleView.setText(event);
        System.out.print(event);

        if (convertView == null){

        }
        return convertView;
    }

    public CalendarAdapter(@NonNull Context context, int resource,
                           @NonNull ArrayList<String> categories) {

        super(context, resource, categories);


        this.events = categories;


    }
}
