package com.example.qr_go.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.qr_go.Actor.Player;
import com.example.qr_go.DataBaseHelper;
import com.example.qr_go.R;

/**
 * Represents greeting screen if player on device is not already in database
 */
public class GreetingScreenFragment extends Fragment {

    final String TAG = "Sample";
    private Button confirmButton;
    private EditText addUsernameText;
    private String android_id;
    private DataBaseHelper dbHelper = new DataBaseHelper();

    public GreetingScreenFragment(String android_id) {
        this.android_id = android_id;
    }

    public static Fragment newInstance(String android_id) {
        GreetingScreenFragment fragment = new GreetingScreenFragment(android_id);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_greeting_screen, container, false);

        confirmButton = view.findViewById(R.id.confirmButton);
        addUsernameText = view.findViewById(R.id.addUsernameText);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = addUsernameText.getText().toString();
                // create player and push to db
                Player player = new Player(username, android_id);

                dbHelper.updateDB(player);

                getActivity().onBackPressed();
            }
        });
        return view;
    }
}