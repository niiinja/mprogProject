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

    public ArrayList<String> scrapeTitle(String u, final ArrayList<String> titlesArray, eventDatabase eventdatabase) {
        final String url = u;
        final eventDatabase db = eventdatabase;

        //final eventDatabase instance = eventDatabase.getInstance(activity);

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

                    if (h2s.size() > titles.size()) {
                        titles = h2s;
                    }

                    if (h3s.size() > titles.size()) {
                        titles = h3s;
                    }

                    if (h4s.size() > titles.size()) {
                        titles = h4s;
                    }
                    for (Element t : titles) {
                        String imgUrl = findImg(doc, t);
                        titlesArray.add(t.text());
                        String org = getOrganizer(url);
                        Element u = t.selectFirst("a[href]");


                        EventEntry eventEntry = new EventEntry(org, t.text(), "00:00",
                                "0-0-0000", "$0", imgUrl, "standard", u.text());
                        db.insertEvent(eventEntry);


                        Elements url = doc.select("h1");

                    }


                } catch (IOException e) {
                    titlesArray.add("error");


                }
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        adapter = new CalendarAdapter(activity, R.layout.eventlist_row, titlesArray, url);
                        lv.setAdapter((ListAdapter) adapter);
                    }
                });
            }
        }).start();
        for (String t : titlesArray) {
            System.out.println(t);
        }
        return titlesArray;
    }


    private String getOrganizer(String u) {
        String organizer = u;
        int index = organizer.indexOf(".");
        if (index > 0)
            organizer = organizer.substring(0, index);

        int i = organizer.indexOf("/");
        if (i > 0)// && index < 8)
            organizer = organizer.substring(i + 2, organizer.length());

        return organizer;
    }

    private String findImg(Document doc, Element t) {
        String imgurl;
        Document eventDoc;
//        Elements images = doc.select(t + "> img" );
//        for (Element i : images){
//            return i.attr("src");
//        }
        return "https://78.media.tumblr.com/123380213bb9d9634a04cc882b0fccad/tumblr_p0xuchO7lC1twki9io1_1280.jpg";
    }
}

    public String scrapeP(String title, eventDatabase eventdatabase, EventEntry evententry){
        final String url = title;
        final EventEntry event = evententry;
        final eventDatabase db = eventdatabase;

        //final eventDatabase instance = eventDatabase.getInstance(activity);

        new Thread(new Runnable() {
            @Override
            public void run() {

                final StringBuilder builder = new StringBuilder();
                try {
                    // SELECT url FROM websites
                    Document doc = Jsoup.connect(url).get();

                    Elements paragraphs = doc.select("p");
                    for( Element p : paragraphs) {
                        String d = p.toString();
                        event.description = d;
                        db.insertEvent(eventEntry);
                        break;
                    }




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
