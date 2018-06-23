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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by s156543 on 11-6-2018.
 */

public class Scraper {
    Adapter adapter;

    public Scraper() {

    }

    public void scrapeTitle(Activity act, ListView listview, String u,
                            final ArrayList<String> titlesArray, eventDatabase eventdatabase) {

        final Activity activity = act;
        final String url = u;
        final eventDatabase db = eventdatabase;
        final ListView lv = listview;

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

                        String u = t.childNode(0).attr("href").toString();
                        String date = scrapeDate(u);

                        String time = scrapeTime(t, u);
                        EventEntry eventEntry = new EventEntry(org, t.text(), time,
                                date, "$0", imgUrl, "standard", u,
                                "no description", false);
                        db.insertEvent(eventEntry);

                    }


                } catch (IOException e) {
                    titlesArray.add("error");
                }

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        return;
                    }
                });
            }
        }
        ).start();
    }

    // Edits the website URL into the name of the organizer
    private String getOrganizer(String u) {
        String organizer = u;
        int index = organizer.indexOf(".");
        if (index > 0)
            organizer = organizer.substring(0, index);
        int i = organizer.indexOf("/");
        if (i > 0)
            organizer = organizer.substring(i + 2, organizer.length());

        return organizer;
    }

    // Selects the image in the parent of the parent of the title's element
    private String findImg(Document doc, Element t) {
        Element grandparent = t.parent().parent();
        Element image = grandparent.select("img").first();
        String imageHref;

        if (image != null) {
            imageHref = image.attr("src");
        } else imageHref = "https://78.media.tumblr.com/avatar_5c436267e955_128.pnj";
        return imageHref;
    }

    // Scrapes the description: the first paragtaph on the event's page.
    public void scrapeP(String eventUrl, final eventDatabase eventdatabase, long ID, DetailActivity act) {
        final String url = eventUrl;
        final DetailActivity activity = act;
        final eventDatabase db = eventdatabase;
        final long id = ID;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(url).get();

                    Elements paragraphs = doc.select("p");
                    for (Element p : paragraphs) {
                        String d = p.text();
                        //event.description = d;
                        eventdatabase.updateDescription(d, id);
                        break;
                    }
                } catch (IOException e) {
                }
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        activity.updateDescription();
                    }
                });


            }
        }).start();
    }

    public String scrapeTime(Element t, String u) throws IOException {
        Pattern pattern = Pattern.compile("([0-9]|0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])");
        String time = "00:00";
        Element parent = t.parent();
        //time = parent.getElementsMatchingText(pattern).first();
        Matcher matcher;
        matcher = pattern.matcher(parent.toString());

        if (matcher.find()) {
            time = matcher.group();

        } else {
            parent = parent.parent();
            matcher = pattern.matcher(parent.toString());
            if (matcher.find()) {
                time = matcher.group();
            }
            else{
                Document doc = Jsoup.connect(u).get();
                parent =  doc.selectFirst("body");
                matcher = pattern.matcher(parent.toString());
                if (matcher.find()) {
                    time = matcher.group();
                }
            }
        }
        return time;
    }

    private String scrapeDate(final String url) throws IOException {

                String date = "";
                String u = url;

                    // SELECT url FROM websites
                    Document doc = Jsoup.connect(u).get();
                    Element body = doc.selectFirst("body");

                    Pattern pattern = Pattern.compile("(0?[1-9]|[12][0-9]|3[01]) " +
                            "(?:Jan(?:uary)?|Feb(?:ruary)?|Mar(?:ch)?|Apr(?:il)|Jun(?:e)?|Jul(?:y)?|" +
                            "Aug(?:ust)?|Sep(?:tember)?|Oct(?:ober)?|Nov(?:ember)?|Dec(?:ember)?)");

                    Matcher matcher;
                    matcher = pattern.matcher(body.toString());
                    if (matcher.find()) {
                        date = matcher.group();
                    }
                return date;
            }

}
