package com.example.qr_go.Activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.qr_go.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.type.LatLng;

//Source: Youtube.com
//URL: https://www.youtube.com/watch?v=JzxjNNCYt_o&t=252s
//Author: https://www.youtube.com/@android_knowledge


/**
 * This class is an activity that shows Google Maps with markers showing geolocations
 * of scanned QR codes
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap gMap;
    FrameLayout map;
    Button back;

    /**
     * This function is called when the activity is created
     * It ensures that the map button and Back button works
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        map = findViewById(R.id.map);
        back = findViewById(R.id.map_back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    /**
     * This function is called, displaying the actual google map
     * It also adds markers to it according to QR code's geolocation
     * @param googleMap
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;
        LatLng tester = new LatLng(50,20);
        this.gMap.addMarker(new MarkerOptions().position(tester).title("QR SCORE: 300"));
        this.gMap.moveCamera(CameraUpdateFactory.newLatLng(tester));


    }
}