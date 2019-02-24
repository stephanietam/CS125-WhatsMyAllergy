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

import java.util.ArrayList;

public class ProfilePage extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                {
                    Intent intent= new Intent(ProfilePage.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                }
                case R.id.navigation_calendar:
                {
                    Intent intent= new Intent(ProfilePage.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                }
                case R.id.navigation_settings:
                {
                    return false;
                }
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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




    }

}
