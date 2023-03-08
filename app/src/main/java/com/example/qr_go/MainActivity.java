package com.example.qr_go;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private ViewPager2 viewPager;

    private LinearLayout map, scan, leaderboard, profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNavigationBar();
        initViewPager();
    }

    private void initViewPager() {
        viewPager = findViewById(R.id.viewPager);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(BlankFragment.newInstance("Map","321"));
        fragments.add(ScanFragment.newInstance("Scan","321"));
        fragments.add(LeaderboardFragment.newInstance("Leaderboard","321"));
        fragments.add(BlankFragment.newInstance("Profile","321"));
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }

    private void initNavigationBar() {
        map = findViewById(R.id.navigation_map);
        map.setOnClickListener(this);
        scan = findViewById(R.id.navigation_scan);
        scan.setOnClickListener(this);
        leaderboard = findViewById(R.id.navigation_leaderboard);
        leaderboard.setOnClickListener(this);
        profile = findViewById(R.id.navigation_profile);
        profile.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        changeTab(view.getId());
    }

    private void changeTab(int i){
        switch(i){
            case R.id.navigation_map:
                viewPager.setCurrentItem(0);
                break;
            case R.id.navigation_scan:
                viewPager.setCurrentItem(1);
                break;
            case R.id.navigation_leaderboard:
                viewPager.setCurrentItem(2);
                break;
            case R.id.navigation_profile:
                viewPager.setCurrentItem(3);
                break;

        }
    }
}