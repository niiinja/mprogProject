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
        String imgUrl = "https://78.media.tumblr.com/b9846a203d1ae13900848565e068c271/tumblr_p94sl3dytZ1rvzucio1_500.jpg";
        String time = "00:00";
        String date = "";
        int s = 0;
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

        if (c.move(position)){
            title = c.getString(c.getColumnIndex("title"));
            organizer = c.getString(c.getColumnIndex("location"));
            imgUrl = c.getString(c.getColumnIndex("imgurl"));
            eventID = c.getLong(c.getColumnIndex("_id"));
            eventUrl = c.getString(c.getColumnIndex("eventurl"));
            time = c.getString(c.getColumnIndex("time"));
            date = c.getString(c.getColumnIndex("date"));
            s = c.getInt(c.getColumnIndex("saved"));
        }

        scraper.scrapeP(eventUrl, db, eventID, this);
        TextView timeView = findViewById(R.id.time);
        timeView.setText(time);

        TextView dateView = findViewById(R.id.date);
        dateView.setText(date);

        //scraper.scrapeP(eventUrl, db, eventID);
        TextView titleview = findViewById(R.id.title);
        titleview.setText(title);
        TextView orgView = findViewById(R.id.organizer);
        orgView.setText(organizer);

        ImageView imageView = (ImageView) findViewById(R.id.img);
        Picasso.get().load(imgUrl).into(imageView); //https://square.github.io/picasso/

        ToggleButton saviour = findViewById(R.id.saviour);
        if(s == 1)
            saviour.setChecked(true);

        saviour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    save(1);

                else save(0);
            }
        });

    }

    private void save(int bool){
        db.saveEvent(eventID, bool);
    }

    public void updateDescription(){
        c = db.selectAllEvents();
        if (c.move(position))
            description = c.getString(c.getColumnIndex("description"));

        TextView desView = findViewById(R.id.description);
        desView.setText(description);
    }
}
