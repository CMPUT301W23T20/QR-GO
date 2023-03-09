package com.example.qr_go.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.qr_go.Actor.PlayerModel;
import com.example.qr_go.Adapters.ProfileQRListAdapter;
import com.example.qr_go.QR.QR;
import com.example.qr_go.R;

import java.util.ArrayList;

public class QRFragment extends Fragment {
    private QR qr;
    private String android_id;
    private PlayerModel model;
    private Button backButton;

    public QRFragment(QR qr, String android_id) {
        this.qr = qr;
        this.model = model;
    }

    public static Fragment newInstance(QR qr, String android_id) {
        QRFragment fragment = new QRFragment(qr, android_id);
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
        View view = inflater.inflate(R.layout.fragment_qr_view, container, false);

        getViews(view);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.qr_list_container, new ProfileQRListFragment(android_id))
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
        backButton = view.findViewById(R.id.back_button_qr);
    }
}
