package com.example.s156543.eventjes;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * This activity displays all the (saved) events that are stored in the database
 */

public class CalendarActivity extends AppCompatActivity {

    private ListView eventlist;
    boolean save = false;
    ToggleButton toggle;
    EventDatabase db;
    CalendarAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        toggle = findViewById(R.id.switchCalendar);
        Button settings = findViewById(R.id.settings);
        Button refresh = findViewById(R.id.refresh);

        db = EventDatabase.getInstance(getApplicationContext());

        eventlist = findViewById(R.id.eventlist);
        eventlist.setOnItemClickListener(new ListViewClickListener());
        eventlist.setAdapter(adapter);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSettings();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData(save);
            }
        });

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    save = true;
                    updateData(save);
                } else {
                    save = false;
                    updateData(save);               }
            }
        });

    }

    // Clicked category is passed to the MenuActivity
    private void toSettings() {
        Intent intent = new Intent(CalendarActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void updateData(Boolean save) {
        ListView lv = findViewById(R.id.eventlist);
        EventDatabase db = EventDatabase.getInstance(getApplicationContext());
        Cursor c;

        if(save) {
            c = db.selectSavedEvents();
            ArrayList<String> titlesArray = new ArrayList<>();

            while(c.moveToNext()){
                String title = c.getString(c.getColumnIndex("title"));
                titlesArray.add(title);
            }
            adapter = new CalendarAdapter(this, c, true);
            lv.setAdapter(adapter);
            return;
        }

        else{
            Cursor ec = db.selectAllEvents();
            adapter = new CalendarAdapter(this, ec, true);
            lv.setAdapter(adapter);
        }
    }

    protected void onResume() {
        super.onResume();
        toggle.setChecked(save);
        updateData(save);
    }

    // Listener on the Listview which checks whether a user clicks a category
    private class ListViewClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(CalendarActivity.this, DetailActivity.class);
            intent.putExtra("save", save);
            intent.putExtra("position", i);

            startActivity(intent);
        }
    }
}