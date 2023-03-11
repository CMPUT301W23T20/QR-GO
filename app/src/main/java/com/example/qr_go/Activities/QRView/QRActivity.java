package com.example.qr_go.Activities.QRView;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.qr_go.QR.QR;

public class QRActivity extends FragmentActivity {
    protected String android_id;
    protected String qr_hash;
    public QRActivity() {
        // set to empty string because you cannot access intent until onCreate
        qr_hash = "";
        android_id = "";
    }

    public void getIDFromBundle() throws Exception{
        if(getIntent() == null || getIntent().getExtras() == null) {
            throw new Exception("Must have intent with \"android_id\" and \"qr_hash\"");
        }
        Bundle extras = getIntent().getExtras();
        Intent intent = getIntent();
        android_id = extras.getString("android_id");
        qr_hash = extras.getString("qr_hash");
    }
}
