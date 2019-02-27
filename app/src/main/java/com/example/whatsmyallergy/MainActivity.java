package com.example.whatsmyallergy;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    static GlobalState globalState;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    intent = new Intent(MainActivity.this, ProfilePage.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_map:
                    intent = new Intent(MainActivity.this, MapPage.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_calendar:
                    intent = new Intent(MainActivity.this, CalendarPage.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(MainActivity.this, SettingsPage.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        globalState = (GlobalState)getApplication();
        updateAfterSymptomsComplete();

        // Getting location key
        if (!globalState.checkLocationIsSet()) { // OR location is different
            AsyncTask asyncTask = new AccuWeatherApi(this).execute();
        }

        // Waiting for symptoms button click
        Button symptoms_button = (Button) findViewById(R.id.symptoms_button);
        symptoms_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalState.setDailySymptomsComplete(true);
                Intent intent = new Intent(MainActivity.this, CalendarPage.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });

        // Bottom Navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void updateAfterSymptomsComplete() {
        Button button_constraint = findViewById(R.id.symptoms_button);
        ScrollView scroll_constraint = findViewById(R.id.symptoms_scroll);
        TextView suggestion_title = findViewById(R.id.suggestions_title);

        if (globalState.checkDailySymptomsComplete()) {
            button_constraint.setVisibility(View.INVISIBLE);
            scroll_constraint.setVisibility(View.VISIBLE);
            suggestion_title.setVisibility(View.VISIBLE);

        } else {
            button_constraint.setVisibility(View.VISIBLE);
            scroll_constraint.setVisibility(View.INVISIBLE);
            suggestion_title.setVisibility(View.INVISIBLE);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void processForecastFromFile(String path) {
        String json = "";
        try {

            InputStream is = getAssets().open(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null)
                json += line + "\n";
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray dailyForecastsJSON = jsonObject.getJSONArray("DailyForecasts");

            ArrayList<Day> fiveDays = new ArrayList<>();

            /// PROCESS JSON
            for (int i = 0; i < dailyForecastsJSON.length(); ++i) {
                JSONArray pollens = dailyForecastsJSON.getJSONObject(i).getJSONArray("AirAndPollen");

                ArrayList<Pollen> pollenList = new ArrayList<>();
                for (int j = 0; j < pollens.length(); ++j) {
                    JSONObject pollen = pollens.getJSONObject(j);
//                    Log.d("Print",pollen.toString());
                    pollenList.add(new Pollen(pollen.getString("Name"),
                                                Integer.parseInt(pollen.getString("Value")),
                                                pollen.getString("Category"),
                                                pollen.getString("CategoryValue")));

                }

//                for (int j= 0; j < pollenList.size(); ++j) {
//                    Log.d("Print", pollenList.get(j).getName());
//                }


                String dateString = dailyForecastsJSON.getJSONObject(i).getString("Date").substring(0,10);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(dateString, formatter);
                Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//                Log.d("Print",date.toString());

                Day day = new Day(date, pollenList);
                fiveDays.add(day);

            }

            globalState.setLocationName("San Francisco, CA");
            FiveDayForecast fiveDayForecast = new FiveDayForecast(fiveDays);
            globalState.setFiveDayForecast(fiveDayForecast);
            ///
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FiveDayForecast s = globalState.getFiveDayForecast();
    }
}
