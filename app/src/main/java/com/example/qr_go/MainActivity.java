package com.example.qr_go;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;


import com.example.qr_go.Activities.MapsActivity;
import com.example.qr_go.Actor.Player;
import com.example.qr_go.Adapters.QRFragmentPagerAdapter;
import com.example.qr_go.Activities.Profile.GreetingScreenActivity;
import com.example.qr_go.Fragments.LeaderboardFragment;
import com.example.qr_go.Fragments.Profile.PlayerProfileFragment;
import com.example.qr_go.Fragments.ScanFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
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
    String TAG = MainActivity.class.getSimpleName();
    public int themeId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, TAG + " onCreate");
        Log.d(TAG, TAG + themeId);
        //setCustomTheme();
        //Log.d(TAG, TAG + themeId);
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            themeId = bundle.getInt("themeId");
            switch (themeId) {
                case 0:
                    setTheme(R.style.Theme_QRGO);
                    break;
                case 1:
                    setTheme(R.style.Theme_QRGO);
                    break;
                case 2:
                    setTheme(R.style.MyAppTheme1);
                    break;
                case 3:
                    setTheme(R.style.MyAppTheme2);
                    break;
                case 4:
                    setTheme(R.style.MyAppTheme3);
                    break;
            }
        }

        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }

        getLocation();
        initGreetingScreen();
        initNavigationBar();
        if (viewPager == null) {
            initViewPager();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, TAG + "onDestroy");

    }

    /**
     * This initialize a viewPager
     */
    private void initViewPager() {
        viewPager = findViewById(R.id.viewPager);
        //fragments.add(ScanFragment.newInstance(getDeviceId()));
        Log.d(TAG, TAG + "initViewPager");
        ScanFragment scanFragment = ScanFragment.newInstance(getDeviceId());
        scanFragment.setFragmentCallback(new FragmentCallback() {
            @Override
            public int getThemeId() {
                return themeId;
            }
        });

        fragments.add(scanFragment);
        fragments.add(LeaderboardFragment.newInstance(getDeviceId()));
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
        ImageView image1,image2,image3,image4;
        image1 = (ImageView) findViewById(R.id.imageView1);
        image2 = (ImageView) findViewById(R.id.imageView2);
        image3 = (ImageView) findViewById(R.id.imageView3);
        image4 = (ImageView) findViewById(R.id.imageView4);
        switch (themeId){
            case 0:
            case 1:
                image1.setBackgroundColor(getResources().getColor(R.color.green));
                image2.setBackgroundColor(getResources().getColor(R.color.green));
                image3.setBackgroundColor(getResources().getColor(R.color.green));
                image4.setBackgroundColor(getResources().getColor(R.color.green));
                break;
            case 2:
                image1.setBackgroundColor(getResources().getColor(R.color.theme1PV));
                image2.setBackgroundColor(getResources().getColor(R.color.theme1PV));
                image3.setBackgroundColor(getResources().getColor(R.color.theme1PV));
                image4.setBackgroundColor(getResources().getColor(R.color.theme1PV));
                break;
            case 3:
                image1.setBackgroundColor(getResources().getColor(R.color.theme2PV));
                image2.setBackgroundColor(getResources().getColor(R.color.theme2PV));
                image3.setBackgroundColor(getResources().getColor(R.color.theme2PV));
                image4.setBackgroundColor(getResources().getColor(R.color.theme2PV));
                break;
            case 4:
                image1.setBackgroundColor(getResources().getColor(R.color.theme3PV));
                image2.setBackgroundColor(getResources().getColor(R.color.theme3PV));
                image3.setBackgroundColor(getResources().getColor(R.color.theme3PV));
                image4.setBackgroundColor(getResources().getColor(R.color.theme3PV));
                break;
        }

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
     * This initialize a greeting screen
     */
    private void initGreetingScreen() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Player.class.getSimpleName()).document(getDeviceId()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.getResult().exists()) {
                            Intent myIntent = new Intent(MainActivity.this, GreetingScreenActivity.class);
                            myIntent.putExtra("android_id", getDeviceId());
                            MainActivity.this.startActivity(myIntent);
                        }
                    }
                });
    }

    /**
     * This retrieve theme data from database and apply the theme to the app
     */
    public void setCustomTheme1() {
        // get database information
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection(Player.class.getSimpleName());


        db.collection(Player.class.getSimpleName()).document(getDeviceId()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            int theme = ((Long) task.getResult().get("theme")).intValue(); // theme val from db
                            if (theme == 0) {
                                themeId = 0;
                                setTheme(R.style.Theme_QRGO);
                                Log.d(TAG, TAG + "if" + themeId);
                            } else {
                                themeId = 1;
                                setTheme(R.style.AppTheme_Cyan);
                                Log.d(TAG, TAG + "else" + themeId);

                            }
                        }
                        Log.d(TAG, TAG + "out" + themeId);
                    }
                });
        Log.d(TAG, TAG + "out" + themeId);
    }

    public void setCustomTheme() {
        // get database information
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection(Player.class.getSimpleName());


        db.collection(Player.class.getSimpleName()).document(getDeviceId()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            int theme = ((Long) documentSnapshot.get("theme")).intValue(); // theme val from db
                            if (theme == 0) {
                                themeId = 0;
                                setTheme(R.style.Theme_QRGO);
                                Log.d(TAG, TAG + "if" + themeId);
                            } else {
                                setThemeId(1);
                                setTheme(R.style.AppTheme_Cyan);
                                Log.d(TAG, TAG + "else" + themeId);

                            }
                        }
                        Log.d(TAG, TAG + "out" + themeId);
                    }
                });
        Log.d(TAG, TAG + "out" + themeId);
    }

    private void setThemeId(Integer themeId){
        this.themeId = themeId;
    }


    /**
     * This calls changeTab method when a view is clicked
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        changeTab(view.getId());
    }

    /**
     * This switches fragments when navigation bar is clicked
     *
     * @param i This is a ID
     */
    private void changeTab(int i) {
        currentPage.setBackground(getDrawable(R.drawable.border_navigation_bar));
        switch (i) {
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
     *
     * @return Return the String ID
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
        try {
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // create option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);
        MenuItem item = menu.findItem(R.id.music_switch1);
        item.setActionView(R.layout.music_switch_layout);
        Switch musicSwitch = item.getActionView().findViewById(R.id.music);

        if (musicSwitch.isChecked()) {
            MainActivity.this.startService(new Intent(MainActivity.this, MusicService.class));
        }
        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    MainActivity.this.startService(new Intent(MainActivity.this, MusicService.class));
                } else {
                    MainActivity.this.stopService(new Intent(MainActivity.this, MusicService.class));
                }
            }
        });
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    // optionMenu selection method
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // get database information
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection(Player.class.getSimpleName());
        HashMap<String, Object> data = new HashMap<>();

        switch (item.getItemId()) {
            case R.id.default_theme:
                Toast.makeText(this, "restart to view theme", Toast.LENGTH_SHORT).show();
                //setTheme(R.style.Theme_QRGO);
                data.put("theme", 0);
                // update db
                collectionReference.document(getDeviceId()).update(data);
                break;
            case R.id.theme1:
                Toast.makeText(this, "restart to view theme", Toast.LENGTH_SHORT).show();
                //MainActivity.this.setTheme(R.style.MyAppTheme);
                data.put("theme", 1);
                // update db
                collectionReference.document(getDeviceId()).update(data);
                break;
            case R.id.theme2:
                Toast.makeText(this, "restart to view theme", Toast.LENGTH_SHORT).show();
                //MainActivity.this.setTheme(R.style.MyAppTheme);
                data.put("theme", 2);
                // update db
                collectionReference.document(getDeviceId()).update(data);
                break;
            case R.id.theme3:
                Toast.makeText(this, "restart to view theme", Toast.LENGTH_SHORT).show();
                //MainActivity.this.setTheme(R.style.MyAppTheme);
                data.put("theme", 3);
                // update db
                collectionReference.document(getDeviceId()).update(data);
                break;
            case R.id.theme4:
                Toast.makeText(this, "restart to view theme", Toast.LENGTH_SHORT).show();
                //MainActivity.this.setTheme(R.style.MyAppTheme);
                data.put("theme", 4);
                // update db
                collectionReference.document(getDeviceId()).update(data);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}