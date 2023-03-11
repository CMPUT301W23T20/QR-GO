package com.example.qr_go.Activities.QRView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qr_go.Activities.Profile.ProfileQRListViewActivity;
import com.example.qr_go.Actor.Player;
import com.example.qr_go.Adapters.LeaderboardAdapter;
import com.example.qr_go.Adapters.QRPlayerListAdapter;
import com.example.qr_go.QR.QR;
import com.example.qr_go.QR.QRComment;
import com.example.qr_go.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class QRPlayerListViewActivity extends QRActivity {
    QRPlayerListAdapter qrPlayerListAdapter;
    private ArrayList<Player> dataPlayerList;
    private ListView playerListView;
    private QR qr;
    private Button backButton;
    private TextView nameText;

    public QRPlayerListViewActivity() {}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_player_list_view);

        getViews();

        try {
            getIDFromBundle();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(QRPlayerListViewActivity.this, QRViewActivity.class);
                myIntent.putExtra("android_id", android_id);
                myIntent.putExtra("qr_hash", qr_hash);
                QRPlayerListViewActivity.this.startActivity(myIntent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Gets views from fragment
     */
    public void getViews() {
        // get views from fragment
        this.backButton = findViewById(R.id.back_button);
        this.nameText = findViewById(R.id.name_text);
    }

    /**
     * Updates the QR information on screen
     */
    private void updateQRInfo() {

        getViews();

        // get database information
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection(Player.class.getSimpleName());

        // put data into class
        db.collection(Player.class.getSimpleName()).document(android_id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        qr = new QR((String)documentSnapshot.get("name"), (String)documentSnapshot.get("avatar"),
                                (int) Integer.parseInt((String)documentSnapshot.get("score")), (ArrayList<QRComment>)documentSnapshot.get("commentsList"));


                        // set total text
                        nameText.setText(qr.getName());
                        dataPlayerList = qr.getPlayerList();

                        // initialize adapter for player list
                        playerListView = findViewById(R.id.players_list_view);
                        qrPlayerListAdapter = new QRPlayerListAdapter(QRPlayerListViewActivity.this, dataPlayerList);
                        playerListView.setAdapter(qrPlayerListAdapter);
                    }
                });
    }
}
