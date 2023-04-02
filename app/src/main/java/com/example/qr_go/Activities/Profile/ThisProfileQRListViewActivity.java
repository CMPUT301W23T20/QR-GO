package com.example.qr_go.Activities.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_go.Activities.QRView.QRViewActivity;
import com.example.qr_go.Actor.Player;
import com.example.qr_go.Adapters.ProfileQRListAdapter;
import com.example.qr_go.DataBaseHelper;
import com.example.qr_go.Interfaces.RecyclerViewInterface;
import com.example.qr_go.QR.QRComment;
import com.example.qr_go.QR.QR;
import com.example.qr_go.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents profile of Player on this device
 */
public class ThisProfileQRListViewActivity extends ProfileActivity implements RecyclerViewInterface {
    private Button backButton;
    private TextView totalText;
    private RecyclerView qrList;
    private ArrayList<QR> qrDataList;
    private ProfileQRListAdapter qrListAdapter;
    private Player player;
    private DataBaseHelper dbHelper = new DataBaseHelper();

    public ThisProfileQRListViewActivity() {
        super();
        qrDataList = new ArrayList<>();
        qrListAdapter = new ProfileQRListAdapter(ThisProfileQRListViewActivity.this, qrDataList, ThisProfileQRListViewActivity.this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_qr_list);

        getViews();

        qrList.setAdapter(qrListAdapter);

        try {
            getIDFromBundle();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        RecyclerView.ItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        qrList.addItemDecoration(divider);

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
                        String username = (String)documentSnapshot.get("username");
                        String deviceID = (String)documentSnapshot.get("deviceID");

                        ArrayList<QR> qrListFromDoc = dbHelper.convertQRListFromDB((List<Map<String, Object>>) documentSnapshot.get("qrList"));

                        int rank = ((Long)documentSnapshot.get("rank")).intValue();
                        int highestScore = ((Long)documentSnapshot.get("highestScore")).intValue();
                        int lowestScore = ((Long)documentSnapshot.get("lowestScore")).intValue();
                        int totalScore = ((Long)documentSnapshot.get("totalScore")).intValue();
                        int theme = ((Long)documentSnapshot.get("theme")).intValue();


                        player = new Player(username, deviceID, qrListFromDoc, rank, highestScore, lowestScore, totalScore, theme);

                        // add data list from player
                        qrDataList = new ArrayList<QR>();

                        qrDataList.addAll(player.getQRList());

                        // sort highest to lowest score
                        Collections.sort(qrDataList);
                        Collections.reverse(qrDataList);

                        // initialize adapter
                        qrList = findViewById(R.id.qr_list);
                        qrList.setLayoutManager(new LinearLayoutManager(ThisProfileQRListViewActivity.this));
                        qrListAdapter = new ProfileQRListAdapter(ThisProfileQRListViewActivity.this, qrDataList, ThisProfileQRListViewActivity.this);
                        qrList.setAdapter(qrListAdapter);

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
        this.qrList = findViewById(R.id.qr_list);
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
        Intent myIntent = new Intent(ThisProfileQRListViewActivity.this, QRViewActivity.class);
        myIntent.putExtra("android_id", android_id);
        myIntent.putExtra("qr_hash", qrDataList.get(i).getQrHash());
        System.out.println(qrDataList.get(i).getQrHash());
        ThisProfileQRListViewActivity.this.startActivity(myIntent);
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int i = viewHolder.getAdapterPosition();

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
                            int theme = ((Long)documentSnapshot.get("theme")).intValue();


                            player = new Player(username, android_id, qrListFromDoc, rank, highestScore, lowestScore, totalScore, theme);

                            // sort highest to lowest score
                            Collections.sort(qrDataList);
                            Collections.reverse(qrDataList);

                            String qrHashRemove = qrDataList.get(i).getQrHash();
                            System.out.println(qrHashRemove);

                            // remove player from qr
                            db.collection(QR.class.getSimpleName()).document(qrHashRemove).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            ArrayList<String> playerList = (ArrayList<String>) documentSnapshot.get("playerList");
                                            playerList.remove(player.getDeviceID());

                                            db.collection(QR.class.getSimpleName()).document(qrHashRemove).update("playerList", playerList);
                                        }
                                    });

                            // remove qr from account
                            player.deleteQR(i);

                            // remove qr from this data set
                            qrDataList.remove(i);

                            // update DB
                            dbHelper.updateDB(player);

                            qrListAdapter.notifyItemRemoved(i);
                        }
                    });

        }
    };
}
