package com.example.s156543.eventjes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
 * Adapter for the SettingsActivity, displays rows of added websites
 */

public class SettingsAdapter extends CursorAdapter{

    @Override
    public View newView(final Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.website_row, viewGroup,
                false);
        view.setClickable(true);
        view.setFocusable(true);
        view.setBackgroundResource(android.R.drawable.menuitem_background);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView buttonText = view.findViewById(R.id.checkWebsite);
        String website = cursor.getString(cursor.getColumnIndex("url"));
        buttonText.setText(website);
    }

    public SettingsAdapter(Context context, Cursor cursor,boolean autoRequery){
        super(context, cursor, autoRequery);
    }
}
