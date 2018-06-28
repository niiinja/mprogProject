# Eventjes
## Description
This app is the solution for those who want to leave Facebook, but don't want to miss out on all the events of their favorite organizers.
With only a website's event page as input, eventjes collects upcoming events and provides the user with a calendar overview. 
The user can always have a handy overview of the events that they are interested in, by saving their favorite events.

### Screenshot
![](/doc/screenshotEventjes.png)



## Technical design
Eventjes makes use of the JSOUP library. The URLs of eventpages that are entered by the users are stored in the websites-table in an SQL database.
Then Scraper, using JSOUP, connects to this URL and searches for event titles in the HTML (assuming that the titles will be the most frequently occuring headers).
The Scraper then continues to scrape for an event's page-URL, date, time and image-URL by scraping the ancestors of the title elements, or the event's own webpage itself.
When all this information is collected, the Scraper creates an event object and stores it in the events-table in the database. The events are shown in a list in the CalendarActivity, sorted by date.
When a user clicks an event, it takes them to the DetailActivity, which shows the details and image (by using the Picasso library) of this event. The DetailActivity also triggers the Scraper to search for a description (the longest paragraph on the event-page).
An event can be stored as "saved" in the DetailActivity, so that it will show up in the CalendarActivity when the "show saved events" option is selected.

### Necessary for functionality
- JSOUP "a Java library for working with real-world HTML" https://jsoup.org/
- Picasso "a powerful image downloading and caching library for Android" https://square.github.io/picasso/
- SQLite database

### Datasources
Can in theory be any HTML/CSS website's event page. However, some websites will work better than others.

These are some websites to test this application with (in order of most to least satisfying output):
- subbacultcha.nl/events
- mu.nl/nl/about/agenda
- tolhuistuin.nl/agenda/
- tac.nu/programma
- ndsm.nl/evenementen
- radar.squat.net/events/city/Amsterdam
- meervaart.nl/theater/programma
- deschoolamsterdam.nl/nl/programma
- ruigoord.nl

### Classes and modules
CalendarActivity: displays all the (saved) events that are stored in the database.

CalendarAdapter: displays the events coming from the database as rows in a list in CalendarActivity.

DetailActivity: scrapes for an event's description, and displays the event details from the database.

EventDatabase: provides all the functionality of the SQL databases for both the website and the event tables.

EventEntry: class for the event entries.

Scraper: handles the scraping of the events, by first scraping a website's HTML for event titles, and then by looking for nearby dates, times, images and more.

SettingsActivity: users can manage their websites in this activity, which will be scraped for events once they are added.

SettingsAdapter: adapter for the SettingsActivity, displays rows of added websites.

WebsiteEntry: class for the website entries.

### Data
| websites |
| -------- |
|    id    |
|   URL    |

|  events  | 
| -------- | 
|    id    | 
|  title   | 
| organizer|
| date |
| time |
| datetime |
| imgurl |
| eventurl |
| description |
| saved |

## Challenges
Every website's HTML is quite different. Therefore i tried to build Eventjes with generic intuitive design conventions.
I have not hardcoded any scraping rules for specific websites. The consequence of that is that some websites have better results than others,
but also that the app is extensible to many different websites.
Sadly I was not able to scrape websites that make heavy use of javascript because the JSOUP library only scrapes HTML.
Other challenges that I encountered all had to do with the Android platform, and I was able to solve them all.
Regular Expressions
Multi-Threading

## Decisions
I decided to invest more time into generalizing the scraper rules, in order to get better results.
This meant that I had to drop the "filter events on event type" functionality that I had originally planned for my MVP.
I made this decision because the filtering did not seem to provide a much richer user experience, whilst generalizing the scraper rules would really improve the user experience.
Also, the scraper rulers seemed more technically interesting and new to me than the filtering option.

Due to a lack of time I focused my efforts on websites that were mainly HTML/CSS, and didn't put any energy into scraping javascript websites which I could have done with WebClient.
I did this because I worried that if I did also dive into javascript websites, I would get disappointing results on botch javascript and HTML/CSS websites, and would not have enough time to improve.
Therefore I decided to focus on HTML/CSS websites.

For UI decisions, I decided to ignore the material design conventions, because in my opinion such conventions create a homogenous world filled with superficial frivolity.
Instead, I researched brutalist web design which approaches digital design by embracing the medium's most raw and basic visual components, quite alike the brutalist architects and their love of raw concrete.
I aimed to design my UI along the lines of this vision, for more info and examples of brutalist design see: http://brutalistwebsites.com/

In an ideal world, with more time, I would build enable the Scraper to scrape javascript websites by use of WebClient. I would also implement the filter-on-type function that I neglected to build.
Another functionality that I would love to implement in the future would be a user-friendly option to tweak a website's scrape rules visually. This would work as the following:
When user enters an event page URL, the app shows them an example detailActivity, containing the data it has scraped for one event of this page. Then the user can select the
sections which are incorrect. The app wil then provide the user with other options for that section. The user then selects which option is the correct one, and the app saves the corresponding element attributes.
With these element attributes, the scraper can scrape for the correct sections for all other events of that specific eventpage.




