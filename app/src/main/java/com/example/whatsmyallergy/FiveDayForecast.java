package com.example.whatsmyallergy;

import java.util.ArrayList;

class DaysOfWeek {
    public static String Sunday = "Sunday";
    public static String Monday = "Monday";
    public static String Tuesday = "Tuesday";
    public static String Wednesday = "Wednesday";
    public static String Thursday = "Thursday";
    public static String Friday = "Friday";
    public static String Saturday = "Saturday";
}

public class FiveDayForecast {
    private ArrayList<Day> days;

    public FiveDayForecast(ArrayList<Day> days) {
        this.days = days;
    }

    public Day getDayN(int n) {
        return days.get(n);
    }

    public String[] getDaysOfWeek() {
        String[] results = new String[7];
        Day today = getDayN(0);
        int todayNum = today.getDay();

        for (int i = 0; i < results.length; ++i) {
            int dayNum = (todayNum+i)%7;
            switch(dayNum) {
                case 0:
                    results[i] = DaysOfWeek.Sunday;
                    break;
                case 1:
                    results[i] = DaysOfWeek.Monday;
                    break;
                case 2:
                    results[i] = DaysOfWeek.Tuesday;
                    break;
                case 3:
                    results[i] = DaysOfWeek.Wednesday;
                    break;
                case 4:
                    results[i] = DaysOfWeek.Thursday;
                    break;
                case 5:
                    results[i] = DaysOfWeek.Friday;
                    break;
                case 6:
                    results[i] = DaysOfWeek.Saturday;
                    break;
                default:
                    break;
            }
        }
        // results[0] = "Today";
        // [1] = "Tomorrow";

        return results;
    }
}
