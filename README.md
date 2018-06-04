# mprogProject

## problem statement
Since the Cambridge Analytica scandal, a large number of people want to leave Facebook. However, many of them feel forced to stay because they depend on it for the events and parties. They will miss one place where they can find all the upcoming events of their liked venues, because they don’t want to spend a long time visiting all the individual websites. This causes people to stay on Facebook unwillingly, or when they leave, to become somewhat socially isolated.

## solution
An application on which the user can find a collection of all the upcoming events of their favourite venues.

## main features and MVP
-	Add/delete websites of venues from which the user wants event updates
-	Browse through all upcoming events of the user’s selected venues
-	Select events on event-type
-	View details on event
-	Save event in event collection calendar
-	View event collection calendar

### optional features
-	Browse private parties hosted by friends and others
-	Create URL where user can share personal events
-	Export event calendar to daily calendar

## prerequisites
Datasources: Various websites of venues. Their event pages will be in html and the content of these pages must be transformed from html strings to values of event object parameters such as “date” “location” “eventName” etc.

External components: I will be screen scraping on event pages of various websites using Jsoup https://jsoup.org/ Jsoup is a java library for parsing, extracting and manipulating real-world HTML

Similar mobile apps: Calendars with RSS feeds? Facebook 

Hardest parts: Every html page will be formatted differently, the hardest part will be to tweak the scraping and transforming in such a way that its output will be legible and for the most part comparable to the results coming from other websites.

