package com.example.qr_go.Activities.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qr_go.Activities.QRViewActivity;
import com.example.qr_go.Actor.PlayerModel;
import com.example.qr_go.Adapters.ProfileQRListAdapter;
import com.example.qr_go.ProfileActivity;
import com.example.qr_go.QR.QRComment;
import com.example.qr_go.QR.QRModel;
import com.example.qr_go.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ProfileQRListViewActivity extends ProfileActivity {
    private Button backButton;
    private TextView totalText;
    private ListView qrList;
    private ProfileQRListAdapter qrListAdapter;
    private ArrayList<QRModel> qrDataList;

    private PlayerModel model;

    public ProfileQRListViewActivity() {
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
                Intent myIntent = new Intent(ProfileQRListViewActivity.this, PlayerProfileViewActivity.class);
                myIntent.putExtra("android_id", android_id);
                ProfileQRListViewActivity.this.startActivity(myIntent);
            }
        });

        updateProfileInfo();

        // set single QR view on item click
        qrList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent myIntent = new Intent(ProfileQRListViewActivity.this, QRViewActivity.class);
                myIntent.putExtra("android_id", android_id);
                myIntent.putExtra("qr_hash", qrDataList.get(i).getQrHash());//Optional parameters
                ProfileQRListViewActivity.this.startActivity(myIntent);
            }
        });
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
        CollectionReference collectionReference = db.collection(PlayerModel.class.getSimpleName());

        // put data into class
        db.collection(PlayerModel.class.getSimpleName()).document(android_id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        model = new PlayerModel((String)documentSnapshot.get("username"), (String)documentSnapshot.get("deviceID"), (ArrayList<QRModel>) documentSnapshot.get("qrList"),
                                (int) Integer.parseInt((String)documentSnapshot.get("rank")), (int) Integer.parseInt((String)documentSnapshot.get("highestScore")),
                                (int)Integer.parseInt((String)documentSnapshot.get("lowestScore")), (int)Integer.parseInt((String)documentSnapshot.get("totalScore")));

                        // add data list from player
                        qrDataList = new ArrayList<QRModel>();

                        qrDataList.addAll(model.getQRList());

                        // test
                        qrDataList.add(new QRModel("herbert", "avatar", 300, new ArrayList<QRComment>()));

                        // initialize adapter
                        qrList = findViewById(R.id.qr_list);
                        qrListAdapter = new ProfileQRListAdapter(ProfileQRListViewActivity.this, qrDataList);
                        qrList.setAdapter(qrListAdapter);

                        // set total text
                        totalText.setText("Total QRs: " + model.getTotalQR());
                    }
                });
    }


    /**
     * Gets views from fragment
     */
    public void getViews() {
        // get views from fragment
        this.qrList = findViewById(R.id.qr_list);
        this.backButton = findViewById(R.id.back_button_qr_list);
        this.totalText = findViewById(R.id.total_text);
    }
}
