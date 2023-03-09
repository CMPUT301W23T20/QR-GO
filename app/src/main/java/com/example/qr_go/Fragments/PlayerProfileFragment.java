package com.example.qr_go.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.qr_go.Actor.PlayerModel;
import com.example.qr_go.QR.QR;
import com.example.qr_go.R;

import java.util.ArrayList;
public class PlayerProfileFragment extends Fragment {

    private TextView usernameTextView;
    private Button qrButton;
    private TextView totalScoreTextView;
    private TextView totalScannedTextView;

    // private String android_id = Secure.getString(getContext().getContentResolver(), Secure.ANDROID_ID);
    // need to find a way to get the device ID
    private PlayerModel model;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View view;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlayerProfileFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(String param1, String param2) {
        PlayerProfileFragment fragment = new PlayerProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player_profile, container, false);
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

        // get views from fragment
        usernameTextView = view.findViewById(R.id.username_text);
        qrButton = view.findViewById(R.id.my_qr_codes_button);
        totalScoreTextView = view.findViewById(R.id.total_score_text);
        totalScannedTextView = view.findViewById(R.id.total_scanned_text);

        /**
         // get database information
         FirebaseFirestore db = FirebaseFirestore.getInstance();
         DocumentReference documentReference = db.collection(Player.class.getSimpleName()).document(android_id);

         // put data into class
         documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
        @Override
        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
        player = new Player((String)value.get("username"), (String)value.get("deviceID"), (ArrayList<QR>) value.get("qrList"),
        (int)value.get("rank"), (int)value.get("highestScore"), (int)value.get("lowestScore"), (int)value.get("totalScore"));
        }
        });

         // update UI
         usernameTextView.setText(player.getUsername());
         totalScoreTextView.setText("Total Score: " + player.getTotalScore());
         totalScannedTextView.setText("Total Scanned: " + player.getTotalQR());
         */


        model = new PlayerModel("test123", "123", new ArrayList<QR>(), 1, 123, 0, 12345);
        usernameTextView.setText(model.getUsername());
        totalScoreTextView.setText("Total Score: " + model.getTotalScore());
        totalScannedTextView.setText("Total Scanned: " + model.getTotalQR());
    }
}