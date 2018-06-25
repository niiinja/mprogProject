package com.example.s156543.eventjes;

import android.app.Activity;
import android.widget.Adapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by s156543 on 11-6-2018.
 */

public class Scraper {

    public Scraper() {

    }

    public void scrapeTitle(String u, eventDatabase eventdatabase, final Activity activity) {

        final String url = u;
        final eventDatabase db = eventdatabase;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // SELECT url FROM websites
                    Document doc = Jsoup.connect(url).get();
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

                    int count = 0;

                        for (Element t : titles) {

                            String imgUrl = findImg(doc, t, url);
                            String org = getOrganizer(url);
                            String u = t.parent().selectFirst("a[href]").attr("abs:href");

                            String date = scrapeDate(u);
                            String time = scrapeTime(t, u, t.text());

                            //SimpleDateFormat sdf = new

                            EventEntry eventEntry = new EventEntry(org, t.text(), time,
                            date, "$0", imgUrl, "standard", u,
                            "no description", false);
                            db.insertEvent(eventEntry);
                            count += 1;

                            if(count >= 30){
                                break;
                            }
                        }



                } catch (IOException e) {
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
    private String findImg(Document doc, Element t, String url) {
        Element grandparent = t.parent().parent();
        Element image = grandparent.select("img").first();
        String imageHref;

        if (image != null) {
            imageHref = image.attr("src");

            if(imageHref.charAt(0) == '/') {
                int indexDot = url.indexOf('.');
                String pastDot = url.substring(indexDot, url.length());
                int indexSlash = indexDot + pastDot.indexOf('/');

                imageHref = url.substring(0, indexSlash) + imageHref;
                }
            }
        else imageHref = "https://proxy.duckduckgo.com/iu/?u=http%3A%2F%2Fwww.temple.edu%2Fprovost%2Fimages%2Ffaculty%2Fnot-pictured.jpg&f=1";
        return imageHref;
    }

    // Scrapes the description: the first paragraph on the event's page.
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
                    String description = "";
                    for (Element p : paragraphs) {
                        String d = p.text();
                        if(d.length() > description.length())
                            description = d;
                        eventdatabase.updateDescription(d, id);

                    }
                    eventdatabase.updateDescription(description, id);

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

    public String scrapeTime(Element t, String u, String title) throws IOException {
        Pattern pattern = Pattern.compile("([0-9]|0[0-9]|1[0-9]|2[0-3])(:|\\.)([0-5][0-9])");
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
                Element body =  doc.body();

                matcher = pattern.matcher(body.toString());
                if (matcher.find()) {
                    time = matcher.group();
                }
            }
        }
        if (time.charAt(2)=='.'){
            time = time.substring(0,1) + ':' + time.substring(3);
        }
        return time;
    }

    private String scrapeDate(final String url) throws IOException {

            String date = "";
            String u = url;


                // SELECT url FROM websites
            try {
                Document doc = Jsoup.connect(u).get();
                Element body = doc.selectFirst("body");

                Pattern pattern = Pattern.compile("(0?[1-9]|[12][0-9]|3[01])( |\\.) " +
                        "(?:Jan(?:uary)?|Feb(?:ruary)?|Mar(?:ch)?|Apr(?:il)|Jun(?:e)?|Jul(?:y)?|" +
                        "Aug(?:ust)?|Sep(?:tember)?|Oct(?:ober)?|Nov(?:ember)?|Dec(?:ember)? | januari?" +
                        "februari?|maart?|april?|mei?|juni?|juli?|augustus?|september?|oktober?|" +
                        "november?|december?)");

                Matcher matcher;
                matcher = pattern.matcher(body.toString());
                if (matcher.find()) {
                    date = matcher.group();
                }
            }
            catch (Exception e){
                    date = "error";
            }
            return date;
        }

        


}
