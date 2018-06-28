package com.example.s156543.eventjes;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Users can manage websites in this activity, which will be scraped for events once they are added.
 */

public class SettingsActivity extends AppCompatActivity {
    Button addBtn;
    SettingsAdapter adapter;
    Button removeBtn;
    ArrayList<String> selected = new ArrayList<>();
    EventDatabase db;
    Scraper scraper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        addBtn = findViewById(R.id.addbtn);
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){ addButton();}
        });
        removeBtn = findViewById(R.id.remove);
        removeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View view){ removeButton();}
        });
        db = EventDatabase.getInstance(getApplicationContext());
        scraper = new Scraper();

        updateAdapter();
    }

    // Adds a new event page to the database, and scrapes the URL
    private void addButton(){
        TextView urlview = findViewById(R.id.input);
        String url = Objects.toString(urlview.getText());
        if (url.substring(0,3) != "http")
            url = "http://" + url;

        try {
            scraper.scrapeForEvents(url, db, this);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        WebsiteEntry websiteEntry = new WebsiteEntry(url);
        EventDatabase instance = EventDatabase.getInstance(this);
        instance.insertWebsite(websiteEntry);

        Toast.makeText(getApplicationContext(),
                "Added website: " + url,
                Toast.LENGTH_LONG).show();
        updateAdapter();
    }

    // Removes a website and all its events
    private void removeButton(){
        for(String t : selected){
            db.deleteWebsite(t);
            Toast.makeText(getApplicationContext(),
                    "Removed website: " + t,
                    Toast.LENGTH_SHORT).show();
        }
        updateAdapter();
    }

    public void clickedBox(View view){
        String title = ((CheckBox) view).getText().toString();
        selected.add(title);
    }

    // Updates the displayed websites
    private void updateAdapter(){
        Cursor c = db.selectAllWebsites();
        adapter = new SettingsAdapter(this, c, true);
        ListView websiteList = findViewById(R.id.websiteList);
        websiteList.setAdapter(adapter);
    }
}
