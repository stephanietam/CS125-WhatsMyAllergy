package com.example.whatsmyallergy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.whatsmyallergy.MainActivity.globalState;

public class MapPage extends AppCompatActivity implements OnMapReadyCallback {

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
                    intent = new Intent(MapPage.this, MainActivity.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    intent.putExtra("uid", uid);
                    return true;
                case R.id.navigation_profile:
                    intent = new Intent(MapPage.this, ProfilePage.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_calendar:
                    intent = new Intent(MapPage.this, CalendarPage.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(MapPage.this, SettingsPage.class);
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
        setContentView(R.layout.activity_map_page);
        setTitle("Map");

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference("Users");
//
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Users currentUser = dataSnapshot.child(uid).getValue(Users.class);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        MenuItem menuItem = navigation.getMenu().getItem(2);
        menuItem.setChecked(true);
    }

    /* https://developers.google.com/maps/documentation/android-sdk/map-with-marker */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker
        // and move the map's camera to the same location.
        double[] latlng = globalState.getLatLong();  // latlng should be set to profile postal code location initially
        LatLng location = new LatLng(latlng[0], latlng[1]);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        googleMap.setMinZoomPreference(12);

        // TO DO: Populate map with more markers using another API for nearby zip codes
        Log.d("Print", "GeoNames Api called");
        AsyncTask asyncTask = new GeoNamesApi(googleMap).execute();
    }
}
