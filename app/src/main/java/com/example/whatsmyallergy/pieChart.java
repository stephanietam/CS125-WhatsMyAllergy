package com.example.whatsmyallergy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class pieChart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);


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
