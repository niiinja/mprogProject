package com.example.s156543.eventjes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

/**
 * This activity scrapes for an event's description,
 * and displays the event details from the database.
 */

public class DetailActivity extends AppCompatActivity {
    int position;
    boolean save;
    EventDatabase db;
    Cursor c;
    String description = null;

    long eventID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = "title...";
        String organizer = "organizer...";
        String imgUrl = "";
        String time = "00:00";
        String date = "";
        int savedEvent = 0;
        eventID = 0;
        Scraper scraper = new Scraper();
        String eventUrl = null;
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();

        position = (int) intent.getSerializableExtra("position") + 1;
        save = (boolean) intent.getSerializableExtra("save");
        db = EventDatabase.getInstance(getApplicationContext());

        if(save)
            c = db.selectSavedEvents();
        else c = db.selectAllEvents();

        // Retrieves the detail info from the cursor
        if (c.move(position)){
            title = c.getString(c.getColumnIndex("title"));
            organizer = c.getString(c.getColumnIndex("organizer"));
            imgUrl = c.getString(c.getColumnIndex("imgurl"));
            eventID = c.getLong(c.getColumnIndex("_id"));
            eventUrl = c.getString(c.getColumnIndex("eventurl"));
            time = c.getString(c.getColumnIndex("time"));
            date = c.getString(c.getColumnIndex("date"));
            savedEvent = c.getInt(c.getColumnIndex("saved"));
        }

        // Scrapes for description
        scraper.scrapeDescription(eventUrl, db, eventID, this);

        TextView timeView = findViewById(R.id.time);
        TextView dateView = findViewById(R.id.date);
        TextView titleview = findViewById(R.id.title);
        TextView orgView = findViewById(R.id.organizer);
        ImageView imageView = findViewById(R.id.img);
        ToggleButton saviour = findViewById(R.id.saviour);

        timeView.setText(time);
        dateView.setText(date);
        titleview.setText(title);
        orgView.setText(organizer);

        // Loads the image from the internet into the activity using the Picasso library
        // More on Picasso: https://square.github.io/picasso/
        Picasso.get().load(imgUrl).into(imageView);

        // Saves or forgets the event when the user hits the toggle button
        if(savedEvent == 1)
            saviour.setChecked(true);

        saviour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    save(1);
                    Toast.makeText(getApplicationContext(),
                        "Saved event!",
                        Toast.LENGTH_SHORT).show();
                }
                else {
                    save(0);
                    Toast.makeText(getApplicationContext(),
                            "Forgot event!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void save(int bool){
        db.saveEvent(eventID, bool);

    }

    // Updates the description when the Scraper on another thread is finished.
    public void updateDescription(){
        c = db.selectAllEvents();
        if (c.move(position))
            description = c.getString(c.getColumnIndex("description"));

        TextView desView = findViewById(R.id.description);
        desView.setText(description);
    }
}
