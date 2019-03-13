package com.example.whatsmyallergy;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.whatsmyallergy.MainActivity.globalState;

public class Suggestions {
    private Context mContext;
    private HashMap<String,ArrayList<String>> causeStrings;
    private HashMap<String,ArrayList<String>> suggestionStrings;
    private HashMap<String,ArrayList<String>> symptomStrings;

    public Suggestions(Context context) {
        mContext = context;
        causeStrings = new HashMap<>();
        suggestionStrings = new HashMap<>();
        symptomStrings = new HashMap<>();

        getSuggestionsFromFile("symptoms.json");
    }

    private void getSuggestionsFromFile(String path) {
        String json = "";
        try {
            InputStream is = mContext.getAssets().open(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null)
                json += line + "\n";
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Process JSON
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject suggestions = jsonObject.getJSONObject("suggestions");
            JSONObject causes = jsonObject.getJSONObject("causes");
            JSONObject symptoms = jsonObject.getJSONObject("symptoms");

            processSymptomsSuggestions(suggestions);
            processSymptomsCauses(causes);
            processSymptoms(symptoms);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void processSymptomsSuggestions(JSONObject jsonObject) {
        String[] types = {"seasonal", "hay_fever", "dust_mites", "pets"};
        String season = globalState.getCurrentSeason();

        try {
            // Seasonal
            ArrayList<String> seasonalSuggestions = new ArrayList<>();
            JSONArray seasonal = jsonObject.getJSONObject("seasonal").getJSONArray(season);
            for (int i = 0; i < seasonal.length(); ++i ) {
                seasonalSuggestions.add(seasonal.getString(i));
            }
            suggestionStrings.put(types[0], seasonalSuggestions);

            for (int i = 1; i < types.length; ++i){
                JSONArray hay_fever = jsonObject.getJSONArray(types[i]);
                ArrayList<String> suggestions = new ArrayList<>();
                for (int j = 0; j < hay_fever.length(); ++j ) {
                    suggestions.add(hay_fever.getString(j));
                }
                suggestionStrings.put(types[i], suggestions);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    private void processSymptomsCauses(JSONObject jsonObject) {
        String[] types = {"seasonal", "hay_fever", "dust_mites", "pets"};
        String season = globalState.getCurrentSeason();

        try {
            // Seasonal
            ArrayList<String> seasonalSuggestions = new ArrayList<>();
            JSONArray seasonal = jsonObject.getJSONObject("seasonal").getJSONArray(season);
            for (int i = 0; i < seasonal.length(); ++i ) {
                seasonalSuggestions.add(seasonal.getString(i));
            }
            causeStrings.put(types[0], seasonalSuggestions);

            for (int i = 1; i < types.length; ++i){
                JSONArray hay_fever = jsonObject.getJSONArray(types[i]);
                ArrayList<String> suggestions = new ArrayList<>();
                for (int j = 0; j < hay_fever.length(); ++j ) {
                    suggestions.add(hay_fever.getString(j));
                }
                causeStrings.put(types[i], suggestions);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void processSymptoms(JSONObject jsonObject) {
        String[] types = {"seasonal", "hay_fever", "dust_mites", "pets"};
        String season = globalState.getCurrentSeason();

        try {
            // Seasonal
            ArrayList<String> seasonalSuggestions = new ArrayList<>();
            JSONArray seasonal = jsonObject.getJSONArray("seasonal");
            for (int i = 0; i < seasonal.length(); ++i ) {
                String symptom = seasonal.getString(i);
                seasonalSuggestions.add(symptom);
            }
            symptomStrings.put(types[0], seasonalSuggestions);

            for (int i = 1; i < types.length; ++i){
                JSONArray suggestions = jsonObject.getJSONArray(types[i]);
                ArrayList<String> suggestionsList = new ArrayList<>();
                for (int j = 0; j < suggestions.length(); ++j ) {
                    String symptom = seasonal.getString(i);
                    suggestionsList.add(suggestions.getString(j));
                }
                symptomStrings.put(types[i], suggestionsList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getCurrentDaySuggestions() {
        String[] types = {"seasonal", "hay_fever", "dust_mites", "pets"};
        // return a list of current day suggestions
        ArrayList<String> symptoms = globalState.getTodaySymptoms();

        HashMap<String, Integer> counts = new HashMap<>();
        counts.put("seasonal", 0);
        counts.put("hay_fever", 0);
        counts.put("dust_mites", 0);
        counts.put("pets", 0);

        for (int i = 0; i < symptoms.size(); ++i) { // for each symptom
            String symptom = symptoms.get(i);
            counts.put("seasonal", counts.get("seasonal") + (symptomStrings.get("seasonal").contains(symptom)?1:0));
            counts.put("hay_fever", counts.get("hay_fever") + (symptomStrings.get("hay_fever").contains(symptom)?1:0));
            counts.put("dust_mites", counts.get("dust_mites") + (symptomStrings.get("dust_mites").contains(symptom)?1:0));
            counts.put("pets", counts.get("pets") + (symptomStrings.get("pets").contains(symptom)?1:0));
        }

        // find highest type
        String highestType = "seasonal";
        double highestValue = Double.MIN_VALUE;
        for (int i = 0; i < counts.size(); ++i) {
            double count = counts.get(types[i]);
            if (count > highestValue) {
                highestType = types[i];
                highestValue = count;
            }
        }

        globalState.setTodaySuggestion(suggestionStrings.get(highestType));
        // get suggestions of highest type
        return suggestionStrings.get(highestType);
    }
}
