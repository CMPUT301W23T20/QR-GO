package com.example.qr_go.Activities.QRView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_go.Actor.Player;
import com.example.qr_go.Adapters.QRPlayerListAdapter;
import com.example.qr_go.DataBaseHelper;
import com.example.qr_go.Interfaces.RecyclerViewInterface;
import com.example.qr_go.QR.QR;
import com.example.qr_go.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents list of players that have scanned a QR
 */
public class QRPlayerListViewActivity extends QRActivity implements RecyclerViewInterface {
    QRPlayerListAdapter qrPlayerListAdapter;
    private ArrayList<String> dataPlayerList;
    private RecyclerView playerListView;
    private QR qr;
    private Button backButton;
    private TextView playerText;
    private DataBaseHelper dbHelper;

    public QRPlayerListViewActivity() {
        dbHelper = new DataBaseHelper();
        dataPlayerList = new ArrayList<>();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_player_list_view);

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
//                Intent myIntent = new Intent(QRPlayerListViewActivity.this, QRViewActivity.class);
//                myIntent.putExtra("android_id", android_id);
//                myIntent.putExtra("qr_hash", qr_hash);
//                QRPlayerListViewActivity.this.startActivity(myIntent);
                finish();
            }
        });

        updateQRInfo();
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
        this.playerText = findViewById(R.id.player_text);
        this.playerListView = findViewById(R.id.player_list);
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
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ArrayList<String> deviceIDList = dbHelper.convertPlayerListFromDB((List<String>)documentSnapshot.get("playerList"));

                        ArrayList<String> usernameList = new ArrayList<>();

                        db.collection(Player.class.getSimpleName()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                for(QueryDocumentSnapshot doc: value) {
                                    if(deviceIDList.contains(doc.getId())) {
                                        usernameList.add((String)doc.get("username"));
                                    }
                                }

                                dataPlayerList.addAll(usernameList);

                                playerListView.setLayoutManager(new LinearLayoutManager(QRPlayerListViewActivity.this));
                                qrPlayerListAdapter = new QRPlayerListAdapter(QRPlayerListViewActivity.this, dataPlayerList, QRPlayerListViewActivity.this);
                                playerListView.setAdapter(qrPlayerListAdapter);

                            }
                        });
                    }
                });
    }

    @Override
    public void onItemClick(int i) {

    }
}
