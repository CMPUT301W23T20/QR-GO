package com.example.qr_go.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_go.Activities.Profile.ProfileQRListViewActivity;
import com.example.qr_go.Actor.Player;
import com.example.qr_go.QR.QRComment;
import com.example.qr_go.QR.QR;
import com.example.qr_go.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class QRViewActivity extends FragmentActivity {

    private QR model;
    private Button backButton;
    private TextView nameText;
    private TextView scoreText;
    private Button playerListButton;
    private RecyclerView commentList;
    private String qr_hash;
    private String android_id;


    public QRViewActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_view);

        getViews();

        try {
            getIDFromBundle();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(QRViewActivity.this, ProfileQRListViewActivity.class);
                myIntent.putExtra("android_id", android_id);
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
    public void getViews() {
        // get views from fragment
        this.nameText = findViewById(R.id.name_text);
        this.backButton = findViewById(R.id.back_button);
        this.scoreText = findViewById(R.id.score_text);
        this.playerListButton = findViewById(R.id.player_list_button);
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
                        model = new QR((String)documentSnapshot.get("name"), (String)documentSnapshot.get("avatar"),
                                (int) Integer.parseInt((String)documentSnapshot.get("score")), (ArrayList<QRComment>)documentSnapshot.get("commentsList"));


                        // set total text
                        nameText.setText(model.getName());
                        nameText.setText(model.getScore());

                        // initialize adapter for comments
                        // to do*

                    }
                });
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
