package com.example.qr_go.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.qr_go.Actor.PlayerModel;
import com.example.qr_go.Adapters.ProfileQRListAdapter;
import com.example.qr_go.LeaderboardContent;
import com.example.qr_go.ProfileQRListContent;
import com.example.qr_go.QR.QR;
import com.example.qr_go.R;

import java.util.ArrayList;

public class ProfileQRListFragment extends Fragment {
    private PlayerModel model;
    private View view;

    private Button backButton;

    private ListView qrList;
    private ProfileQRListAdapter qrListAdapter;
    private ArrayList<QR> qrDataList;

    public ProfileQRListFragment(PlayerModel model) {
        this.model = model;
    }

    public static Fragment newInstance(PlayerModel model) {
        ProfileQRListFragment fragment = new ProfileQRListFragment(model);
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
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.qr_list_container, new PlayerProfileFragment(model.getDeviceID()))
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

        qrDataList = new ArrayList<QR>();

        qrDataList.addAll(model.getQRList());

        qrList = view.findViewById(R.id.qr_list);
        qrListAdapter = new ProfileQRListAdapter(getContext(), qrDataList);

        qrList.setAdapter(qrListAdapter);
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
        this.backButton = view.findViewById(R.id.back_button);
    }
}
