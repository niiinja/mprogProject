package com.example.s156543.eventjes;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
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

    public static ArrayList<String> titles = new ArrayList<String>();

    eventDatabase db;
    CalendarAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Document doc = null;
        super.onCreate(savedInstanceState);
        switches = findViewById(R.id.switchCalendar);
        setContentView(R.layout.activity_calendar);

        db = eventDatabase.getInstance(getApplicationContext());

        ToggleButton toggle = findViewById(R.id.switchCalendar);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updateData(true);
                } else {updateData(false);               }
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
            ArrayList<String> titlesArray = null;

            while(c.moveToNext()){
                String title = c.getString(c.getColumnIndex("title"));
                titlesArray.add(title);
            }

            String url = "bla";
            adapter = new CalendarAdapter(this, R.layout.eventlist_row, titlesArray, url);
            lv.setAdapter( adapter);
        }

        else{
            c = db.selectAllWebsites();

            final ArrayList<String> titlesArray = new ArrayList<>();
            Scraper scraper = new Scraper();

            while (c.moveToNext()) {
                String url = c.getString(c.getColumnIndex("url"));
                scraper.scrapeTitle(CalendarActivity.this, lv, url, titlesArray, db);
            }
        }
    }


    protected void onResume() {

        super.onResume();
        updateData(false);
        System.out.println("resume update");

    }

    // Listener on the Lisview which checks whether a user clicks a category
    private class ListViewClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String click  = (String) adapterView.getItemAtPosition(i);
//
//            String entryTitle = click.getString(click.getColumnIndex("title"));
//            String entryOrg = click.getString(click.getColumnIndex("organizer"));

            // Clicked category is passed to the MenuActivity
            Intent intent = new Intent(CalendarActivity.this, DetailActivity.class);

            intent.putExtra("title", click);
            intent.putExtra("organizer", click);
            intent.putExtra("position", i);

            startActivity(intent);
        }
    }



}