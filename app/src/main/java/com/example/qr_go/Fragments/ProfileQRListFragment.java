package com.example.qr_go.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.qr_go.Actor.PlayerModel;
import com.example.qr_go.Adapters.ProfileQRListAdapter;
import com.example.qr_go.QR.QRComment;
import com.example.qr_go.QR.QRModel;
import com.example.qr_go.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ProfileQRListFragment extends Fragment {
    private PlayerModel model;
    private View view;
    private String android_id;
    private Button backButton;
    private TextView totalText;
    private ListView qrList;
    private ProfileQRListAdapter qrListAdapter;
    private ArrayList<QRModel> qrDataList;

    public ProfileQRListFragment(String android_id) {
        this.android_id = android_id;
    }

    public static Fragment newInstance(String android_id) {
        ProfileQRListFragment fragment = new ProfileQRListFragment(android_id);
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
        View view = inflater.inflate(R.layout.fragment_player_qr_list, container, false);

        getViews(view);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.profile_container, new PlayerProfileFragment(android_id))
                        .addToBackStack(null)
                        .commit();
            }
        });

        // set single QR view on item click
        // to do*

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        getViews(view);

        updateProfileInfo(view);
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
        this.qrList = view.findViewById(R.id.qr_list);
        this.backButton = view.findViewById(R.id.back_button_qr_list);
        this.totalText = view.findViewById(R.id.total_text);
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

                        // add data list from player
                        qrDataList = new ArrayList<QRModel>();

                        qrDataList.addAll(model.getQRList());

                        // test
                        qrDataList.add(new QRModel("herbert", "avatar", 300, new ArrayList<QRComment>()));

                        // initialize adapter
                        qrList = view.findViewById(R.id.qr_list);
                        qrListAdapter = new ProfileQRListAdapter(getContext(), qrDataList);
                        qrList.setAdapter(qrListAdapter);

                        // set total text
                        totalText.setText("Total QRs: " + model.getTotalQR());
                    }
                });
    }
}
