package com.example.qr_go.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.qr_go.Actor.PlayerModel;
import com.example.qr_go.MainActivity;
import com.example.qr_go.QR.QRModel;
import com.example.qr_go.R;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class PlayerProfileFragment extends Fragment {
    private TextView usernameTextView;
    private Button qrButton;
    private TextView totalScoreTextView;
    private TextView totalScannedTextView;
    private final String android_id;
    private PlayerModel model;
    private View view;
    private String test;

    private MainActivity mainActivity;


    public PlayerProfileFragment(String android_id) {
        this.android_id = android_id;
    }

    public static Fragment newInstance(String android_id) {
        PlayerProfileFragment fragment = new PlayerProfileFragment(android_id);
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
        View view = inflater.inflate(R.layout.fragment_player_profile, container, false);

        getViews(view);

        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.profile_container, new ProfileQRListFragment(android_id))
                        .addToBackStack(null)
                        .commit();
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
         CollectionReference collectionReference = db.collection(PlayerModel.class.getSimpleName());

         // put data into class
        db.collection(PlayerModel.class.getSimpleName()).document(android_id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        model = new PlayerModel((String)documentSnapshot.get("username"), (String)documentSnapshot.get("deviceID"), (ArrayList<QRModel>) documentSnapshot.get("qrList"),
                                (int) Integer.parseInt((String)documentSnapshot.get("rank")), (int) Integer.parseInt((String)documentSnapshot.get("highestScore")),
                                (int)Integer.parseInt((String)documentSnapshot.get("lowestScore")), (int)Integer.parseInt((String)documentSnapshot.get("totalScore")));

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