package com.example.whatsmyallergy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static GlobalState globalState;
    private Suggestions suggestions;

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
        setTitle("Home");

        globalState = (GlobalState)getApplication();


        if (globalState.checkDailySymptomsComplete()) {
            suggestions = new Suggestions(this);
            setSuggestionsView();
            updateAfterSymptomsComplete();
        }

        // Getting location key
        if (!globalState.checkLocationIsSet()) { // OR location is different
//            AsyncTask asyncTask = new AccuWeatherApi(this).execute();
        } else {
            setTextViews();
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

    private void setSuggestionsView() {
        runOnUiThread(new Runnable() {
            public void run() {
                TextView suggestionsView = findViewById(R.id.symptoms_text);
                String suggestionText = "";
                ArrayList<String> suggestionsList = suggestions.getCurrentDaySuggestions();
                for (int i = 0; i < suggestionsList.size(); ++i) {
                    suggestionText += suggestionsList.get(i) + "\n";
                }
                suggestionsView.setText(suggestionText);
            }
        });
    }

    private void setTextViews() {
        runOnUiThread(new Runnable() {
            public void run() {
                FiveDayForecast fiveDayForecast = globalState.getFiveDayForecast();

                Pollen highestPollen = globalState.getFiveDayForecast().getDayN(0).getHighestPollutant();

                TextView pollenValue = (TextView) findViewById(R.id.count_text);
                pollenValue.setText(String.valueOf(highestPollen.getValue()));

                TextView pollenCategory = (TextView) findViewById(R.id.severity_text);
                pollenCategory.setText(highestPollen.getCategory());

                TextView pollenName = (TextView) findViewById(R.id.name_text);
                pollenName.setText(highestPollen.getName());

                TextView locationText = (TextView) findViewById(R.id.location_text);
                locationText.setText(globalState.getLocationName());

                TextView severityText = (TextView) findViewById(R.id.severity_text);
                severityText.setText(highestPollen.getSeverity());

                TextView[] dayOne = new TextView[] {
                        findViewById(R.id.day_one_label),
                        findViewById(R.id.day_one_count)};
                TextView[] dayTwo = new TextView[] {
                        findViewById(R.id.day_two_label),
                        findViewById(R.id.day_two_count)};
                TextView[] dayThree = new TextView[] {
                        findViewById(R.id.day_three_label),
                        findViewById(R.id.day_three_count)};
                TextView[] dayFour = new TextView[] {
                        findViewById(R.id.day_four_label),
                        findViewById(R.id.day_four_count)};
                TextView[] dayFive = new TextView[] {
                        findViewById(R.id.day_five_label),
                        findViewById(R.id.day_five_count)};

                TextView[][] week = new TextView[][]{ dayOne, dayTwo, dayThree, dayFour, dayFive };

                for (int i = 0; i < week.length; ++i) {
                    TextView[] day = week[i];
                    day[0].setText(fiveDayForecast.getDaysOfWeek()[i]);

                    int count = fiveDayForecast.getDayN(i).getHighestPollutant().getValue();
                    day[1].setText(count+""); // count
                }
            }
        });
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
}

