package com.example.s156543.eventjes;

import android.app.Activity;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by s156543 on 11-6-2018.
 */

public class Scraper {
    Activity activity;
    ListView lv;
    Adapter adapter;
    public Scraper(Activity activity, ListView lv) {
        this.activity = activity;
        this.adapter = adapter;
        this.lv = lv;
    }

    public ArrayList<String> scrapeTitle(String u, final ArrayList<String> titlesArray){
        final String url = u;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                try {
                    // SELECT url FROM websites
                    Document doc = Jsoup.connect(url).get();

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
                        titlesArray.add(t.text());

                    }


                } catch (IOException e) {
                    titlesArray.add("error");


                }
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        adapter = new CalendarAdapter(activity, R.layout.eventlist_row, titlesArray, url);
                        lv.setAdapter((ListAdapter) adapter);                    }
                });
            }
        }).start();
        for(String t : titlesArray){
            System.out.println(t);
            }
        return titlesArray;
    }

}
