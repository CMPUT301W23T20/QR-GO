package com.example.qr_go.Activities.QRView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_go.Actor.Player;
import com.example.qr_go.QR.QRComment;
import com.example.qr_go.QR.QR;
import com.example.qr_go.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Represents a QR
 */
public class QRViewActivity extends QRActivity {
    private QR qr;
    private Button backButton;
    private TextView nameText;
    private TextView scoreText;
    private Button playerListButton;
    private RecyclerView commentListRecyclerView;


    public QRViewActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_view);

        getViews();

        try {
            getInfoFromBundle();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // will stack between two activities.
//                Intent myIntent = new Intent(QRViewActivity.this, ProfileQRListViewActivity.class);
//                myIntent.putExtra("android_id", android_id);
//                QRViewActivity.this.startActivity(myIntent);
                finish();
            }
        });

        playerListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(QRViewActivity.this, QRPlayerListViewActivity.class);
                myIntent.putExtra("android_id", android_id);
                myIntent.putExtra("qr_hash", qr_hash);
                QRViewActivity.this.startActivity(myIntent);
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
    private void getViews() {
        // get views from fragment
        this.nameText = findViewById(R.id.name_text);
        this.backButton = findViewById(R.id.back_button);
        this.scoreText = findViewById(R.id.score_text);
        this.playerListButton = findViewById(R.id.player_list_button);
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
                        String name = (String)documentSnapshot.get("name");
                        String avatar = (String)documentSnapshot.get("avatar");
                        int score = ((Long)documentSnapshot.get("score")).intValue();
                        qr = new QR(name , avatar, score,
                                (ArrayList<QRComment>)documentSnapshot.get("commentsList"),
                                (ArrayList<Player>)documentSnapshot.get("playerList"));


                        // set total text
                        nameText.setText(qr.getName());
                        scoreText.setText(qr.getScore());

                        // initialize adapter for comments
                        // to do*

                    }
                });
    }
}
