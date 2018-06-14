package com.example.s156543.eventjes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        Cursor cursor = db.selectAll();
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

    private void updateData() {
        ListView lv = findViewById(R.id.eventlist);

        eventDatabase db = eventDatabase.getInstance(getApplicationContext());
        WebsiteEntry we = new WebsiteEntry("http://subbacultcha.nl/events", "type", "standard");
        db.insertWebsite(we);

        Cursor c = db.selectAll();
        final ArrayList<String> titlesArray = new ArrayList<>();


        Scraper scraper = new Scraper(CalendarActivity.this, lv);
        while (c.moveToNext()) {
            String url = c.getString(c.getColumnIndex("url"));
            scraper.scrapeTitle(url, titlesArray, db);
        }
    }

    protected void onResume() {

        super.onResume();
        updateData();
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