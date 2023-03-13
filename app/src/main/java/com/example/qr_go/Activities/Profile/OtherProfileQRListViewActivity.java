package com.example.qr_go.Activities.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_go.Activities.QRView.QRViewActivity;
import com.example.qr_go.Actor.Player;
import com.example.qr_go.Adapters.ProfileQRListAdapter;
import com.example.qr_go.DataBaseHelper;
import com.example.qr_go.Interfaces.RecyclerViewInterface;
import com.example.qr_go.QR.QR;
import com.example.qr_go.QR.QRComment;
import com.example.qr_go.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents profile of Player not on this device
 */
public class OtherProfileQRListViewActivity extends ProfileActivity implements RecyclerViewInterface {
    private Button backButton;
    private TextView totalText;
    private RecyclerView qrListRecyclerView;
    private ProfileQRListAdapter qrListAdapter;
    private ArrayList<QR> qrDataList;
    private Player player;
    private DataBaseHelper dbHelper = new DataBaseHelper();

    public OtherProfileQRListViewActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_qr_list);

        getViews();

        try {
            getIDFromBundle();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // will stack between two activities.
//                Intent myIntent = new Intent(ProfileQRListViewActivity.this, PlayerProfileViewActivity.class);
//                myIntent.putExtra("android_id", android_id);
//                ProfileQRListViewActivity.this.startActivity(myIntent);
                finish();
            }
        });

        updateProfileInfo();
    }

    @Override
    public void onStart() {
        super.onStart();

        updateProfileInfo();
    }

    @Override
    public void onResume() {
        super.onResume();

        updateProfileInfo();
    }

    /**
     * Updates the profile information on screen
     */
    private void updateProfileInfo() {

        getViews();

        // get database information
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection(Player.class.getSimpleName());

        // put data into class
        db.collection(Player.class.getSimpleName()).document(android_id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String username = (String)documentSnapshot.get("username");
                        String deviceID = (String)documentSnapshot.get("deviceID");

                        ArrayList<QR> qrListFromDoc = dbHelper.convertQRListFromDB((List<Map<String, Object>>) documentSnapshot.get("qrList"));

                        int rank = ((Long)documentSnapshot.get("rank")).intValue();
                        int highestScore = ((Long)documentSnapshot.get("highestScore")).intValue();
                        int lowestScore = ((Long)documentSnapshot.get("lowestScore")).intValue();
                        int totalScore = ((Long)documentSnapshot.get("totalScore")).intValue();



                        player = new Player(username, deviceID, qrListFromDoc, rank, highestScore, lowestScore, totalScore);

                        // add data list from player
                        qrDataList = new ArrayList<QR>();

                        qrDataList.addAll(player.getQRList());

                        // initialize adapter
                        qrListRecyclerView = findViewById(R.id.qr_list);
                        qrListRecyclerView.setLayoutManager(new LinearLayoutManager(OtherProfileQRListViewActivity.this));
                        qrListAdapter = new ProfileQRListAdapter(OtherProfileQRListViewActivity.this, qrDataList, OtherProfileQRListViewActivity.this);
                        qrListRecyclerView.setAdapter(qrListAdapter);

                        // set total text
                        totalText.setText("Total QRs: " + player.getTotalQR());
                    }
                });
    }

    /**
     * Gets views from fragment
     */
    public void getViews() {
        // get views from fragment
        this.qrListRecyclerView = findViewById(R.id.qr_list);
        this.backButton = findViewById(R.id.qr_list_back_button);
        this.totalText = findViewById(R.id.total_text);
    }

    /**
     * Sends you to QR view
     * @param i
     * Index of QR in list
     */
    @Override
    public void onItemClick(int i) {
        Intent myIntent = new Intent(OtherProfileQRListViewActivity.this, QRViewActivity.class);
        myIntent.putExtra("android_id", android_id);
        myIntent.putExtra("qr_hash", qrDataList.get(i).getQrHash());//Optional parameters
        OtherProfileQRListViewActivity.this.startActivity(myIntent);
    }
}
