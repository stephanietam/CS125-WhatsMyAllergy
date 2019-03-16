package com.example.whatsmyallergy;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Button;
import java.util.HashMap;
import android.widget.CalendarView;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import static com.example.whatsmyallergy.MainActivity.globalState;


public class CalendarPage extends AppCompatActivity {

    private NotificationUtils mNotificationUtils;


    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String uid;

    CalendarView symptomCalendar;
    private TextView mTextMessage;
    String currentDate;
    String date;
    ArrayList<String> daySymptoms = new ArrayList<String>();
    Map<String, ArrayList<String>> symptomMap = new HashMap<String, ArrayList<String>>();
    String[] selectedDateArr;
    String [] currentDateArr;
    int currentMonth;
    int currentDay;
    int selectedMonth;
    int selectedDay;
    boolean olderDateSelected = false;
    List<String> symptomList = new ArrayList<>(Arrays.asList("Runny nose", "Watery eyes","Sneezing","Coughing","Itchy eyes and nose","Dark circles","Inflamed nasal passage","Itchy throat and mouth","Skin reactions","Ear pressure","Fatigue"));

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(CalendarPage.this, MainActivity.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_profile:
                    intent = new Intent(CalendarPage.this, ProfilePage.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_map:
                    intent = new Intent(CalendarPage.this, MapPage.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(CalendarPage.this, SettingsPage.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mNotificationUtils = new NotificationUtils(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_page);
        setTitle("Calendar");

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //this is the only place that the user's information can be be retrieved and can't be accessed out of this loop because it can't capture the data
                Users currentUser = dataSnapshot.child(uid).getValue(Users.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        currentDate = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());
        System.out.println("Current Date : " + currentDate);

        currentDateArr = currentDate.split("/");

        //set date to be current date by default
        date = currentDate;

        //Calendar
        symptomCalendar = (CalendarView)findViewById(R.id.calendarView);
        symptomCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                daySymptoms = new ArrayList<String>();
                date = month+1 + "/" + dayOfMonth + "/" + year;
                System.out.println("Selected Date : " + date);

                //parses current date into array of ints
                currentDateArr = date.split("/");
                currentMonth = Integer.parseInt(currentDateArr[0]);
                currentDay = Integer.parseInt(currentDateArr[1]);

                //parses selected date into array of ints
                selectedDateArr = currentDate.split("/");
                selectedMonth =  Integer.parseInt(selectedDateArr[0]);
                selectedDay = Integer.parseInt(selectedDateArr[1]);

                //prints out dates
                System.out.println(Arrays.toString(currentDateArr));
                System.out.println(Arrays.toString(selectedDateArr));

                Button submitButton=(Button)findViewById(R.id.calendar_submit_button);

                LinearLayout layout = (LinearLayout)findViewById(R.id.checkboxLayout);
                for (int i = 0; i < layout.getChildCount(); i++) {
                    View v = layout.getChildAt(i);
                    if (v instanceof CheckBox) {
                        ((CheckBox) v).setChecked(false);
                        //System.out.println(""((CheckBox) v).getText());
                    }
                }

                //if date clicked on is in the past
                if(selectedMonth > currentMonth || selectedMonth == currentMonth && selectedDay >= currentDay)
                {
                    olderDateSelected = true;
                    ArrayList<String> checkedSymptoms = symptomMap.get(date);
                    List<CheckBox> chosenSymptoms= new ArrayList<CheckBox>();

                    if(checkedSymptoms != null)
                    {
                        //check all boxes that were selected for an older day
                        for (int i = 0; i < layout.getChildCount(); i++) {
                            View v = layout.getChildAt(i);
                            if (v instanceof CheckBox) {
                                for(String symptom : symptomList)
                                {
                                    if((checkedSymptoms.contains(symptom)) && ((((CheckBox) v).getText()).equals(symptom)))
                                    {
                                        chosenSymptoms.add((CheckBox)v);
                                        ((CheckBox)v).setChecked(true);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        System.out.println("Symptom List is empty");
                    }
                    //edit button text Submit -> Edit
                    if(selectedDay == currentDay)
                    {
                        submitButton.setText("Submit");
                    }
                    else {
                        submitButton.setText("Edit");
                    }
                    //if (submitButton.getVisibility()==View.VISIBLE){
//                        submitButton.setVisibility(View.GONE);
//                    }
                }
                else
                {
                    olderDateSelected = false;
                    for (int i = 0; i < layout.getChildCount(); i++) {
                        View v = layout.getChildAt(i);
                        if (v instanceof CheckBox) {
                            ((CheckBox) v).setChecked(false);
                        }
                    }
                    submitButton.setText("Submit");
                    //submitButton.setVisibility(View.VISIBLE);

                }
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
                    daySymptoms.add("Runny nose");
                    if(olderDateSelected == true)
                    {
                        ((CheckBox)view).setChecked(true);
                    }
                }

                else
                {
                    if(daySymptoms.contains("Runny nose")) {
                        daySymptoms.remove("Runny nose");
                    }
                    ((CheckBox)view).setChecked(false);
                }
                break;
            case R.id.Symptom_wateryEyes:
                if(checked)
                {
                    daySymptoms.add("Watery eyes");
                    if(olderDateSelected == true)
                    {
                        ((CheckBox)view).setChecked(true);
                    }
                }
                else
                {
                    if(daySymptoms.contains("Watery eyes")) {
                        daySymptoms.remove("Watery eyes");
                    }
                    ((CheckBox)view).setChecked(false);
                }
                break;
            case R.id.Symptom_sneezing:
                if(checked)
                {
                    daySymptoms.add("Sneezing");
                    if(olderDateSelected == true)
                    {
                        ((CheckBox)view).setChecked(true);
                    }
                }
                else
                {
                    if(daySymptoms.contains("Sneezing")) {
                        daySymptoms.remove("Sneezing");
                    }
                    ((CheckBox)view).setChecked(false);
                }
                break;
            case R.id.Symptom_coughing:
                if(checked)
                {
                    daySymptoms.add("Coughing");
                    if(olderDateSelected == true)
                    {
                        ((CheckBox)view).setChecked(true);
                    }
                }
                else
                {
                    if(daySymptoms.contains("Coughing")) {
                        daySymptoms.remove("Coughing");
                    }
                    ((CheckBox)view).setChecked(false);
                }
                break;
            case R.id.Symptom_itchyEyesAndNose:
                if(checked)
                {
                    daySymptoms.add("Itchy eyes and nose");
                    if(olderDateSelected == true)
                    {
                        ((CheckBox)view).setChecked(true);
                    }
                }
                else
                {
                    if(daySymptoms.contains("Itchy eyes and nose")) {
                        daySymptoms.remove("Itchy eyes and nose");
                    }
                    ((CheckBox)view).setChecked(false);
                }
                break;
            case R.id.Symptom_darkCircles:
                if(checked)
                {
                    daySymptoms.add("Dark circles");
                    if(olderDateSelected == true)
                    {
                        ((CheckBox)view).setChecked(true);
                    }
                }
                else
                {
                    if(daySymptoms.contains("Dark circles")) {
                        daySymptoms.remove("Dark circles");
                    }
                    ((CheckBox)view).setChecked(false);
                }
                break;
            case R.id.Symptom_inflamedNasal:
                if(checked)
                {
                    daySymptoms.add("Inflamed nasal passage");
                    if(olderDateSelected == true)
                    {
                        ((CheckBox)view).setChecked(true);
                    }
                }
                else
                {
                    if(daySymptoms.contains("Inflamed nasal passage")) {
                        daySymptoms.remove("Inflamed nasal passage");
                    }
                    ((CheckBox)view).setChecked(false);
                }
                break;
            case R.id.Symptom_itchyThroat:
                if(checked)
                {
                    daySymptoms.add("Itchy throat and mouth");
                    if(olderDateSelected == true)
                    {
                        ((CheckBox)view).setChecked(true);
                    }
                }
                else
                {
                    if(daySymptoms.contains("Itchy throat and mouth")) {
                        daySymptoms.remove("Itchy throat and mouth");
                    }
                    ((CheckBox)view).setChecked(false);
                }
                break;
            case R.id.Symptom_skinReactions:
                if(checked)
                {
                    daySymptoms.add("Skin reactions");
                    if(olderDateSelected == true)
                    {
                        ((CheckBox)view).setChecked(true);
                    }
                }
                else
                {
                    if(daySymptoms.contains("Skin reactions")) {
                        daySymptoms.remove("Skin reactions");
                    }
                    ((CheckBox)view).setChecked(false);
                }
                break;
            case R.id.Symptom_earPressure:
                if(checked)
                {
                    daySymptoms.add("Ear pressure");
                    if(olderDateSelected == true)
                    {
                        ((CheckBox)view).setChecked(true);
                    }
                }
                else
                {
                    if(daySymptoms.contains("Ear pressure")) {
                        daySymptoms.remove("Ear pressure");
                    }
                    ((CheckBox)view).setChecked(false);
                }
                break;
            case R.id.Symptom_fatigue:
                if(checked)
                {
                    daySymptoms.add("Fatigue");
                    if(olderDateSelected == true)
                    {
                        ((CheckBox)view).setChecked(true);
                    }
                }
                else
                {
                    if(daySymptoms.contains("Fatigue")) {
                        daySymptoms.remove("Fatigue");
                    }
                    ((CheckBox)view).setChecked(false);
                }
                break;
        }
        //System.out.println("Symptom List : " + daySymptoms);
        globalState.getCalendarEntries(symptomMap);
        //TextView update = (TextView) findViewById(R.id.calendarEntryCount);
        //update.setText(globalState.calendarEntriesLength ());
    }

    //Submit button clicked
    public void onSubmitClicked(View view) {
        symptomMap.put(date,daySymptoms);
        System.out.println("Elements of ArrayList of String Type: " + symptomMap);

        //        //SEND NOTIFICATION LIKE THIS
        Notification.Builder nb = mNotificationUtils.
                getAndroidChannelNotification("What's My Allergy", "New recommendations for your symptoms.");

        mNotificationUtils.getManager().notify(101, nb.build());
        Log.d("Calendar Page", (String.format(Locale.ENGLISH, "Submit clicked!!!")));
    }
}


//                symptomMap.put("Runny Nose",0);
//                symptomMap.put("Watery Eyes",0);
//                symptomMap.put("Sneezing",0);
//                symptomMap.put("Coughing",0);
//                symptomMap.put("Itchy eyes and nose",0);
//                symptomMap.put("Dark circles",0);
//                symptomMap.put("Inflamed nasal passage",0);
//                symptomMap.put("Itchy throat and mouth",0);
//                symptomMap.put("Skin reactions",0);
//                symptomMap.put("Ear pressure",0);
//                symptomMap.put("Fatigue",0);

//iterate through symptoms

//                        if(checkedSymptoms.contains("Watery Eyes"))
//                        {
//                            wateryEyesCheckbox.setChecked(true);
//                        }
//                        else if(checkedSymptoms.contains("Watery Eyes")==false)
//                        {
//                            wateryEyesCheckbox.setChecked(false);
//                        }
//                        if(checkedSymptoms.contains("Runny Nose"))
//                        {
//                            runnyNoseCheckbox.setChecked(true);
//                        }
//                        else if(checkedSymptoms.contains("Runny Nose")==false)
//                        {
//                            wateryEyesCheckbox.setChecked(false);
//                        }

//                                for(CheckBox c : chosenSymptoms)
//                                {
//                                    System.out.println("Chosen Symptom = " + ((CheckBox) v2).getText());
//
//                                    String a = (((CheckBox) v2).getText()).toString();
//                                    String b = (c.getText()).toString();
//
//
//                                }