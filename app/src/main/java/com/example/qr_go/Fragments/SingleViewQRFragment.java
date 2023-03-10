package com.example.qr_go.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_go.Actor.Player;
import com.example.qr_go.Adapters.ProfileQRListAdapter;
import com.example.qr_go.Fragments.Profile.ProfileQRListFragment;
import com.example.qr_go.QR.QRComment;
import com.example.qr_go.QR.QR;
import com.example.qr_go.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SingleViewQRFragment extends Fragment {
    private QR model;
    private View view;
    private String android_id;
    private Button backButton;
    private TextView nameText;
    private TextView scoreText;
    private Button playerListButton;
    private RecyclerView commentList;
    private ProfileQRListAdapter qrListAdapter;
    private ArrayList<QR> qrDataList;

    public SingleViewQRFragment(String android_id, QR model) {
        this.android_id = android_id;
        this.model = model;
    }

    public static Fragment newInstance(String android_id, QR model) {
        SingleViewQRFragment fragment = new SingleViewQRFragment(android_id, model);
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_qr_view, container, false);

        getViews(view);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.qr_list_container, new ProfileQRListFragment(android_id))
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
        getViews(view);
    }

    @Override
    public void onResume() {
        super.onResume();

        View view = getView();


    }

    /**
     * Gets views from fragment
     */
    public void getViews(View view) {
        // get views from fragment
        this.nameText = view.findViewById(R.id.name_text);
        this.backButton = view.findViewById(R.id.back_button);
        this.scoreText = view.findViewById(R.id.score_text);
        this.playerListButton = view.findViewById(R.id.player_list_button);
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

}
