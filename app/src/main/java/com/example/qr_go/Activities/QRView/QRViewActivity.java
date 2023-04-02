package com.example.qr_go.Activities.QRView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qr_go.DataBaseHelper;
import com.example.qr_go.Interfaces.RecyclerViewInterface;
import com.example.qr_go.QR.Avatar;
import com.example.qr_go.QR.QRComment;
import com.example.qr_go.QR.QR;
import com.example.qr_go.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a QR
 */
public class QRViewActivity extends QRActivity implements RecyclerViewInterface {
    private QR qr;
    private Button backButton;
    private TextView nameText;
    private TextView scoreText;
    private TextView avatarTextView;
    private ImageView imageView;
    private Uri imageUri;
    private Button playerListButton;
    private Button commentListButton;

    private DataBaseHelper dbHelper;

    public QRViewActivity() {
        dbHelper = new DataBaseHelper();
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

        updateQRInfo();

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

        commentListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(QRViewActivity.this, QRCommentViewActivity.class);
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
        this.imageView = findViewById(R.id.image_view);
        this.playerListButton = findViewById(R.id.player_list_button);
        this.commentListButton = findViewById(R.id.comment_list_button);
        this.avatarTextView = findViewById(R.id.visual_text);
    }

    /**
     * Updates the QR information on screen
     */
    private void updateQRInfo() {

        getViews();

        // get database information
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection(QR.class.getSimpleName());

        // put data into class
        collectionReference.document(qr_hash).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String name = (String)documentSnapshot.get("name");
                        String avatar = (String)documentSnapshot.get("avatar");
                        String image = (String)documentSnapshot.get("photoURI");
                        int score = ((Long)documentSnapshot.get("score")).intValue();
                        ArrayList<QRComment> commentList = dbHelper.convertQRCommentListFromDB((List<Map<String, Object>>)documentSnapshot.get("commentsList"));


                        qr = new QR(qr_hash, name, avatar, score,
                                commentList,
                                new ArrayList<>());


                        // set total text
                        nameText.setText("Name: " + qr.getName());

                        scoreText.setText("Score: " + qr.getScore());

                        // set image view
                        if (image != null) {
                            imageUri = Uri.parse(image);
                            Picasso.get().load(imageUri).into(imageView);

                        }


                        // put avatar into xml view
                        avatarTextView.setText(qr.getAvatar());

                    }
                });
    }

    @Override
    public void onItemClick(int i) {

    }
}
