package com.example.s156543.eventjes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class CalendarActivity extends AppCompatActivity {
    private Button settings;
    private Button browse;
    private Button switches;
    private ListView eventlist;

    private TextView temp;
    public static ArrayList<String> websites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Document doc = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        temp = (TextView) findViewById(R.id.temp);
        browse = (Button) findViewById(R.id.browse);
        settings = (Button) findViewById(R.id.settings);
        switches = findViewById(R.id.switchcalendar);
        eventlist = findViewById(R.id.eventlist);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSettings();
            }
        });
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWebsite();
            }
        });
        switches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDetails();
            }
        });
        eventlist.setOnItemClickListener(new ListViewClickListener());
        //websites.add("https://radar.squat.net/en/events/city/Amsterdam");
        //getWebsite();
    }

    private void toSettings(){
        // Clicked category is passed to the MenuActivity
        Intent intent = new Intent(CalendarActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void getWebsite(){
        System.out.print("aanroep");

//        TextView urlview = findViewById(R.id.input);
//        final String url = Objects.toString(urlview.getText());
//        final ArrayList<String> ts = new ArrayList<String>();
//        ts.add("test");
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                try {
                    Document doc = Jsoup.connect("http://subbacultcha.nl/events/").get();
                    //Document doc = Jsoup.connect("https://radar.squat.net/en/events/city/Amsterdam").get();
                    //BufferedReader r = new BufferedReader(new FileReader(String.valueOf(doc)));
                    int ch;
                    int count = 0;
                    System.out.print("PRINT");

                    // kk leluuuuukkkkkkkk
                    Elements titles = null;
                    Elements h1s = doc.select("h1");
                    Elements h2s = doc.select("h2");
                    Elements h3s = doc.select("h3");
                    Elements h4s = doc.select("h4");

                    titles = h1s;

                    if(h2s.size() > titles.size()){
                        titles = h2s;
                    }

                    if(h3s.size() > titles.size()){
                        titles = h3s;
                    }

                    if(h4s.size() > titles.size()){
                        titles = h4s;
                    }
                    for(Element t : titles){
                        builder.append(t.text()).append("\n");
                        //ts.add(t.text());
                    }


                } catch (IOException e) {
                    builder.append("Error!");
                    }
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        temp.setText(builder.toString());
                       // ts.add("error");
                    }
                });
            }
        }).start();
//        ArrayAdapter<String> calendarAdapter =
//                new CalendarAdapter(this, R.layout.eventlist_row, ts);
//        ListView lv = findViewById(R.id.eventlist);
//
//        lv.setAdapter(calendarAdapter);
    }
    private void toDetails(){
        Intent intent = new Intent(CalendarActivity.this, DetailActivity.class);
        startActivity(intent);
    }
    private class ListViewClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Intent intent = new Intent(CalendarActivity.this, DetailActivity.class);
            startActivity(intent);
        }
    }
}

