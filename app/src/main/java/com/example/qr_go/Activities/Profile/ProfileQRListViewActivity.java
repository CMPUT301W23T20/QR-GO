package com.example.qr_go.Activities.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_go.Activities.QRView.QRViewActivity;
import com.example.qr_go.Actor.Player;
import com.example.qr_go.Adapters.ProfileQRListAdapter;
import com.example.qr_go.Interfaces.RecyclerViewInterface;
import com.example.qr_go.QR.QRComment;
import com.example.qr_go.QR.QR;
import com.example.qr_go.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ProfileQRListViewActivity extends ProfileActivity implements RecyclerViewInterface {
    private Button backButton;
    private TextView totalText;
    private RecyclerView qrList;
    private ProfileQRListAdapter qrListAdapter;
    private ArrayList<QR> qrDataList;
    private Player model;

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
                // will stack between two activities.
//                Intent myIntent = new Intent(ProfileQRListViewActivity.this, PlayerProfileViewActivity.class);
//                myIntent.putExtra("android_id", android_id);
//                ProfileQRListViewActivity.this.startActivity(myIntent);
                finish();
            }
        });

        updateProfileInfo();

        // set single QR view on item click
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(qrList);
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
                        model = new Player((String)documentSnapshot.get("username"), (String)documentSnapshot.get("deviceID"), (ArrayList<QR>) documentSnapshot.get("qrList"),
                                (int) Integer.parseInt((String)documentSnapshot.get("rank")), (int) Integer.parseInt((String)documentSnapshot.get("highestScore")),
                                (int)Integer.parseInt((String)documentSnapshot.get("lowestScore")), (int)Integer.parseInt((String)documentSnapshot.get("totalScore")));

                        // add data list from player
                        qrDataList = new ArrayList<QR>();

                        qrDataList.addAll(model.getQRList());

                        // test
                        qrDataList.add(new QR("herbert", "avatar", 300, new ArrayList<QRComment>()));

                        // initialize adapter
                        qrList = findViewById(R.id.qr_list);
                        qrList.setLayoutManager(new LinearLayoutManager(ProfileQRListViewActivity.this));
                        qrListAdapter = new ProfileQRListAdapter(ProfileQRListViewActivity.this, qrDataList, ProfileQRListViewActivity.this);
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
        this.backButton = findViewById(R.id.qr_list_back_button);
        this.totalText = findViewById(R.id.total_text);
    }

    @Override
    public void onItemClick(int i) {
        Intent myIntent = new Intent(ProfileQRListViewActivity.this, QRViewActivity.class);
        myIntent.putExtra("android_id", android_id);
        myIntent.putExtra("qr_hash", qrDataList.get(i).getQrHash());//Optional parameters
        ProfileQRListViewActivity.this.startActivity(myIntent);
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            qrDataList.remove(viewHolder.getAdapterPosition());
            qrListAdapter.notifyDataSetChanged();
        }
    };
}
