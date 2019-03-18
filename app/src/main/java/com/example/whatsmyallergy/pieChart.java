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
import static com.example.whatsmyallergy.MainActivity.globalState;


public class pieChart extends AppCompatActivity {
    private TextView mTextMessage;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String uid;
    ArrayList<String> todaySymptoms;

    float runnynose_index = 1;
    float watereyes_index = 1;
    float sneezing_index = 1;
    float coughing_index = 1;
    float itchyeyes_index = 1;
    float darkcircles_index = 1;
    float inflamednasal_index = 5;
    float itchythroat_index = 3;
    float skin_index = 2;
    float earpressure_index = 1;
    float fatigue_index = 1;


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
                if (uid != null) {
                    Users currentUser = dataSnapshot.child(uid).getValue(Users.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        todaySymptoms = globalState.getTodaySymptoms();

        if(todaySymptoms != null){
            //for(int i = 0; i < todaySymptoms.size(); ++i){
            for(String s : todaySymptoms){
                if(s == "Runny nose"){
                    ++runnynose_index;
                }
                else if(s=="Watery eyes"){
                    ++watereyes_index;
                }
                else if(s=="Sneezing"){
                    ++sneezing_index;
                }
                else if(s == "Coughing"){
                    ++coughing_index;
                }
                else if(s=="Itchy eyes and nose"){
                    ++itchyeyes_index;
                }
                else if(s == "Dark circles"){
                    ++darkcircles_index;
                }
                else if(s == "Inflamed nasal passage"){
                    ++inflamednasal_index;
                }
                else if(s == "Itchy throat and mouth"){
                    ++itchythroat_index;
                }
                else if(s == "Skin reactions"){
                    ++skin_index;
                }
                else if(s == "Ear pressure"){
                    ++earpressure_index;
                }
                else if(s == "Fatigue"){
                    ++fatigue_index;
                }
            }
        }
        else{
            System.out.println("symptom list empty in pie chart");
        }

        //PIE CHART

        PieChart pieChart = findViewById(R.id.piechart);
        ArrayList NoOfSymptoms = new ArrayList();

        NoOfSymptoms.add(new Entry(runnynose_index, 0));
        NoOfSymptoms.add(new Entry(watereyes_index, 1));
        NoOfSymptoms.add(new Entry(sneezing_index, 2));
        NoOfSymptoms.add(new Entry(coughing_index, 3));
        NoOfSymptoms.add(new Entry(itchyeyes_index, 4));
        NoOfSymptoms.add(new Entry(darkcircles_index, 5));
        NoOfSymptoms.add(new Entry(inflamednasal_index, 6));
        NoOfSymptoms.add(new Entry(itchythroat_index, 7));
        NoOfSymptoms.add(new Entry(skin_index, 8));
        NoOfSymptoms.add(new Entry(earpressure_index, 9));
        NoOfSymptoms.add(new Entry(fatigue_index, 10));
        PieDataSet dataSet = new PieDataSet(NoOfSymptoms, "Symptoms Per MONTH NAME");

        ArrayList symptoms = new ArrayList();

        symptoms.add("Runny nose");
        symptoms.add("Watery eyes");
        symptoms.add("Sneezing");
        symptoms.add("Coughing");
        symptoms.add("Itchy eyes and nose");
        symptoms.add("Dark circles Under eyes");
        symptoms.add("Inflamed nasal passages");
        symptoms.add("Itchy throat and mouth");
        symptoms.add("Skin reactions");
        symptoms.add("Ear pressure");
        symptoms.add("Fatigue");

        PieData data = new PieData(symptoms, dataSet);
        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(5000, 5000);

        /////
    }

    public void onBack(View view) {

        Intent intent = new Intent(pieChart.this, ProfilePage.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }
}
