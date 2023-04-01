package com.example.qr_go.Activities.Scan;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import com.example.qr_go.Actor.Player;
import com.example.qr_go.DataBaseHelper;
import com.example.qr_go.MainActivity;
import com.example.qr_go.QR.QR;
import com.example.qr_go.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

//Source: Youtube.com
//URL: https://www.youtube.com/@allcodingtutorials1857
//Author:https://www.youtube.com/@CodingwithDev


/**
 * This class is called after scanning a QR code and recording the object
 * It allows the player the choice to obtain the geolocation of the object to be stored
 */
public class GetLocationActivity extends AppCompatActivity implements LocationListener {
    Button button_location;
    Button done_button;
    TextView textView_location;
    LocationManager locationManager;

    private DataBaseHelper dbHelper = new DataBaseHelper();

    QR qr;

    /**
     * This function is called right when the activity is created
     * It ensures that the Get Location button works, it retrieves the user's geolocation and stores it
     * It also ensures the Done button will bring the player back to MainActivity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        qr = getIntent().getParcelableExtra("QR");

        textView_location = findViewById(R.id.text_location);
        button_location = findViewById(R.id.button_location);
        done_button = findViewById(R.id.done_button);
        // Runtime permissions
        if(ContextCompat.checkSelfPermission(GetLocationActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(GetLocationActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }


        button_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(GetLocationActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });
    }

    /**
     * This function requests the locationManager to get the location of the user
     */
    @SuppressLint("MissingPermission")
    private void getLocation(){
        try{
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,GetLocationActivity.this);


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * This function is called whenever Location is updated
     * It will store the address in a List, this will be stored with the QR code
     * @param location the updated location
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {
       // Toast.makeText(this,""+location.getLatitude()+","+location.getLongitude(),Toast.LENGTH_SHORT).show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection(QR.class.getSimpleName());

        HashMap<String, Object> data = new HashMap<>();
        data.put("latitude", (float)location.getLatitude());
        data.put("longitude", (float)location.getLongitude());

        collectionReference.document(qr.getQrHash()).update(data);

        try{
            Geocoder geocoder = new Geocoder(GetLocationActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);

            textView_location.setText(address);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}