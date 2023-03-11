package com.example.qr_go.Activities.Scan;
import androidx.annotation.NonNull;
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

import com.example.qr_go.MainActivity;
import com.example.qr_go.R;

import java.util.List;
import java.util.Locale;

public class GetLocationActivity extends AppCompatActivity implements LocationListener {
    Button button_location;

    Button done_button;
    TextView textView_location;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

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

    @SuppressLint("MissingPermission")
    private void getLocation(){
        try{
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,GetLocationActivity.this);


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(this,""+location.getLatitude()+","+location.getLongitude(),Toast.LENGTH_SHORT).show();

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