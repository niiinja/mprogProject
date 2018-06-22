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
    Adapter adapter;

    public Scraper() {

    }

    public ArrayList<String> scrapeTitle(Activity act, ListView listview, String u,
                                         final ArrayList<String> titlesArray, eventDatabase eventdatabase) {

        final Activity activity = act;
        final String url = u;
        final eventDatabase db = eventdatabase;

        this.adapter = adapter;
        final ListView lv = listview;

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
                        //String u = t.attr("abs:href");
                        //u = t.childNode(0).attributes().toString();
                        String u = t.childNode(0).attr("href").toString();

                        EventEntry eventEntry = new EventEntry(org, t.text(), "00:00",
                                "0-0-0000", "$0", imgUrl, "standard", u,
                                "no description", false);
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
        Element grandparent = t.parent().parent();
        Element image = grandparent.select("img").first();
        String imageHref;
        if(image != null){
             imageHref = image.attr("src");}
        else imageHref = "https://78.media.tumblr.com/avatar_5c436267e955_128.pnj";
        return imageHref;
    }


    public void scrapeP(String eventUrl, final eventDatabase eventdatabase, long ID){
        final String url = eventUrl;
        //final EventEntry event = evententry;
        final eventDatabase db = eventdatabase;
        final long id = ID;


        //final eventDatabase instance = eventDatabase.getInstance(activity);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // SELECT url FROM websites
                    Document doc = Jsoup.connect(url).get();

                    Elements paragraphs = doc.select("p");
                    for( Element p : paragraphs) {
                        String d = p.text();
                        //event.description = d;
                        eventdatabase.updateDescription(d, id);
                        break;
                    }
                }
                catch (IOException e) {
                }
            }
        }).start();
        //return d[0];

    }
}
