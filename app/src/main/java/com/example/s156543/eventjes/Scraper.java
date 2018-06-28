package com.example.s156543.eventjes;

import android.app.Activity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.isDigit;

/**
 * The Scraper handles the scraping of the events, by first scraping a website's HTML for event
 * titles, and then by looking for nearby dates, times, images and more.
 */

public class Scraper {

    public Scraper() {
    }

    // The main scraping method which calls all other scraping methods
    // and adds the scraped events to the database
    public void scrapeForEvents(String u, EventDatabase eventdatabase, final Activity activity)
                                throws  UnknownHostException{
        final String url = u;
        final EventDatabase db = eventdatabase;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(url).get();
                    Elements titles = scrapeTitles(doc);

                    int count = 0;
                    for (Element t : titles) {
                        String imgUrl = findImg(t.parent().parent(), url);
                        if(imgUrl == null){
                            Element body = doc.body();
                            imgUrl = findImg(body, url);

                            // Show image of "no image available" when no image could be found
                            if(imgUrl == null)
                                imgUrl = "https://proxy.duckduckgo.com/iu/?u=" +
                                        "http%3A%2F%2Fwww.temple.edu%2Fprovost%2Fimages%" +
                                        "2Ffaculty%2Fnot-pictured.jpg&f=1";
                        }

                        String org = getOrganizer(url);
                        String date = scrapeDate(t.parent());

                        String u = null;
                        try{
                            u = t.parent().selectFirst("a[href]").attr("abs:href");
                            }
                        catch(Exception e){}

                        if (date == null) {
                            try{
                            Document eventDoc = Jsoup.connect(u).get();
                            Element body = eventDoc.selectFirst("body");
                            date = scrapeDate(body);
                            }

                            // Assigning "31/12" to events without a date, places them at the
                            // end of the calendar.
                            catch(Exception e){
                                date = "31/12";
                            }
                        }

                        // Stop scraping events that have already taken place
                        if(date == "bad")
                            continue;

                        String time = scrapeTime(t, u);

                        java.sql.Date sqlDate = null;
                        try {
                            sqlDate = parseDate(date, time);
                        }
                        catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // Create eventobject and store it in the database
                        EventEntry eventEntry = new EventEntry(org, t.text(), time,
                        date, sqlDate, imgUrl, u, "no description", false);
                        db.insertEvent(eventEntry);

                        // Scrape max. 100 events of a website
                        count += 1;
                        if(count >= 100) break;
                    }
                }
                catch (IOException e) {
                } catch (ParseException e) {
                    e.printStackTrace();
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

    // Scrapes for titles, assuming that the event title will be in the most frequent header type
    private Elements scrapeTitles(Document doc){
        Elements titles;
        Elements h1s = doc.select("h1");
        Elements h2s = doc.select("h2");
        Elements h3s = doc.select("h3");
        Elements h4s = doc.select("h4");

        titles = h1s;

        if (h2s.size() > titles.size())
            titles = h2s;

        if (h3s.size() > titles.size())
            titles = h3s;

        if (h4s.size() > titles.size())
            titles = h4s;

        return titles;
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

    // Selects the image in the parent of the parent of the title's element,
    // or if that fails in the entire <body>
    private String findImg(Element element, String url) {
        Element image = element.select("img").first();
        String imageHref = null;

        if (image != null) {
            imageHref = image.attr("src");

            if(imageHref.charAt(0) == '/') {
                int indexDot = url.indexOf('.');
                String pastDot = url.substring(indexDot, url.length());
                int indexSlash = indexDot + pastDot.indexOf('/');

                imageHref = url.substring(0, indexSlash) + imageHref;
                }
            }

        return imageHref;
    }

    // Scrapes the description, assuming it is the largest paragraph on the event's page.
    public void scrapeDescription(String eventUrl, final EventDatabase eventdatabase,
                        long ID, DetailActivity act) {
        final String url = eventUrl;
        final DetailActivity activity = act;
        final EventDatabase db = eventdatabase;
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

    // Scrapes the time, by searching for a hh:mm pattern nearby the event title
    public String scrapeTime(Element t, String u) throws IOException {
        Pattern pattern = Pattern.compile("([0-9]|0[0-9]|1[0-9]|2[0-3])(:|\\.)([0-5][0-9])");
        String time = "00:00";
        Element parent = t.parent();
        Matcher matcher;
        matcher = pattern.matcher(parent.toString());

        if (matcher.find()) {
            time = matcher.group();
        }
        else {
            parent = parent.parent();
            matcher = pattern.matcher(parent.toString());

            if (matcher.find())
                time = matcher.group();
            else{
                Document doc = Jsoup.connect(u).get();
                Element body =  doc.body();
                matcher = pattern.matcher(body.toString());

                if (matcher.find())
                    time = matcher.group();
            }
        }
        if(time.length() <= 4)
            time = "0"+time;

        time = time.substring(0,2) + ':' + time.substring(3);
        return time;
    }

    // Scrapes the date by searching for a dd/mm pattern nearby the title or on the eventpage
    private String scrapeDate(Element element) throws ParseException {
            String date = null;
            Element body = element;
            Matcher matcher;

            Pattern pattern = Pattern.compile("(0?[1-9]|[12][0-9]|3[01])\\D" +
                    "(?:Jan(?:uary)?|Feb(?:ruary)?|Mar(?:ch)?|Apr(?:il)?|May?|Jun(?:e)?|Jul(?:y)?|" +
                    "Aug(?:ust)?|Sep(?:tember)?|Oct(?:ober)?|Nov(?:ember)?|Dec(?:ember)? | januari?" +
                    "februari?|maart?|april?|mei?|juni?|juli?|augustus?|september?|oktober?|" +
                    "november?|december?|" +
                    "JAN?|FEB?|MRT?|APR?|MEI?|JU(?:N)?|JU(?:L)?|AUG?|SEP?|OKT?|NOV?|DEC?)");

            matcher = pattern.matcher(body.toString());

            if (matcher.find()) {
                date = matcher.group();

                // Convert the date string into a general format
                if (!isDigit(date.charAt(1)))
                    date = '0' + date;

                date = date.substring(0, 2) + '/' + date.substring(3);
                String days = date.substring(0, 3);
                String m = date.substring(3);
                m = m.toLowerCase();

                if (m.equals("jan")|| m.equals("january")|| m.equals("januari")){
                    date = days + "01";
                }
                else if (m.equals("feb")|| m.equals("february")|| m.equals("februari")){
                    date = days + "02";
                }
                else if (m.equals("mar")|| m.equals("mrt")|| m.equals("march")|| m.equals("maart")){
                    date = days + "03";
                }
                else if (m.equals("apr")|| m.equals("april")){
                    date = days + "04";
                }
                else if (m.equals("may")|| m.equals("mei")){
                    date = days + "05";
                }
                else if (m.equals("jun")|| m.equals("june")|| m.equals("juni")) {
                    date = days + "06";
                }
                else if (m.equals("jul")|| m.equals("july")| m.equals("juli")) {
                    date = days + "07";
                }
                else if (m.equals("aug")|| m.equals("august")|| m.equals("augustus")) {
                    date = days + "08";
                }
                else if (m.equals("sep")|| m.equals("september")) {
                    date = days + "09";
                }
                else if (m.equals("oct")|| m.equals("october")|| m.equals("oktober")
                        ||m.equals("okt")){
                    date = days + "10";
                }
                else if (m.equals("nov")|| m.equals("november")){
                    date = days + "11";
                }
                else if (m.equals("dec")|| m.equals("december")) {
                    date = days + "12";
                }
            }

        // Compare event date to current date, to determine if the event has already taken place
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dt = date + "/" + "2018";
        LocalDate localDate = LocalDate.now();
        String localdatetime = String.valueOf(localDate);
        localdatetime = localdatetime.substring(8, 10) + "/" + localdatetime.substring(5,7) + "/"
                + localdatetime.substring(0,4);

        try{
        java.util.Date localDateTime = sdf.parse(localdatetime);
        java.util.Date dateTime = sdf.parse(dt);

        if(localDateTime.after(dateTime))
            return "bad";
        }
        catch (ParseException p){
        }

        return date;
    }

    // Convert date and time into a SQL date object, to enable sorting the database on datetime
    public static java.sql.Date parseDate(String date, String time) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM hh:mm");
        String dt = date + " " + time;
        java.util.Date dateTime = sdf.parse(dt);

        java.sql.Date sqldate = new java.sql.Date(dateTime.getTime());
        return  sqldate;
    }
}
