package com.example.whatsmyallergy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.util.ArrayList;


public class ProfilePage extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(ProfilePage.this, MainActivity.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_map:
                    intent = new Intent(ProfilePage.this, MapPage.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_calendar:
                    intent = new Intent(ProfilePage.this, CalendarPage.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(ProfilePage.this, SettingsPage.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        setTitle("Profile");

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        MenuItem menuItem = navigation.getMenu().getItem(1);
        menuItem.setChecked(true);

        ListView knownAllergy = findViewById(R.id.allergyListView);
        String[] values = new String[] { "Ragweed", "Mold", "Pets",
                        "Grass"};

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        knownAllergy.setAdapter(adapter);
        ListView familyHistory = findViewById(R.id.familyHistoryListView);
        familyHistory.setAdapter(adapter);


        //PIE CHART

        PieChart pieChart = findViewById(R.id.piechart);
        ArrayList NoOfSymptoms = new ArrayList();

        NoOfSymptoms.add(new Entry(12, 0));
        NoOfSymptoms.add(new Entry(4f, 1));
        NoOfSymptoms.add(new Entry(7f, 2));
        NoOfSymptoms.add(new Entry(9f, 3));
        NoOfSymptoms.add(new Entry(1f, 4));
        NoOfSymptoms.add(new Entry(12, 5));
        NoOfSymptoms.add(new Entry(4f, 6));
        NoOfSymptoms.add(new Entry(22f, 7));
        NoOfSymptoms.add(new Entry(9f, 8));
        NoOfSymptoms.add(new Entry(11f, 9));
        NoOfSymptoms.add(new Entry(1f, 10));
        PieDataSet dataSet = new PieDataSet(NoOfSymptoms, "Symptoms Per MONTH NAME");

        ArrayList symptoms = new ArrayList();

        symptoms.add("Runny Nose");
        symptoms.add("Watery Eyes");
        symptoms.add("Sneezing");
        symptoms.add("Coughing");

        symptoms.add("Itchy Eyes and Nose");
        symptoms.add("Dark Circles Under Eyes");
        symptoms.add("Inflamed Nasal Passages");
        symptoms.add("Itchy Throat and Mouth");
        symptoms.add("Skin Reactions");
        symptoms.add("Ear Pressure");
        symptoms.add("Fatigue");


        PieData data = new PieData(symptoms, dataSet);
        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(5000, 5000);

        /////
    }




}
