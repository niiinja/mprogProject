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
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by s156543 on 20-6-2018.
 */

public class SettingsAdapter extends CursorAdapter{

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.website_row, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView buttonText = (TextView) view.findViewById(R.id.checkWebsite);
        String website = cursor.getString(cursor.getColumnIndex("url"));
        buttonText.setText(website);
    }

    public SettingsAdapter(Context context, Cursor cursor,boolean autoRequery){

        super(context, cursor, autoRequery);
    }
}
