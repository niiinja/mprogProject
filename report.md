# Eventjes
## Description
This app is the solution for those who want to leave facebook, but don't want to miss out on all the events of their favorite organizers.
With only a website's event page as input, eventjes collects upcoming events and provides the user with a calendar overview. 
The user can save the events that they are interested in.

![](/doc/screenshotEventjes.png)

Some websites to test this application with:
- subbacultcha.nl/events
- mu.nl/nl/about/agenda
- tolhuistuin.nl/agenda/
- tac.nu/programma
- ndsm.nl/evenementen 
- radar.squat.net/events/city/Amsterdam
- meervaart.nl/theater/programma
- deschoolamsterdam.nl/nl/programma
- ruigoord.nl


## Technical design
Eventjes makes use of the JSOUP library
Picasso

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
Sadly I was not able to scrape websites that make heavy use of javascript because the JSOUP libabry only scrapes HTML.

## Decisions
I decided to invest more time into generalizing the scraper rules, in order to get better results.
This meant that I had to drop the "filter events on event type" functionality that I had originally planned for my MVP.
I made this decision because the filtering did not seem to provide a much richer user experience, whilst generalizing the scraper rules would really improve the user experience.
Also, the scraper rulers seemed more technically interesting and new to me than the filtering option.

In an ideal world, with more time, I would build enable the Scraper to scrape javascript websites by use of WebClient. I would also implement the filter-on-type function that I neglected to build.
Another functionality that I would love to implement in the future would be a user-friendly option to tweak a website's scrape rules visually. This would work as the following:
When user enters an event page URL, the app shows them an example detailActivity, containing the data it has scraped for one event of this page. Then the user can select the
sections which are incorrect. The app wil then provide the user with other options for that section. The user then selects which option is the correct one, and the app saves the corresponding element attributes.
With these element attributes, the scraper can scrape for the correct sections for all other events of that specific eventpage.




