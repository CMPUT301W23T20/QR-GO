package com.example.qr_go.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qr_go.Actor.Player;
import com.example.qr_go.DataBaseHelper;
import com.example.qr_go.MainActivity;
import com.example.qr_go.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Represents greeting screen if player on device is not already in database
 */
public class GreetingScreenFragment extends Fragment {

    final String TAG = "Sample";
    private Button confirmButton;
    private EditText addUsernameText;
    private EditText addContactText;
    private String android_id;
    private String username;
    private String contact;
    private String message;
    private ArrayList<String> ArrayUsername;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
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
        addContactText = view.findViewById(R.id.addContactText);
        ArrayUsername = new ArrayList<String>();

        db = FirebaseFirestore.getInstance();

        collectionReference = db.collection("Player");

        addToArrayUsername();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = addUsernameText.getText().toString();
                contact = addContactText.getText().toString();
                if (lengthCheck() && uniqueCheck() && validateEmailAddress()) {
                    // create player and push to db
                    Player player = new Player(username, android_id);

                    dbHelper.updateDB(player);


                    getActivity().onBackPressed();
                }
                else {
                    message();
                }
            }
        });
        return view;
    }

    protected Boolean lengthCheck() {
        if (username.length()>0) {
            return true;
        }
        else {
            message = "Username cannot be empty.";
            addUsernameText.setText("");
            addContactText.setText("");
            return false;
        }
    }

    protected void addToArrayUsername() {

        collectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
                                ArrayUsername.add(document.get("Username").toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    protected Boolean uniqueCheck() {
//        System.out.println(ArrayUsername);
        boolean uniqueCheckCaseInsensitive = ArrayUsername.stream().anyMatch(username::equalsIgnoreCase);
        if (uniqueCheckCaseInsensitive) {
            message = "Username already taken.";
            addUsernameText.setText("");
            addContactText.setText("");
            return false;
        }
        else {
            return true;
        }

    }

    protected Boolean validateEmailAddress() {

        if(contact.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(contact).matches()) {
            return true;
        } else {
            message = "Invalid Email Address.";
            addUsernameText.setText("");
            addContactText.setText("");
            return false;
        }

    }

    protected void message() {
//        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}