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
import com.example.qr_go.Interfaces.RecyclerViewInterface;
import com.example.qr_go.QR.QR;
import com.example.qr_go.QR.QRComment;
import com.example.qr_go.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class OtherProfileQRListViewActivity extends ProfileActivity implements RecyclerViewInterface {
    private Button backButton;
    private TextView totalText;
    private RecyclerView qrListRecyclerView;
    private ProfileQRListAdapter qrListAdapter;
    private ArrayList<QR> qrDataList;
    private Player player;

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
                        player = new Player((String)documentSnapshot.get("username"), (String)documentSnapshot.get("deviceID"), (ArrayList<QR>) documentSnapshot.get("qrList"),
                                (int) Integer.parseInt((String)documentSnapshot.get("rank")), (int) Integer.parseInt((String)documentSnapshot.get("highestScore")),
                                (int)Integer.parseInt((String)documentSnapshot.get("lowestScore")), (int)Integer.parseInt((String)documentSnapshot.get("totalScore")));

                        // add data list from player
                        qrDataList = new ArrayList<QR>();

                        qrDataList.addAll(player.getQRList());

                        // test
                        qrDataList.add(new QR("herbert", "avatar", 300, new ArrayList<QRComment>()));

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
