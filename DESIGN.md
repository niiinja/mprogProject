# Design doc
## UI design, module+class+function diagrams
![](/doc/UIdesign.png)

## Needed for functionality
- Volley 
- Jsoup
- SQLite

## Datasources
These will be the event pages of various websites.
Transforming the data from HTML to legible information will be done by following intuitive design conventions.
For example: an event title will often start with <h... and its heading type will be the most frequent heading on the page. An event's ticket price will often start with €.. and its time is usually in the format of 00:00. The location name of the event can be derived from the domain. Further tweaking is necessary to arrive at the optimal method of the data transformation.

## Database tables + fields
Events
- id
- title
- location
- date
- time
- price
- type

Websites
- id
- URL
- type
