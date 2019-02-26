package com.example.whatsmyallergy;

import java.util.ArrayList;

public class FiveDayForecast {
    private ArrayList<Day> days;

    public FiveDayForecast(ArrayList<Day> days) {
        this.days = days;
    }

    public Day getDayN(int n) {
        return days.get(n);
    }
}
