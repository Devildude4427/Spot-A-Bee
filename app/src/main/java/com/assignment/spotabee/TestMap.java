package com.assignment.spotabee;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.assignment.spotabee.database.AppDatabase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestMap extends AppCompatActivity implements OnMapReadyCallback{
    private HashMap<String, LatLng> markersMvcDemo;
    private static final String TAG = "my debug";
    private AppDatabase db;
    private List<Double> latitudes;
    private List<Double> longitudes;
    private HashMap<String, LatLng> coOrdinates;
    private boolean mapIsReady;
    private GoogleMap googleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapIsReady = false;

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "App Database"
        ).fallbackToDestructiveMigration().build();

        Log.d(TAG, "initialise starting");
        initialise();
        Log.d(TAG, "initialise finished");

    }

    public void initialise(){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                latitudes = db.descriptionDao()
                        .getAllLatitudes();

                longitudes = db.descriptionDao()
                        .getAllLongitudes();

                for (int i = 0; i < latitudes.size(); i++){
                    Log.d(TAG, i + " " + latitudes.get(i) + longitudes.get(i));
                    coOrdinates.put("marker", new LatLng(latitudes.get(i), longitudes.get(i)));
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        mapIsReady = true;
        initialise();
//        setMarkers(this.coOrdinates, this.googleMap);
        //setMarkers();
    }

//    public void setMarkers(HashMap<String, LatLng> coOrdinates, GoogleMap googleMap) {
public void setMarkers() {


    if(this.coOrdinates.isEmpty()){
            Log.d(TAG, "setMarkers: co-ordinates HashMap is empty.");
            return;
        }

        final ArrayList<LatLng> arrayListLatLang = new ArrayList<>();
        for (String title : coOrdinates.keySet()){
            googleMap.addMarker(new MarkerOptions()
                    .position(coOrdinates.get(title))
                    .title(title));

            arrayListLatLang.add(coOrdinates.get(title));
        }

        LatLngBounds.Builder bld = new LatLngBounds.Builder();
        for (int i = 0; i < arrayListLatLang.size(); i++) {
            LatLng ll = arrayListLatLang.get(i);
            bld.include(ll);
        }
        LatLngBounds bounds = bld.build();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 70));
    }
}
