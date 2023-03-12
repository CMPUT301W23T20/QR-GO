package com.example.qr_go;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;


import com.example.qr_go.Activities.MapsActivity;
import com.example.qr_go.Actor.Player;
import com.example.qr_go.Adapters.QRFragmentPagerAdapter;
import com.example.qr_go.Fragments.GreetingScreenFragment;
import com.example.qr_go.Fragments.LeaderboardFragment;
import com.example.qr_go.Fragments.Profile.PlayerProfileFragment;
import com.example.qr_go.Fragments.ScanFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Connects activities and fragments together
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

    private ViewPager2 viewPager;
    private LinearLayout map, scan, leaderboard, profile, currentPage;
    LocationManager locationManager;
    ArrayList<Fragment> fragments = new ArrayList<>();
    List<Address> address = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }

        getLocation();
        initGreetingScreen();
        initNavigationBar();
        initViewPager();

    }

    /**
     * This initialize a viewPager
     */
    private void initViewPager() {
        viewPager = findViewById(R.id.viewPager);
        //fragments.add(MapFragment.newInstance("Map","321"));
        fragments.add(ScanFragment.newInstance(getDeviceId()));
        fragments.add(LeaderboardFragment.newInstance("Leaderboard","321"));
        fragments.add(PlayerProfileFragment.newInstance(getDeviceId(), true));
        QRFragmentPagerAdapter pagerAdapter = new QRFragmentPagerAdapter(
                getSupportFragmentManager(),
                getLifecycle(),
                fragments);
        viewPager.setAdapter(pagerAdapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                changeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });




    }

    /**
     * This initialize a navigation bar on main screen
     */
    private void initNavigationBar() {
        map = findViewById(R.id.navigation_map);
        scan = findViewById(R.id.navigation_scan);
        leaderboard = findViewById(R.id.navigation_leaderboard);
        profile = findViewById(R.id.navigation_profile);
        currentPage = scan;

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);

            }
        });
        scan.setOnClickListener(this);
        leaderboard.setOnClickListener(this);
        profile.setOnClickListener(this);
        // uncomment to have scan opened in activity when clicked
//        scan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent scanIntent = new Intent(MainActivity.this, ScanActivity.class);
//                startActivity(scanIntent);
//
//            }
//        });

        // uncomment to have profile opened in activity when clicked
//        profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent myIntent = new Intent(MainActivity.this, PlayerProfileViewActivity.class);
//                myIntent.putExtra("android_id", getDeviceId());
//                MainActivity.this.startActivity(myIntent);
//            }
//        });
    }

    /**
     *  This initialize a greeting screen
     */
    private void initGreetingScreen() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Player.class.getSimpleName()).document(getDeviceId()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(!task.getResult().exists()) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .add(R.id.main_container, new GreetingScreenFragment(getDeviceId()))
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                });
    }

    /**
     * This calls changeTab method when a view is clicked
     * @param view
     */
    @Override
    public void onClick(View view) {
        changeTab(view.getId());
    }

    /**
     * This switches fragments when navigation bar is clicked
     * @param i
     *      This is a ID
     */
    private void changeTab(int i){
        currentPage.setBackground(getDrawable(R.drawable.border_navigation_bar));
        switch(i){
            //case R.id.navigation_map:
                //viewPager.setCurrentItem(0);
                //break;


            case R.id.navigation_scan:
                viewPager.setCurrentItem(0);
            case 0:
                scan.setBackgroundColor(getColor(R.color.gray));
                currentPage = scan;
                break;
            case R.id.navigation_leaderboard:
                viewPager.setCurrentItem(1);
            case 1:
                leaderboard.setBackgroundColor(getColor(R.color.gray));
                currentPage = leaderboard;
                break;
            case R.id.navigation_profile:
                viewPager.setCurrentItem(2);
            case 2:
                profile.setBackgroundColor(getColor(R.color.gray));
                currentPage = profile;
                break;

        }
    }

    /**
     * This returns the device's ID
     * @return
     *      Return the String ID
     */
    public String getDeviceId() {
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return deviceId;
    }


    @SuppressLint("MissingPermission")
    private void getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 5F, (LocationListener) MainActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        try{
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            address = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}