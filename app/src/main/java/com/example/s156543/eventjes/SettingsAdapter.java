package com.example.s156543.eventjes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by s156543 on 20-6-2018.
 */

public class SettingsAdapter extends ArrayAdapter<String>{
    private final ArrayList<String> urls;

    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.website_row, parent, false);
        TextView buttonText = (TextView) convertView.findViewById(R.id.checkWebsite);
        buttonText.setText(urls.get(position));
        return convertView;
    }

    public SettingsAdapter(@NonNull Context context, int resource,
                           @NonNull ArrayList<String> urls){

        super(context, resource, urls);
        this.urls = urls;
    }
}
