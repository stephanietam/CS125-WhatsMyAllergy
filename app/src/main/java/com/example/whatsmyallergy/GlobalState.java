package com.example.whatsmyallergy;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;

public class GlobalState extends Application {
    private boolean daily_symptoms_complete;

    private String apiLocationKey;

    private boolean locationSet;

    private String postalCode;

    private FiveDayForecast fiveDayForecast;

    private String locationName;

    private String currentSeason;

    private ArrayList<String> todaySymptoms;

    public GlobalState() {
        locationSet = false;
        postalCode = "92506";
        currentSeason = "winter";
        todaySymptoms = new ArrayList<>();

        // Sample symptoms
        todaySymptoms.add("watery_eyes");
        todaySymptoms.add("stuffy_nose");
        todaySymptoms.add("coughing");
        todaySymptoms.add("fatigue");
        todaySymptoms.add("itchy_throat");
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

    public String getCurrentSeason() {
        return currentSeason;
    }

    public ArrayList<String> getTodaySymptoms() {
        return todaySymptoms;
    }
}
