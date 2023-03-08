package com.example.qr_go;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.qr_go.Actor.Player;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import android.provider.Settings.Secure;

import java.util.ArrayList;
public class PlayerProfileFragment extends Fragment {

    private TextView usernameTextView;
    private Button qrButton;
    private TextView totalScoreTextView;
    private TextView totalScannedTextView;

    private String android_id = Secure.getString(getContext().getContentResolver(), Secure.ANDROID_ID);
    private Player player;

    public PlayerProfileFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(int i) {
        PlayerProfileFragment myFragment = new PlayerProfileFragment();

        Bundle args = new Bundle();
        args.putInt("int", i);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get views from fragment
        usernameTextView = getView().findViewById(R.id.username);
        qrButton = getView().findViewById(R.id.my_qr_codes);
        totalScoreTextView = getView().findViewById(R.id.total_score);
        totalScannedTextView = getView().findViewById(R.id.total_scanned);


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

        /**
         * For testing without database:
         *
         * player = new Player("test123", "123", new ArrayList<QR>(), 1, 123, 0, 12345);
         *
         *         usernameTextView.setText(player.getUsername());
         *         totalScoreTextView.setText("Total Score: " + player.getTotalScore());
         *         totalScannedTextView.setText("Total Scanned: " + player.getTotalQR());
         */

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player_profile, container, false);
    }
}