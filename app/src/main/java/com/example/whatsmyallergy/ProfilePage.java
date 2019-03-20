package com.example.whatsmyallergy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


public class ProfilePage extends AppCompatActivity {

    private TextView userNameText,userDOB, userZip;
    private Switch petSwitch;
    private TextView knownAllergy, familyHistory;
    private Button overview1Button, overview2Button;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private String uid;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(ProfilePage.this, MainActivity.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_map:
                    intent = new Intent(ProfilePage.this, MapPage.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_calendar:
                    intent = new Intent(ProfilePage.this, CalendarPage.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(ProfilePage.this, SettingsPage.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };

    private void lookup() {
        userNameText = findViewById(R.id.UserName);
        userDOB = findViewById(R.id.UserDOB);
        petSwitch = findViewById(R.id.PetSwitch);
        knownAllergy = findViewById(R.id.allergyListView);
        familyHistory = findViewById(R.id.familyHistoryListView);
        userZip = findViewById(R.id.UserZip);
        overview1Button = findViewById(R.id.overview1Button);
        overview1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, pieChart.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });
        overview2Button = findViewById(R.id.overview2Button);
        overview2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, pieChart.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });
    }


    private String listToString(ArrayList<String> list)
    {
        StringBuffer res = new StringBuffer();
        if (list.size()!=0) {
            for (int i = 0; i < list.size() - 1; i++) {
                res.append(list.get(i));
                res.append("\n\n");
            }
            res.append(list.get(list.size()-1));

        }
        return res.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        setTitle("Profile");

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");


//        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        MenuItem menuItem = navigation.getMenu().getItem(1);
        menuItem.setChecked(true);

        lookup();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //this is the only place that the user's information can be be retrieved and can't be accessed out of this loop because it can't capture the data
                Users currentUser = dataSnapshot.child(uid).getValue(Users.class);
                userNameText.setText(currentUser.userName);
                userDOB.setText(currentUser.userDOB);
                userZip.setText(currentUser.userZip);
                petSwitch.setChecked(currentUser.pets);
                knownAllergy.setText(listToString(currentUser.knownAllergens));
                familyHistory.setText(listToString(currentUser.familyHistory));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        //PIE CHART
//
//        PieChart pieChart = findViewById(R.id.piechart);
//        ArrayList NoOfSymptoms = new ArrayList();
//
//        NoOfSymptoms.add(new Entry(12, 0));
//        NoOfSymptoms.add(new Entry(4f, 1));
//        NoOfSymptoms.add(new Entry(7f, 2));
//        NoOfSymptoms.add(new Entry(9f, 3));
//        NoOfSymptoms.add(new Entry(1f, 4));
//        NoOfSymptoms.add(new Entry(12, 5));
//        NoOfSymptoms.add(new Entry(4f, 6));
//        NoOfSymptoms.add(new Entry(22f, 7));
//        NoOfSymptoms.add(new Entry(9f, 8));
//        NoOfSymptoms.add(new Entry(11f, 9));
//        NoOfSymptoms.add(new Entry(1f, 10));
//        PieDataSet dataSet = new PieDataSet(NoOfSymptoms, "Symptoms Per MONTH NAME");
//
//        ArrayList symptoms = new ArrayList();
//
//        symptoms.add("Runny Nose");
//        symptoms.add("Watery Eyes");
//        symptoms.add("Sneezing");
//        symptoms.add("Coughing");
//
//        symptoms.add("Itchy Eyes and Nose");
//        symptoms.add("Dark Circles Under Eyes");
//        symptoms.add("Inflamed Nasal Passages");
//        symptoms.add("Itchy Throat and Mouth");
//        symptoms.add("Skin Reactions");
//        symptoms.add("Ear Pressure");
//        symptoms.add("Fatigue");
//
//
//        PieData data = new PieData(symptoms, dataSet);
//        pieChart.setData(data);
//        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//        pieChart.animateXY(5000, 5000);
//
//        /////
    }




}
