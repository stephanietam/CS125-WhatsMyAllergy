# CS125-WhatsMyAllergy
What's My Allergy application

What’s My Allergy is an Android mobile application that allows users to record their daily symptoms and provides suggestions that will help them reduce their pollen intake. The application is beneficial to anyone who is allergic to pollen, dust mites, or pets. What’s My Allergy will also provide daily and 5-day pollen counts for areas around the user. Pollen exists year round and many people are affected. With our application, we hope to notify users when pollen counts are high in their area and to provide users with information to help them live healthier lives.

Files:

AccountInfo
  - A class that collects information such as their name, date of birth and known allergy information and adds it to the Firebase Database.

AccuWeatherApi
- A class that contains AsyncTask functions which calls the AccuWeather APIs and updates the home page UI with forecasts.

CalendarPage

Day
- A class that contains properties regarding the date and the pollen that occurs on this day. 

FiveDayForecast
- A class that contains a list of five Day objects and a function that determines the days of the week. 

GeoNamesApi
- A class that contains AsyncTask functions which calls the GeoNames API and the BreezoMeter API. It also adds markers to the maps page.

GlobalState
- A class which contains all the properties and functions that need to be used throughout the application. An object of the class is stored with the application and it saves the stored variables to use for all pages.

MainActivity 
- Class that implements the home page. It calls an AsyncTask using the AccuWeatherApi class. Forecasts and suggestions are implemented in this class.

MapPage
- Class that implements the map page. It uses Google maps with the user's location, and calls an AsyncTask using the GeoNamesApi class to create markers with nearby forecasts.  

MySingleton
- A supporting class that is used to obtain the current GPS location of the user. 

NotificationUtils
- A file containing the notification class utilized within the app. Notifications are designed to appear in their own channel and only the most recent notification sent in the app is indicated to the user.

pieChart
- Class that contains the pie chart graph. The class utilizes implementation from "https://github.com/PhilJay/MPAndroidChart".

Pollen
- A class that contains properties of pollen such as name, value, category, and category value. 

ProfilePage 
- A class where the information user can view their account informantion that they inputted during their signup.

SettingsPage
- Class where basic account information is stored and location and  notifications may be turned on.

SignupActivity
- Activity where users enter an email address and password to be registered with firebase. 

Suggestions
- Class that takes in a json file to extract suggestion string. It contains functions that can be used to retrieve suggestions and compare symptoms. 

Users
- Helper class that allows the system to know what is a user and what their profile looks like. This structure os also used when a user is being added to the Firebase Database. It also contains functions that are useful in checking for valid user information.  
