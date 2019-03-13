package com.example.whatsmyallergy;

import android.app.Application;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GlobalState extends Application {
    private Map<String, ArrayList<String>> calendarEntries;

    private String currentSeason;

    private double[] currentGlobalLocation;

    public String prevPostalCode;

    public HashMap<String, String[]> nearbyPostalCodes;

    private String locationName;

    private String apiLocationKey;

    private boolean locationSet;

    private String postalCode;

    private ArrayList<String> todaySymptoms;

    private boolean daily_symptoms_complete;

    private FiveDayForecast fiveDayForecast;

    public GlobalState() {
        locationSet = false;
        currentSeason = setSeason();

        postalCode = prevPostalCode = "92506"; // postalCode should be taken from profile, prevPostalCode is to look for new change in location
        // need to set latlng in MainActivity

        nearbyPostalCodes = new HashMap<>(); // "Zipcode": [lat, lng, serverity] nearby areas to get pollen counts for the map

        //settings --> location
        currentGlobalLocation = new double[] {0,0}; // TO DO: Should be taken from zipcode

        // Sample symptoms -- Should be taken from profile
        todaySymptoms = new ArrayList<>();
        todaySymptoms.add("watery_eyes");
        todaySymptoms.add("stuffy_nose");
        todaySymptoms.add("coughing");
        todaySymptoms.add("fatigue");
        todaySymptoms.add("itchy_throat");

        calendarEntries = new HashMap<String, ArrayList<String>>();
    }


    /**
     * Calendar
     */

    public Map<String, ArrayList<String>> getCalendarEntries(Map<String, ArrayList<String>> entry) {
        return entry;
    }

    public Map<String, ArrayList<String>> getGlobalCalendarEntries()
    {
        return calendarEntries;
    }


    public String calendarEntriesLength () {
        return (""+ calendarEntries.size());
    }

    //public Map<String, ArrayList<String>> setCalendarEntries(Map<String, ArrayList<String>> savedMap) {
    public void setCalendarEntries(Map<String, ArrayList<String>> savedMap) {
        calendarEntries.putAll(savedMap);
        System.out.println("Global state map = " + calendarEntries);
    }

    /**
     * APIs
     */

    public String getPostalCodeRequest() {
        String url = getResources().getString(R.string.accu_postalCodeApiURL);
        String apiKey = getResources().getString(R.string.accu_apikey);

        String requestURL = url+"?apikey="+apiKey+"&q="+postalCode;
        Log.d("Print", "PostalCode GET request URL: " + requestURL);
        return requestURL;
    }

    public String getForecastRequest() {
        String weatherAPI = getResources().getString(R.string.accu_5dayApiURL);
        String apiKey = getResources().getString(R.string.accu_apikey);

        String requestURL = weatherAPI+apiLocationKey+"?apikey="+apiKey+"&details=true";
        Log.d("Print", "Forecast request URL: " + requestURL);

        return requestURL;
    }


    /**
     * Symptoms and Forecast
     */

    public boolean checkDailySymptomsComplete() {
        return daily_symptoms_complete;
    }

    public void setDailySymptomsComplete(boolean complete) {
        daily_symptoms_complete = complete;
    }


    public void setFiveDayForecast(FiveDayForecast fiveDayForecast) {
        this.fiveDayForecast = fiveDayForecast;
    }

    public FiveDayForecast getFiveDayForecast() {
        return fiveDayForecast;
    }

    public String getTodayPollenStatus() {
        try {
            return fiveDayForecast.getDayN(0).getHighestPollutant().getSeverity();
        } catch (NullPointerException e) {
            Log.d("Print", "fiveDayForecast is empty");
        }
        return "NULL";
    }

    public String getCurrentSeason() {
        return currentSeason;
    }

    public String setSeason() {
        String seasons[] = {
                "winter", "winter",
                "spring", "spring", "spring",
                "summer", "summer", "summer",
                "fall", "fall", "fall",
                "winter"
        };
        Date date = new Date();
        return seasons[ date.getMonth() ];
    }

    public ArrayList<String> getTodaySymptoms() {
        return todaySymptoms;
    }


    /**
     * Postal Code
     */

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostalCode() {
        return postalCode;
    }


    /**
     * Lat/Lng Location
     */

    public void setCurrentGlobalLocation(double lat, double lng) {
        this.currentGlobalLocation[0] = lat;
        this.currentGlobalLocation[1] = lng;
    }

    public double[] getLatLong() {
        return currentGlobalLocation;
    }

    public void setApiLocationKey(String key) {
        apiLocationKey = key;
    }

    public boolean checkLocationIsSet() {
        return locationSet;
    }

    public void locationSet(boolean set) {
        locationSet = set;
    }

    public String getApiLocationKey() {
        return apiLocationKey;
    }

    public void setLocationName(String location) {
        this.locationName = location;
    }

    public String getLocationName() {
        return locationName;
    }


    /**
     * Map
     */

    public String lastMapRun = "";

    public String getLastRun() {
        return lastMapRun;
    }


}
