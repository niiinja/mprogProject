# Project mprog 
Nina Boelsums

The goal of this mobile application is to provide users with an overview of the upcoming events of their favourite organizers.

## Problem statement
Since the Cambridge Analytica scandal, a large number of people want to leave Facebook. However, many of them feel forced to stay because they depend on it for the events and parties. They will miss one place where they can find all the upcoming events of their liked venues, because they don’t want to spend a long time visiting all the individual websites. This causes people to stay on Facebook unwillingly, or to become somewhat socially isolated when they leave.

## Solution
An application on which the user can find a collection of all the upcoming events of their favourite venues.

## Visual representation
![](/doc/finalUI.png)

## Main features and MVP
-	Add/delete websites of event organizers
-	Browse through all upcoming events of these organizers, sorted on date
-	View details such as title, time, date image and description on event
-	Store an event as saved
-	View the collection of saved events in the calendar

### Optional future features
-	Select events on event-type
-	Enable user-friendly and visual tweaking of the web-scraping rules.

## Prerequisites
Datasources: Various websites of venues. Their event pages will be in html and the content of these pages must be transformed from html strings to values of event object parameters such as “date” “location” “eventName” etc.

External components: Eventjes screen scrapes event pages of various websites using Jsoup https://jsoup.org/ Jsoup is a java library for parsing, extracting and manipulating real-world HTML

Similar mobile apps: At the moment people use social media like Facebook(and to some extends Twitter and Instagram) to find out about events. These platforms require the organizers to create an account and actively post their events on there. Before social media there was the RSS feed, which is still used by some to read the news. RSS also requires the organizer to take action: they need to add an RSS feed to their website. There currently aren’t any mobile apps that collect events without any extra effort from the organizers .

Hardest parts: Every HTML page is formatted differently. The hardest part is to tweak the scraping and transforming of the data in such a way that the outputs will be legible and for the most part comparable to the results coming from other websites.

