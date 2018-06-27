package com.example.s156543.eventjes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity {
    private Button settings;
    private Button browse;
    private Button switches;
    private ListView eventlist;

    private TextView temp;
    boolean save = false;
    ToggleButton toggle;
    eventDatabase db;
    CalendarAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switches = findViewById(R.id.switchCalendar);
        setContentView(R.layout.activity_calendar);
        db = eventDatabase.getInstance(getApplicationContext());
        toggle = findViewById(R.id.switchCalendar);

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

        eventlist = findViewById(R.id.eventlist);
        eventlist.setOnItemClickListener(new ListViewClickListener());
        eventlist.setAdapter(adapter);

        temp = (TextView) findViewById(R.id.temp);
        settings = (Button) findViewById(R.id.settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSettings();
            }
        });
    }

    private void toSettings() {
        // Clicked category is passed to the MenuActivity
        Intent intent = new Intent(CalendarActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void updateData(Boolean save) {
        ListView lv = findViewById(R.id.eventlist);

        eventDatabase db = eventDatabase.getInstance(getApplicationContext());
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
        System.out.println("resume update");

    }

    // Listener on the Listview which checks whether a user clicks a category
    private class ListViewClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Cursor click  = (Cursor) adapterView.getItemAtPosition(i);

            // Clicked category is passed to the MenuActivity
            Intent intent = new Intent(CalendarActivity.this, DetailActivity.class);
            intent.putExtra("save", save);
            intent.putExtra("position", i);

            startActivity(intent);
        }
    }
}