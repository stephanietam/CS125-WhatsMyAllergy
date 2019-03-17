package com.example.whatsmyallergy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class pieChart extends AppCompatActivity {
    private TextView mTextMessage;


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
                    intent = new Intent(pieChart.this, MainActivity.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_map:
                    intent = new Intent(pieChart.this, MapPage.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_calendar:
                    intent = new Intent(pieChart.this, CalendarPage.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(pieChart.this, SettingsPage.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);


        //


        setTitle("Profile");

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        MenuItem menuItem = navigation.getMenu().getItem(1);
        menuItem.setChecked(true);


        System.out.println("+++++++++++++++++++++++" + uid + "+++++++++++++++++++++++++++++++");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

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

    public void onBack(View view) {

        Intent intent = new Intent(pieChart.this, ProfilePage.class);
        startActivity(intent);
    }
}
