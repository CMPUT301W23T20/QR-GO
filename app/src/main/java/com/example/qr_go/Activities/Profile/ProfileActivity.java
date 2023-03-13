package com.example.qr_go.Activities.Profile;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

/**
 * Parent class to profile related activities
 */
public class ProfileActivity extends FragmentActivity {

    protected String android_id;

    public ProfileActivity() {
        // set to empty string because you cannot access intent until onCreate
        this.android_id = "";
    }

    /**
     * Gets android ID from bundle
     * @throws Exception
     * Throws exception if no ID was passed in intent
     */
    public void getIDFromBundle() throws Exception{
        if(getIntent() == null || getIntent().getExtras() == null) {
            throw new Exception("Must have intent with \"android_id\"");
        }
        Bundle extras = getIntent().getExtras();
        android_id = extras.getString("android_id");
    }
}
