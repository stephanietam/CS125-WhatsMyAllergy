package com.example.whatsmyallergy;

import android.app.Application;
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
    private boolean daily_symptoms_complete;

    private String apiLocationKey;

    private boolean locationSet;

    private String postalCode;

    private FiveDayForecast fiveDayForecast;

    private String locationName;

    private Map<String, ArrayList<String>> calendarEntries = new HashMap<String, ArrayList<String>>();

    public Map<String, ArrayList<String>> getCalendarEntries(Map<String, ArrayList<String>> entry)
    {
        return entry;
    }

    public String calendarEntriesLength () {
        return (""+ calendarEntries.size());
    }

    private String currentSeason;

    private ArrayList<String> todaySymptoms;

    private double[] currentGlobalLocation;

    public String prevPostalCode;

    public ArrayList<String[]> nearbyPostalCodes;

    public GlobalState() {
        locationSet = false;
        currentSeason = setSeason();

        postalCode = prevPostalCode = "92506"; // postalCode should be taken from profile
        // prevPostalCode is to look for new change in location

        nearbyPostalCodes = new ArrayList<String[]>(); // nearby areas to get pollen counts for the map

        //settings --> location
        currentGlobalLocation = new double[] {0,0}; // TO DO: Should be taken from profile

        // Sample symptoms -- Should be taken from profile
        todaySymptoms = new ArrayList<>();
        todaySymptoms.add("watery_eyes");
        todaySymptoms.add("stuffy_nose");
        todaySymptoms.add("coughing");
        todaySymptoms.add("fatigue");
        todaySymptoms.add("itchy_throat");
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

    public boolean checkDailySymptomsComplete() {
        return daily_symptoms_complete;
    }

    public void setDailySymptomsComplete(boolean complete) {
        daily_symptoms_complete = complete;
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

    public void setFiveDayForecast(FiveDayForecast fiveDayForecast) {
        this.fiveDayForecast = fiveDayForecast;
    }

    public FiveDayForecast getFiveDayForecast() {
        return fiveDayForecast;
    }

    public int getTodayPollenCount() {
        try {
            return fiveDayForecast.getDayN(0).getHighestPollutant().getValue();
        } catch (NullPointerException e) {
            Log.d("Print", "fiveDayForecast is empty");
        }
        return 0;
    }

    public String getCurrentSeason() {
        return currentSeason;
    }

    public ArrayList<String> getTodaySymptoms() {
        return todaySymptoms;
    }


    /* API */

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


    /* Postal Code */

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String setPostalCode() {
        String postalCode = getPostalCode();
        double[] latLong = getLatLong();
        Geocoder gc = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = gc.getFromLocation(latLong[0],latLong[1],1);
            if (addresses.size()>=1) {
                postalCode = addresses.get(0).getPostalCode();
                setPostalCode(postalCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.postalCode = postalCode;
        return postalCode;
    }


    /* Latitude and Longitude */

    public void setCurrentGlobalLocation(double lat, double lng) {
        this.currentGlobalLocation[0] = lat;
        this.currentGlobalLocation[1] = lng;
        setPostalCode();
    }

    public double[] getLatLong() {
        return currentGlobalLocation;
    }

    public void setLatLong(double lat, double lng) {
        currentGlobalLocation[0] = lat;
        currentGlobalLocation[1] = lng;
    }


}
