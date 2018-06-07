package com.example.s156543.eventjes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.Objects;

public class CalendarActivity extends AppCompatActivity {
    private Button getBtn;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Document doc = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        result = (TextView) findViewById(R.id.result);
        getBtn = (Button) findViewById(R.id.getBtn);
        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWebsite();
            }
        });
    }

    private void getWebsite(){
        TextView urlview = findViewById(R.id.input);
        final String url = Objects.toString(urlview.getText());

        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    Document doc = Jsoup.connect(url).get();
                    //Document doc = Jsoup.connect("https://radar.squat.net/en/events/city/Amsterdam").get();

                    String title = doc.title();
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
                    }

                } catch (IOException e) {
                    builder.append("Error!");
                    }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(builder.toString());
                    }
                });
            }
        }).start();
    }
}
