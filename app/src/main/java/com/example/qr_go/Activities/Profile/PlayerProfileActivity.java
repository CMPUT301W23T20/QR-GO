package com.example.qr_go.Activities.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.qr_go.Activities.QRView.QRViewActivity;
import com.example.qr_go.Actor.Player;
import com.example.qr_go.DataBaseHelper;
import com.example.qr_go.QR.QR;
import com.example.qr_go.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerProfileActivity extends ProfileActivity {

    private TextView usernameTextView;
    private TextView contactTextView;
    private Button qrButton;
    private Button backButton;
    private TextView totalScoreTextView;
    private TextView totalScannedTextView;
    private Player model;
    private View view;
    private String test;
    private DataBaseHelper dbHelper = new DataBaseHelper();

    public PlayerProfileActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        getViews();

        try {
            getIDFromBundle();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        updateProfileInfo();

        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent;
                Bundle extras = getIntent().getExtras();
                Boolean isThisDevice = extras.getBoolean("isThisDevice");
                // allow QR deletion if profile is this player
                if(isThisDevice) {
                    myIntent = new Intent(PlayerProfileActivity.this, ThisProfileQRListViewActivity.class);
                }
                // don't allow QR deletion if profile is other player
                else {
                    myIntent = new Intent(PlayerProfileActivity.this, OtherProfileQRListViewActivity.class);
                }
                myIntent.putExtra("android_id", android_id);

                PlayerProfileActivity.this.startActivity(myIntent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Updates the profile information on screen
     */
    private void updateProfileInfo() {


        // get database information
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection(Player.class.getSimpleName());

        // put data into class
        db.collection(Player.class.getSimpleName()).document(android_id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String username = (String)documentSnapshot.get("username");
                        String contact = (String)documentSnapshot.get("contact");
                        String deviceID = (String)documentSnapshot.get("deviceID");

                        ArrayList<QR> qrListFromDoc = dbHelper.convertQRListFromDB((List<Map<String, Object>>) documentSnapshot.get("qrList"));

                        int rank = ((Long)documentSnapshot.get("rank")).intValue();
                        int highestScore = ((Long)documentSnapshot.get("highestScore")).intValue();
                        int lowestScore = ((Long)documentSnapshot.get("lowestScore")).intValue();
                        int totalScore = ((Long)documentSnapshot.get("totalScore")).intValue();

                        model = new Player(username, deviceID, qrListFromDoc, rank, highestScore, lowestScore, totalScore);

                        // update UI
                        usernameTextView.setText("Username: " + model.getUsername());
                        contactTextView.setText("Contact: " + contact);
                        totalScoreTextView.setText("Total Score: " + model.getTotalScore());
                        totalScannedTextView.setText("Total Scanned: " + model.getTotalQR());
                    }
                });
    }


    /**
     * Gets views from fragment
     */
    public void getViews() {
        // get views from fragment
        usernameTextView = findViewById(R.id.username_text);
        qrButton = findViewById(R.id.my_qr_codes_button);
        totalScoreTextView = findViewById(R.id.total_score_text);
        totalScannedTextView = findViewById(R.id.total_scanned_text);
        contactTextView = findViewById(R.id.contact_text);
        backButton = findViewById(R.id.back_button);
    }
}
