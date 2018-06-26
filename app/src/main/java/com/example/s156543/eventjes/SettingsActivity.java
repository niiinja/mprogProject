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

import java.util.ArrayList;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    Button addBtn;
    SettingsAdapter adapter;
    Button removeBTN;
    ArrayList<Integer> selected = new ArrayList<>();
    eventDatabase db;
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

        removeBTN = (Button) findViewById(R.id.remove);
        removeBTN.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View view){ removeButton();}
        });

        db = eventDatabase.getInstance(getApplicationContext());
        Cursor c = db.selectAllWebsites();


        adapter = new SettingsAdapter(this, c, true);
        ListView sl = findViewById(R.id.scrollList);
        sl.setAdapter((ListAdapter) adapter);

        sl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                EventEntry eventEntry = (EventEntry) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + eventEntry.title,
                        Toast.LENGTH_LONG).show();
                selected.add(position);
            }
        });
        scraper = new Scraper();
    }

    private void addButton(){
        TextView urlview = findViewById(R.id.input);
        String url = Objects.toString(urlview.getText());
        if (url.substring(0,3) != "http"){
            url = "http://" + url;
        }
        scraper.scrapeTitle(url, db, this);

        WebsiteEntry websiteEntry = new WebsiteEntry(url, "bla", "standard");
        eventDatabase instance = eventDatabase.getInstance(this);
        instance.insertWebsite(websiteEntry);
    }

    private void removeButton(){
        //welke zijn er aangeklikt

        // zoek index in de array
        for(int id : selected){
            long ID = new Long(id);
            db.deleteWebsite(ID);
        }
        //verwijder die id uit de db
    }
}
