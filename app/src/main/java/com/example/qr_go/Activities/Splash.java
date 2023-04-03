package com.example.qr_go.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.qr_go.Actor.Player;
import com.example.qr_go.MainActivity;
import com.example.qr_go.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This is the class to create the loading screen
 */
public class Splash extends AppCompatActivity {
    Handler handler;
    public int themeId = 0;
    String TAG = Splash.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomTheme();
        setContentView(R.layout.splash);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("themeId", themeId);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    public String getDeviceId() {
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return deviceId;
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
                            switch (theme){
                                case 0:
                                    themeId = 0;
                                    break;
                                case 1:
                                    themeId = 1;
                                    break;
                                case 2:
                                    themeId = 2;
                                    break;
                                case 3:
                                    themeId = 3;
                                    break;
                                case 4:
                                    themeId = 4;
                                    break;
                            }
                        }
                    }
                });
    }
}
