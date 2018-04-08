package com.assignment.spotabee;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import com.assignment.spotabee.database.AppDatabase;
import com.assignment.spotabee.database.Description;
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

public class Markers extends AppCompatActivity implements OnMapReadyCallback{
    private static final String TAG = "my_debug";
    private AppDatabase db;
    private List<Double> latitudes;
    private List<Double> longitudes;
    private List<Description> descriptions;
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

        this.coOrdinates = new HashMap<>();
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

//    public void initialise(){
//
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                latitudes = db.descriptionDao()
//                        .getAllLatitudes();
//
//                longitudes = db.descriptionDao()
//                        .getAllLongitudes();
//
//                for (int i = 0; i < latitudes.size(); i++){
//                    Log.d(TAG, "IN DATABASE: " + i + " " + latitudes.get(i) + longitudes.get(i));
//                    coOrdinates.put(String.format("marker %d", i), new LatLng(latitudes.get(i), longitudes.get(i)));
//                }
//            }
//        });
//        for(String key : coOrdinates.keySet()){
//            Log.d(TAG, "IN CO-ORDINATES:" + coOrdinates.get(key).toString());
//        }
//    }
public void initialise(){

    AsyncTask.execute(new Runnable() {
        @Override
        public void run() {
            descriptions = db.descriptionDao()
                    .getAllDescriptions();

            for (int i = 0; i < descriptions.size(); i++){
//                Log.d(TAG, "IN DATABASE: " + i + " " + latitudes.get(i) + longitudes.get(i));
                Description currentDescription = descriptions.get(i);
                coOrdinates.put(currentDescription.getLocation(),
                        new LatLng(currentDescription.getLatitude(),
                                currentDescription.getLongitude()));
            }
        }
    });
    for(String key : coOrdinates.keySet()){
        Log.d(TAG, "IN CO-ORDINATES:" + coOrdinates.get(key).toString());
    }
}

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        mapIsReady = true;
        initialise();

        readyMapLayout();
    }


public void setMarkers(int width, int height) {


    if(coOrdinates.isEmpty()){
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
    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, 70));
    }

    public void readyMapLayout(){
        final View mapFragment = findViewById(R.id.map);
        ViewTreeObserver vto = mapFragment.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mapFragment.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width  = mapFragment.getMeasuredWidth();
                int height = mapFragment.getMeasuredHeight();
                setMarkers(width, height);

            }
        });
    }
}
