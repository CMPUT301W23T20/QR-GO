package com.example.qr_go.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.fragment.app.FragmentActivity;

import com.example.qr_go.Activities.Profile.PlayerProfileViewActivity;

public class TestActivity extends FragmentActivity {

    public TestActivity(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent myIntent = new Intent(TestActivity.this, PlayerProfileViewActivity.class);
        myIntent.putExtra("android_id", getAndroidID());
        TestActivity.this.startActivity(myIntent);
    }

    public String getAndroidID() {
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return android_id;
    }
}
