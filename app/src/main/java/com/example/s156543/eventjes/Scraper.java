package com.example.s156543.eventjes;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by s156543 on 11-6-2018.
 */

public class Scraper {
    public Scraper() {
    }

    public ArrayList<String> scrapeTitle(String u){
        final String url = u;
        final ArrayList<String> titles = new ArrayList<>();
        titles.add("test");

        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                try {
                    // SELECT url FROM websites
                    Document doc = Jsoup.connect(url).get();
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
                        //builder.append(t.text()).append("\n");
                        //ts.add(t.text());
                        String te = new String(t.text());
                        titles.append(te);
                    }


                } catch (IOException e) {
                    builder.append("Error!");
                }
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        //temp.setText(builder.toString());
//                        // ts.add("error");
//                    }
//                });
            }
        }).start();
        return titles;
    }

}
