package com.example.qr_go.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.qr_go.Actor.Player;
import com.example.qr_go.Adapters.LeaderboardAdapter;
import com.example.qr_go.QR.QR;
import com.example.qr_go.QR.QRComment;
import com.example.qr_go.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

        ArrayList<QR> qrList = new ArrayList<>();

        // get database information
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = db.collection(Player.class.getSimpleName());

        // add db to
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for(QueryDocumentSnapshot doc: value) {
                    // create qr from doc
                    String name = (String)doc.get("name");
                    String avatar = (String)doc.get("avatar");
                    int score = ((Long)doc.get("score")).intValue();


                    QR qr = new QR(doc.getId(), name, avatar, score,
                            new ArrayList<>(),
                            new ArrayList<>());

                    qrList.add(qr);
                }

                // do whatever the fuck you want with the new qrList data that we just created
            }
        });

        LatLng tester = new LatLng(50,20);
        this.gMap.addMarker(new MarkerOptions().position(tester).title("QR SCORE: 300"));
        this.gMap.moveCamera(CameraUpdateFactory.newLatLng(tester));


    }
}