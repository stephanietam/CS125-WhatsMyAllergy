package com.example.whatsmyallergy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.HashMap;
import android.widget.CalendarView;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;


public class CalendarPage extends AppCompatActivity {

    CalendarView symptomCalendar;
    private TextView mTextMessage;
    //ArrayList<String> daySymptoms = new ArrayList<String>();
    Map<String, Integer> symptomMap = new HashMap<>();

    private int mDay;
    private int mMonth;
    private int mYear;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(CalendarPage.this, MainActivity.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_profile:
                    intent = new Intent(CalendarPage.this, ProfilePage.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_map:
                    intent = new Intent(CalendarPage.this, MapPage.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(CalendarPage.this, SettingsPage.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_page);

        //initialize symptoms
        //symptomMap.put("Date",)
        symptomMap.put("Runny Nose",0);
        symptomMap.put("Watery Eyes",0);
        symptomMap.put("Sneezing",0);

        //Calendar
        symptomCalendar = (CalendarView)findViewById(R.id.calendarView);
        symptomCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String date = month+1 + "/" + dayOfMonth + "/" + year;
                System.out.println(date);
                //if selected date < current date display the checkbox values for that date key

                //else init hashmap and add symptoms for that date key
            }
        });

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        MenuItem menuItem = navigation.getMenu().getItem(3);
        menuItem.setChecked(true);
    }

    //Checkbox selected
    public void selectSymptom(View view){
        boolean checked = ((CheckBox)view).isChecked();
        switch(view.getId())
        {
            case R.id.Symptom_runnyNose:
                if(checked)
                {
                    symptomMap.put("Runny Nose",1);
                    //daySymptoms.add("Runny Nose");
                    ((CheckBox)view).setChecked(true);
                }
            case R.id.Symptom_wateryEyes:
                if(checked)
                {
                    symptomMap.put("Watery Eyes",1);
                    ((CheckBox)view).setChecked(true);
                    //daySymptoms.add("Watery Eyes");
                }

            case R.id.Symptom_sneezing:
                if(checked)
                {
                    symptomMap.put("Sneezing",1);
                    ((CheckBox)view).setChecked(true);
                }
        }
    }

    //Submit button clicked
    public void onSubmitClicked(View view) {
        System.out.println("Elements of ArrayList of String Type: "+symptomMap);
    }

}

