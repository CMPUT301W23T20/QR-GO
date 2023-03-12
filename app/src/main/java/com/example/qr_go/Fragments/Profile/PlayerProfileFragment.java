package com.example.qr_go.Fragments.Profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.qr_go.Activities.Profile.OtherProfileQRListViewActivity;
import com.example.qr_go.Activities.Profile.ThisProfileQRListViewActivity;
import com.example.qr_go.Actor.Player;
import com.example.qr_go.DataBaseHelper;
import com.example.qr_go.MainActivity;
import com.example.qr_go.QR.QR;
import com.example.qr_go.R;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PlayerProfileFragment extends Fragment {
    private TextView usernameTextView;
    private Button qrButton;
    private TextView totalScoreTextView;
    private TextView totalScannedTextView;
    private final String android_id;
    private final boolean isThisDevice;
    private Player model;
    private View view;
    private String test;
    private DataBaseHelper dbHelper = new DataBaseHelper();

    private MainActivity mainActivity;

    /**
     * Constructor
     * @param android_id
     * Android ID of player
     * @param isThisDevice
     * If the android ID belongs to player on this device
     */
    public PlayerProfileFragment(String android_id, boolean isThisDevice) {
        this.android_id = android_id;
        this.isThisDevice = isThisDevice;
    }

    public static Fragment newInstance(String android_id, boolean isThisDevice) {
        PlayerProfileFragment fragment = new PlayerProfileFragment(android_id, isThisDevice);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_player_profile, container, false);


        getViews(view);

        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent;
                // allow QR deletion if profile is this player
                if(isThisDevice) {
                    myIntent = new Intent(getActivity(), ThisProfileQRListViewActivity.class);
                }
                // don't allow QR deletion if profile is other player
                else {
                    myIntent = new Intent(getActivity(), OtherProfileQRListViewActivity.class);
                }
                myIntent.putExtra("android_id", android_id);
                startActivity(myIntent);

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        updateProfileInfo(view);
    }

    @Override
    public void onResume() {
        super.onResume();

        View view = getView();

        // only call if data changed
        // to be implemented
        updateProfileInfo(view);
    }

    /**
     * Updates the profile information on screen
     */
    private void updateProfileInfo(View view) {

        getViews(view);


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

                        model = new Player(username, deviceID, qrListFromDoc, rank, highestScore, lowestScore, totalScore);

                        // update UI
                        usernameTextView.setText(model.getUsername());
                        totalScoreTextView.setText("Total Score: " + model.getTotalScore());
                        totalScannedTextView.setText("Total Scanned: " + model.getTotalQR());
                    }
                });
    }


    /**
     * Gets views from fragment
     */
    public void getViews(View view) {
        // get views from fragment
        usernameTextView = view.findViewById(R.id.username_text);
        qrButton = view.findViewById(R.id.my_qr_codes_button);
        totalScoreTextView = view.findViewById(R.id.total_score_text);
        totalScannedTextView = view.findViewById(R.id.total_scanned_text);
    }
}