# Eventjes
## Description
This app is the solution for those who want to leave facebook, but don't want to miss out on all the events of their favorite organizers.
With only a website's event page as input, eventjes collects upcoming events and provides the user with a calendar overview. 
The user can save the events that they are interested in.

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
Every website's html is quite different. Therefore eventjes tries to make use of generic intuitive design conventions. 
However, these are not always sufficient. Because I was interested in making 

## Decisions
I decided to invest more time into generalizing the scraper rules, in order to get better results , and because it didn't seem to add much to the functionality, I decided to not implement a filtering-on-event type functionalty.


