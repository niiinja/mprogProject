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

public class CalendarActivity extends AppCompatActivity {
    private Button settings;
    private Button browse;
    private Button switches;
    private ListView eventlist;

    private TextView temp;

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
        adapter = new CalendarAdapter(this, cursor);
        eventlist = findViewById(R.id.eventlist);
        eventlist.setAdapter(adapter);

        temp = (TextView) findViewById(R.id.temp);
        settings = (Button) findViewById(R.id.settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSettings();
            }
        });

//        switches.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toDetails();
//            }
//        });
        //eventlist.setOnItemClickListener(new ListViewClickListener());
        //websites.add("https://radar.squat.net/en/events/city/Amsterdam");
        //getWebsite();
    }

    private void toSettings(){
        // Clicked category is passed to the MenuActivity
        Intent intent = new Intent(CalendarActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void toDetails(){
        Intent intent = new Intent(CalendarActivity.this, DetailActivity.class);
        startActivity(intent);
    }

    private void updateData(){
        eventDatabase db = eventDatabase.getInstance(getApplicationContext());
        Cursor c = db.selectAll();

        adapter.swapCursor(c);
    }

    protected void onResume() {

        super.onResume();
        updateData();
        System.out.println("resume update");

    }

//    private void getWebsite(){
//        SQLiteDatabase db;
//        System.out.print("aanroep");
//
////        TextView urlview = findViewById(R.id.input);
////        final String url = Objects.toString(urlview.getText());
////        final ArrayList<String> ts = new ArrayList<String>();
////        ts.add("test");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final StringBuilder builder = new StringBuilder();
//                try {
//                    // SELECT url FROM websites
//                    Document doc = Jsoup.connect("http://subbacultcha.nl/events/").get();
//                    //Document doc = Jsoup.connect("https://radar.squat.net/en/events/city/Amsterdam").get();
//                    //BufferedReader r = new BufferedReader(new FileReader(String.valueOf(doc)));
//                    int ch;
//                    int count = 0;
//                    System.out.print("PRINT");
//
//                    // kk leluuuuukkkkkkkk
//                    Elements titles = null;
//                    Elements h1s = doc.select("h1");
//                    Elements h2s = doc.select("h2");
//                    Elements h3s = doc.select("h3");
//                    Elements h4s = doc.select("h4");
//
//                    titles = h1s;
//
//                    if(h2s.size() > titles.size()){
//                        titles = h2s;
//                    }
//
//                    if(h3s.size() > titles.size()){
//                        titles = h3s;
//                    }
//
//                    if(h4s.size() > titles.size()){
//                        titles = h4s;
//                    }
//                    for(Element t : titles){
//                        builder.append(t.text()).append("\n");
//                        //ts.add(t.text());
//                    }
//
//
//                } catch (IOException e) {
//                    builder.append("Error!");
//                    }
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        temp.setText(builder.toString());
//                       // ts.add("error");
//                    }
//                });
//            }
//        }).start();
//        ArrayAdapter<String> calendarAdapter =
//                new CalendarAdapter(this, R.layout.eventlist_row, ts);
//        ListView lv = findViewById(R.id.eventlist);
//
//        lv.setAdapter(calendarAdapter);
    }

//    private class ListViewClickListener implements AdapterView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            Intent intent = new Intent(CalendarActivity.this, DetailActivity.class);
//            startActivity(intent);
//        }
//    }

//    public Cursor getWebsites(){
//        SQLiteDatabase db;
//        Cursor cursor = getReadableDatabase().rawQuery("FROM TABLE websites SELECT url FROM websites");
//        return cursor;
//    }


