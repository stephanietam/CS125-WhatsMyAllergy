package com.example.whatsmyallergy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Day {
    private Date date;
    private HashMap<String, Pollen> pollens;

    public Day(Date date, ArrayList<Pollen> pollenList) {
        pollens = new HashMap<>();

        this.date = date;

        for (int i = 0; i < pollenList.size(); ++i) {
            pollens.put(pollenList.get(i).getName(), new Pollen(pollenList.get(i)));
        }
    }

    public int getDay() {
        return date.getDay();
    }

    public Pollen getHighestPollutant() {
        Pollen highest = pollens.get("Grass");
        int highestValue = 0;
        for (Pollen pollen: pollens.values()) {
            if (pollen.getValue()>highestValue) {
                highestValue = pollen.getValue();
                highest = pollen;
            }
        }
        return highest;
    }

    public HashMap<String, Pollen> getPollens() {
        return pollens;
    }

}
