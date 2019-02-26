package com.example.whatsmyallergy;

import android.app.Application;

public class GlobalState extends Application {
    private boolean daily_symptoms_complete;

    private String apiLocationKey;

    private boolean locationSet;

    private FiveDayForecast fiveDayForecast;

    private String location;

    public GlobalState() {
        locationSet = false;
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

    public void setLocationString(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setFiveDayForecast(FiveDayForecast fiveDayForecast) {
        this.fiveDayForecast = fiveDayForecast;
    }

    public FiveDayForecast getFiveDayForecast() {
        return fiveDayForecast;
    }
}
